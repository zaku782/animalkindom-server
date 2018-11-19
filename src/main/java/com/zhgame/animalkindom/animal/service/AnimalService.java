package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.animal.entity.*;
import com.zhgame.animalkindom.land.entity.Land;
import com.zhgame.animalkindom.land.service.LandService;
import com.zhgame.animalkindom.plant.entity.ExploreEnd;
import com.zhgame.animalkindom.plant.entity.Plant;
import com.zhgame.animalkindom.redis.service.RedisService;
import com.zhgame.animalkindom.tools.DateTool;
import com.zhgame.animalkindom.tools.NetMessage;
import com.zhgame.animalkindom.tools.RandomTool;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.zhgame.animalkindom.tools.NetMessage.*;
import static java.util.stream.Collectors.toList;

@Component
public class AnimalService {

    private final Integer PERCENT_MAX = 100;

    public Animal getByAccount(Account account) {
        Animal animal = animalRepository.getByAccountId(account.getId());
        animal.setAccountName(account.getName());
        return animal;
    }

    public Animal save(Animal animal) {
        return animalRepository.save(animal);
    }

    public SleepEnd sleep(Animal animal) {

        int vigourRecover = 0;
        int satietyCost = 0;

        if (animal.getSleepTime() != null) {
            int recoverTimes = new BigDecimal(DateTool.getNowMillis())
                    .subtract(new BigDecimal(animal.getSleepTime()))
                    .divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP)
                    .divide(new BigDecimal(gameConfig.get("sleepVigourRecoverInterval")), 3, BigDecimal.ROUND_HALF_UP)
                    .intValue();

            if (recoverTimes > 0) {
                vigourRecover = new BigDecimal(recoverTimes)
                        .multiply(new BigDecimal(gameConfig.get("sleepVigourRecover")))
                        .intValue();

                int afterRecoverVigour = Math.min(100, animal.getVigour() + vigourRecover);
                //实际恢复精力
                vigourRecover = afterRecoverVigour - animal.getVigour();
                animal.setVigour(afterRecoverVigour);

                satietyCost = Math.max(1, new BigDecimal(gameConfig.get("sleepSatietyCost"))
                        .multiply(new BigDecimal(animal.getBaseSatiety()))
                        .multiply(new BigDecimal(recoverTimes)).intValue());

                int afterRecoverSatiety = Math.max(0, animal.getSatiety() - satietyCost);
                //实际饱食度消耗
                satietyCost = animal.getSatiety() - afterRecoverSatiety;
                animal.setSatiety(afterRecoverSatiety);
            }
            animal.setSleepTime(null);
        } else {
            animal.setSleepTime(DateTool.getNowMillis());
        }

        animalRepository.save(animal);

        return new SleepEnd(vigourRecover, satietyCost);
    }

    public ExploreEnd explore(Animal animal) {
        ExploreEnd exploreEnd = new ExploreEnd();
        if (animal.getExploreTime() != null) {
            int exploreGetTimes = new BigDecimal(DateTool.getNowMillis())
                    .subtract(new BigDecimal(animal.getExploreTime()))
                    .divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP)
                    .divide(new BigDecimal(gameConfig.get("exploreInterval")), 3, BigDecimal.ROUND_HALF_UP)
                    .intValue();

            //当前所在地图的植物产出概率,plantRate需要扣除产出减益
            List<Land> lands = redisService.getLandData();
            int plantRate = landService.getPlantRate(lands.stream()
                    .filter(land -> land.getId().intValue() == animal.getCurrentLand().intValue())
                    .findFirst()
                    .get());

            //计算可以获得的植物
            List<Plant> plants = Stream.iterate(0, n -> n + 1)
                    .map(n -> yieldPlant(plantRate) && discoverPlant(animal))
                    .limit(exploreGetTimes)
                    .filter(get -> get)
                    .map(get -> getPlant()).collect(toList());

            //清空探索时间
            animal.setExploreTime(null);
            exploreEnd.setPlants(plants);

            //redis记录探索到的资源,保存一段时间,同时用于拾取资源时服务器验证,移动到别的大陆时删除这些资源
            redisService.recordExplorePlants(animal.getId(), plants);

            //发现玩家
            List<Animal> animals = redisService.getLandAnimals(animal.getCurrentLand())
                    .stream()
                    .map(o -> (Animal) o)
                    .filter(a -> (a.getId().longValue() != animal.getId().longValue()) && findAnimal(a, animal))
                    .collect(toList());
            exploreEnd.setAnimals(animals);

            //发现地图
        } else {
            animal.setExploreTime(DateTool.getNowMillis());
        }
        animalRepository.save(animal);
        return exploreEnd;
    }

    public boolean findAnimal(Animal target, Animal self) {
        Integer base = Integer.parseInt(gameConfig.get("animalFindBaseRate"));
        return RandomTool.happen(base + (self.getIntelligence() - target.getIntelligence()), 100);
    }

    public List<Animal> getAnimalByLand(Integer landId) {
        return null;
    }

    public List<Plant> getFindPlants(Animal animal) {
        return redisService.getExplorePlants(animal.getId()).stream().map(o -> (Plant) o).collect(toList());
    }

    private Plant getPlant() {
        List<Plant> plants = redisService.getPlants();
        return plants.get(new Random().nextInt(plants.size()));
    }

    private boolean yieldPlant(int plantRate) {
        return RandomTool.happen(plantRate, 100);
    }

    private boolean discoverPlant(Animal animal) {
        return RandomTool.happen(animal.getIntelligence(), 100);
    }

    @Transactional
    public EatEnd eatAtOnce(Animal animal, String plantName) {
        final EatEnd eatEnd = new EatEnd(0, 0);
        findPlantFromRedisAndOperate(animal, plantName, (Plant plant) -> {
            Map<String, Integer> eatRecover = eat(animal, plant);
            eatEnd.setSatietyAdd(eatRecover.get("satiety"));
            eatEnd.setVigourAdd(eatRecover.get("vigour"));
            animalRepository.save(animal);
            return Optional.empty();
        });
        return eatEnd;
    }

    @Transactional
    public EatEnd eatFromBag(Animal animal, Long itemId) {
        final EatEnd eatEnd = new EatEnd(0, 0);
        BagItem bagItem = bagItemRepository.getOne(itemId);
        if (bagItem.getId() != null) {
            Map<String, Integer> eatRecover = eat(animal, bagItem);
            eatEnd.setSatietyAdd(eatRecover.get("satiety"));
            eatEnd.setVigourAdd(eatRecover.get("vigour"));
            animalRepository.save(animal);
            bagItemRepository.delete(bagItem);
        }
        return eatEnd;
    }

    private Map<String, Integer> eat(Animal animal, Eatable eatable) {
        Integer preSatiety = animal.getSatiety();
        Integer preVigour = animal.getVigour();
        animal.setSatiety(Math.min(animal.getBaseSatiety(), animal.getSatiety() + eatable.getSatietyAdd()));
        animal.setVigour(Math.min(100, animal.getVigour() + eatable.getVigourAdd()));
        Map<String, Integer> eatRecover = new HashMap<>();
        eatRecover.put("satiety", animal.getSatiety() - preSatiety);
        eatRecover.put("vigour", animal.getVigour() - preVigour);
        return eatRecover;
    }

    public Integer getBagLoad(Animal animal) {
        return animal.getBaseSatiety() / 10;
    }

    public NetMessage collectPlant(Animal animal, String plantName) {
        return findPlantFromRedisAndOperate(animal, plantName, (Plant plant) -> {
            Integer maxLoad = getBagLoad(animal);
            List<BagItem> items = bagItemRepository.getByAnimalId(animal.getId());
            if (items.stream().mapToInt(BagItem::getWeight).sum() + plant.getWeight() > maxLoad) {
                return Optional.of(BAG_FULL);
            }

            BagItem bagItem = new BagItem();
            bagItem.setAnimalId(animal.getId());
            bagItem.setName(plant.getName());
            bagItem.setSatietyAdd(plant.getSatietyAdd());
            bagItem.setVigourAdd(plant.getVigourAdd());
            bagItem.setWeight(plant.getWeight());
            bagItemRepository.save(bagItem);

            return Optional.empty();
        });
    }

    @SuppressWarnings("unchecked")
    private NetMessage findPlantFromRedisAndOperate(Animal animal, String plantName, Function<Plant, Optional<String>> f) {
        List<Plant> plants = getFindPlants(animal);
        Optional<Plant> toOperate = plants.stream().filter(p -> p.getName().equals(plantName)).findFirst();
        if (toOperate.isPresent()) {
            Optional<String> msg = f.apply(toOperate.get());
            if (msg.isPresent()) {
                return new NetMessage(msg.get(), DANGER);
            }
            redisService.consumePlant(animal.getId(), toOperate.get());
            //当前区域资源消耗计算
            redisService.landPlantCost(animal.getCurrentLand());
        }
        return new NetMessage(STATUS_OK, SUCCESS);
    }

    public List<BagItem> getBagItems(Animal animal) {
        return bagItemRepository.getByAnimalId(animal.getId());
    }

    /**
     * 新生
     *
     * @param account 关联账号
     */
    public void metempsychosis(Account account) {
        this.metempsychosis(null, account, 0);
    }

    /**
     * 转生
     *
     * @param animal   现世物种
     * @param account  关联账号
     * @param useSouls 使用魂值
     */
    @Transactional
    public NetMessage metempsychosis(Animal animal, Account account, Integer useSouls) {

        if (animal != null) {
            if (useSouls > animal.getSouls()) {
                return new NetMessage(NetMessage.STATUS_OK, NetMessage.DANGER, NetMessage.STATUS_INVALID_OPERATION);
            }
        }

        List<AnimalData> animalData = redisService.getAllAnimalData();

        Integer number = new Random().nextInt(100);

        if (animal != null) {
            number = number - new BigDecimal(useSouls)
                    .divide(new BigDecimal(gameConfig.get("metempsychosisRateBuffPerSouls")), BigDecimal.ROUND_DOWN)
                    .intValue();

            number = Math.max(0, number);
        }

        Integer level = getMetempsychosisLevel(animal, number);

        List<AnimalData> levelAnimals = animalData.stream()
                .filter(data -> data.getLevel().intValue() == level.intValue())
                .collect(toList());

        Animal newAnimal = new Animal(levelAnimals.get(new Random().nextInt(levelAnimals.size())), gameConfig);

        if (animal != null) {
            newAnimal.setId(animal.getId());
            if (newAnimal.getTypeId().intValue() == animal.getTypeId().intValue()) {
                newAnimal.setGrowLevel(animal.getGrowLevel() + 1);
                upgrade(newAnimal);
            }

            newAnimal.setPoint(new BigDecimal(animal.getSouls() - useSouls)
                    .divide(new BigDecimal(gameConfig.get("pointGetPerSouls")), BigDecimal.ROUND_DOWN)
                    .intValue());
        }

        newAnimal.setAccountId(account.getId());

        animalRepository.save(newAnimal);

        //清空行囊
        bagItemRepository.deleteByAnimalId(animal.getId());

        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS);
    }

    public void upgrade(Animal animal) {
        Integer level = animal.getGrowLevel();
        animal.setAgile(propUpgrade(animal.getAgile(), level));
        animal.setStrength(propUpgrade(animal.getStrength(), level));
        animal.setIntelligence(propUpgrade(animal.getIntelligence(), level));
        animal.setSpeed(propUpgrade(animal.getSpeed(), level));
    }

    public Integer propUpgrade(Integer old, Integer level) {
        return new BigDecimal(old)
                .multiply(new BigDecimal(1)
                        .add(new BigDecimal(gameConfig.get("levelBuff"))
                                .multiply(new BigDecimal(level)))).intValue();
    }

    public Integer getMetempsychosisLevel(Animal animal, Integer number) {
        String[] rates = gameConfig.get("metempsychosisRate").split(",");
        return IntStream.range(0, rates.length)
                .filter(index -> number < Integer.parseInt(rates[index]))
                .findFirst()
                .getAsInt() + 1;
    }

    public NetMessage addPoint(Animal animal, PointAdd pointAdd) {

        if (pointAdd.sum() > animal.getPoint()) {
            return new NetMessage(NetMessage.STATUS_OK, NetMessage.DANGER, NetMessage.STATUS_INVALID_OPERATION);
        }

        animal.setStrength(animal.getStrength() + pointAdd.getStrengthAdd());
        animal.setIntelligence(animal.getIntelligence() + pointAdd.getIntelligenceAdd());
        animal.setAgile(animal.getAgile() + pointAdd.getAgileAdd());
        animal.setSpeed(animal.getSpeed() + pointAdd.getSpeedAdd());
        animal.setPoint(animal.getPoint() - pointAdd.sum());

        animalRepository.save(animal);

        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS);
    }

    @Resource
    private AnimalRepository animalRepository;
    @Resource
    private Map<String, String> gameConfig;
    @Resource
    private LandService landService;
    @Resource
    private RedisService redisService;
    @Resource
    private BagItemRepository bagItemRepository;
}
