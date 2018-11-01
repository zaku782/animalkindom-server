package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.GameConfig;
import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.entity.AnimalData;
import com.zhgame.animalkindom.animal.entity.SleepEnd;
import com.zhgame.animalkindom.tools.CalculateTool;
import com.zhgame.animalkindom.tools.DateTool;
import com.zhgame.animalkindom.tools.RandomTool;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
            int recoverTimes = (int) (DateTool.getNowMillis() - animal.getSleepTime()) / 1000 / GameConfig.sleepVigourRecoverMinInterval;
            if (recoverTimes > 0) {
                vigourRecover = recoverTimes * GameConfig.sleepVigourRecover;
                int afterRecover = Math.min(100, animal.getVigour() + vigourRecover);
                //实际恢复精力
                vigourRecover = afterRecover - animal.getVigour();
                animal.setVigour(afterRecover);
                satietyCost = Math.max(1, CalculateTool.calToInteger(GameConfig.sleepSatietyCost * animal.getBaseSatiety() * recoverTimes));
                animal.setSatiety(Math.max(0, animal.getSatiety() - satietyCost));
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
}
