package com.zhgame.animalkindom.plant.service;

import com.zhgame.animalkindom.plant.entity.Plant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class PlantService {

    private List<Plant> plants;

    public synchronized List<Plant> getPlants() {
        if (plants == null) {
            plants = plantRepository.findAll();
        }
        return plants;
    }

    @Resource
    private PlantRepository plantRepository;
}
