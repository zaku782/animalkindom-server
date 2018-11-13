package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.animal.entity.*;
import com.zhgame.animalkindom.land.service.LandService;
import com.zhgame.animalkindom.plant.entity.ExploreEnd;
import com.zhgame.animalkindom.plant.entity.Plant;
import com.zhgame.animalkindom.plant.service.PlantService;
import com.zhgame.animalkindom.tools.DateTool;
import com.zhgame.animalkindom.tools.NetMessage;
import com.zhgame.animalkindom.tools.RandomTool;
import com.zhgame.animalkindom.tools.RedisTool;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zhgame.animalkindom.tools.NetMessage.*;

@Component
public class AnimalService {

    public Animal createAnimal(Account account) {
        List<AnimalData> animalData = animalDataService.getAllAnimalData();
        AnimalData animalType = animalData.get(new Random().nextInt(animalData.size()));
        Animal a = new Animal(animalType, gameConfig);
        a.setAccountId(account.getId());
        return a;
    }

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

            exploreGetTimes = 20;

            //当前所在地图的植物产出概率,plantRate需要扣除产出减益,最少保留
            int plantRate = Math.max(landService.getLandPlantRate().get(animal.getCurrentLand()) -
                    new BigDecimal(landService.getLandPlantCost(animal.getCurrentLand()))
                            .divide(new BigDecimal(gameConfig.get("plantYieldDescPerCost")), BigDecimal.ROUND_DOWN).intValue(), Integer.parseInt(gameConfig.get("plantYieldLeast")));
            //计算可以获得的植物
            List<Plant> plants = Stream.iterate(0, n -> n + 1)
                    .map(n -> yieldPlant(plantRate) && discoverPlant(animal))
                    .limit(exploreGetTimes)
                    .filter(get -> get)
                    .map(get -> getPlant()).collect(Collectors.toList());
            //清空探索时间
            animal.setExploreTime(null);

            Map<String, List> finds = new HashMap<>();
            finds.put("plant", plants);
            exploreEnd.setFinds(finds);
            //redis记录探索到的资源,保存一段时间,同时用于拾取资源时服务器验证,移动到别的大陆时删除这些资源
            redisTool.set("explore_" + animal.getId(), finds);


            //发现地图
        } else {
            animal.setExploreTime(DateTool.getNowMillis());
        }
        animalRepository.save(animal);
        return exploreEnd;
    }


    @SuppressWarnings("unchecked")
    public Map<String, List> getFinds(Animal animal) {
        return (Map<String, List>) redisTool.get("explore_" + animal.getId());
    }

    public Plant getPlant() {
        List<Plant> plants = plantService.getPlants();
        return plants.get(new Random().nextInt(plants.size()));
    }

    public boolean yieldPlant(int plantRate) {
        return RandomTool.happen(plantRate, 100);
    }

    public boolean discoverPlant(Animal animal) {
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
        if (bagItem != null) {
            Map<String, Integer> eatRecover = eat(animal, bagItem);
            eatEnd.setSatietyAdd(eatRecover.get("satiety"));
            eatEnd.setVigourAdd(eatRecover.get("vigour"));
            animalRepository.save(animal);
            bagItemRepository.delete(itemId);
        }
        return eatEnd;
    }

    public Map<String, Integer> eat(Animal animal, Eatable eatable) {
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
        Map<String, List> finds = getFinds(animal);
        Object plant = finds.get("plant");
        if (plant != null) {
            List<Plant> plants = (List<Plant>) plant;
            Optional<Plant> toOperate = plants.stream().filter(p -> p.getName().equals(plantName)).findFirst();
            if (toOperate.isPresent()) {
                Optional<String> msg = f.apply(toOperate.get());
                if (msg.isPresent()) {
                    return new NetMessage(msg.get(), DANGER);
                }
                plants.remove(toOperate.get());
                redisTool.set("explore_" + animal.getId(), finds);
                //当前区域资源消耗计算
                landService.landPlantCost(animal.getCurrentLand());
            }
        }
        return new NetMessage(STATUS_OK, SUCCESS);
    }

    public List<BagItem> getBagItems(Animal animal) {
        return bagItemRepository.getByAnimalId(animal.getId());
    }

    @Resource
    private AnimalRepository animalRepository;
    @Resource
    private AnimalDataService animalDataService;
    @Resource
    private Map<String, String> gameConfig;
    @Resource
    private LandService landService;
    @Resource
    private PlantService plantService;
    @Resource
    private RedisTool redisTool;
    @Resource
    private BagItemRepository bagItemRepository;
}
