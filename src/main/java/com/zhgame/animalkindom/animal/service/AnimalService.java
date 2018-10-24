package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.entity.AnimalData;
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

    public void sleep(Animal animal) {
        if (animal.getSleepTime() != null) {
            animal.setSleepTime(null);
        } else {
            animal.setSleepTime(DateTool.getNowMillis());
        }

        animalRepository.save(animal);
    }

    @Resource
    private AnimalRepository animalRepository;
    @Resource
    private AnimalDataService animalDataService;
}
