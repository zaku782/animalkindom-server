package com.zhgame.animalkindom.plant.entity;

import com.zhgame.animalkindom.animal.entity.AnimalSimple;

import java.util.List;
import java.util.Map;

public class ExploreEnd {
    private Map<String, List> finds;
    private List<Plant> plants;
    private List<AnimalSimple> animals;

    public Map<String, List> getFinds() {
        return finds;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void setFinds(Map<String, List> finds) {
        this.finds = finds;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

    public List<AnimalSimple> getAnimals() {
        return animals;
    }

    public void setAnimals(List<AnimalSimple> animals) {
        this.animals = animals;
    }
}
