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
                animal.setVigour(Math.min(100, animal.getVigour() + vigourRecover));
                //醒来时,若因为时间太短导致扣除的饱食度为0,此时要扣除1点
                satietyCost = Math.max(1, CalculateTool.calToInteger(GameConfig.sleepSatietyCost * animal.getBaseSatiety()) * recoverTimes);
                animal.setSatiety(animal.getSatiety() - satietyCost);
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
