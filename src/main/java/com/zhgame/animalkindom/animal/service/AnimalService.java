package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.entity.AnimalData;
import com.zhgame.animalkindom.animal.entity.SleepEnd;
import com.zhgame.animalkindom.tools.DateTool;
import com.zhgame.animalkindom.tools.RandomTool;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class AnimalService {

    public Animal createAnimal(Account account) {
        List<AnimalData> animalData = animalDataService.getAllAnimalData();
        AnimalData animalType = animalData.get(RandomTool.random.nextInt(animalData.size()));
        Animal a = new Animal(animalType);
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

    @Resource
    private AnimalRepository animalRepository;
    @Resource
    private AnimalDataService animalDataService;
    @Resource
    private Map<String, String> gameConfig;
}
