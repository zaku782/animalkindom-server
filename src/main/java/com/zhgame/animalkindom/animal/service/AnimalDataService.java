package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.animal.entity.AnimalData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AnimalDataService {

    private static List<AnimalData> animalData;

    List<AnimalData> getAllAnimalData() {
        if (animalData == null) {
            animalData = animalDataRepository.findAll();
        }
        return animalData;
    }

    @Resource
    private AnimalDataRepository animalDataRepository;
}
