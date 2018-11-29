package com.zhgame.animalkindom.redis.service;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.entity.AnimalData;
import com.zhgame.animalkindom.animal.entity.AnimalSimple;
import com.zhgame.animalkindom.animal.service.AnimalDataRepository;
import com.zhgame.animalkindom.config.service.GameConfigDataRepository;
import com.zhgame.animalkindom.land.entity.Land;
import com.zhgame.animalkindom.land.service.LandRepository;
import com.zhgame.animalkindom.plant.entity.Plant;
import com.zhgame.animalkindom.plant.service.PlantRepository;
import com.zhgame.animalkindom.tools.CookieTool;
import com.zhgame.animalkindom.tools.RedisTool;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class RedisService {

    private final String EXPLORE_PLANTS_KEY = "explore_plants_";
    private final String EXPLORE_ANIMALS_KEY = "explore_animals_";
    private final String LAND_PLANT_COST = "land_plant_cost_";
    private final String LAND_ANIMAL_KEY = "land_animals_";
    private final String SOCKET_KET = "sockets";

    @Cacheable(value = "land_datas")
    public List<Land> getLandData() {
        return landRepository.findAll();
    }

    @Cacheable(value = "plants")
    public List<Plant> getPlants() {
        return plantRepository.findAll();
    }

    @Cacheable(value = "animal_datas")
    public List<AnimalData> getAllAnimalData() {
        return animalDataRepository.findAll();
    }

    public void addToken(String token, Account accountSession) {
        redisTool.set(token, accountSession, CookieTool.COOKIE_TIME);
    }

    public void removeToken(String token) {
        redisTool.del(token);
    }

    public Account getAccountByToken(String token) {
        return (Account) redisTool.get(token);
    }

    public void recordExplorePlants(final Long animalId, List<Plant> plants) {
        redisTool.del(EXPLORE_PLANTS_KEY + animalId);
        plants.forEach(plant -> redisTool.lSet(EXPLORE_PLANTS_KEY + animalId, plant));
    }

    public void recordExploreAnimals(final Long animalId, List<AnimalSimple> animals) {
        redisTool.del(EXPLORE_ANIMALS_KEY + animalId);
        animals.forEach(animal -> redisTool.lSet(EXPLORE_ANIMALS_KEY + animalId, animal));
    }

    public List<Object> getExplorePlants(Long animalId) {
        List<Object> plants = redisTool.lGet(EXPLORE_PLANTS_KEY + animalId, 0, -1);
        if (plants == null) {
            plants = new ArrayList<>();
        }
        return plants;
    }

    public void consumePlant(Long animalId, Plant plant) {
        redisTool.lRemove(EXPLORE_PLANTS_KEY + animalId, 0, plant);
    }

    public synchronized void landPlantCost(Integer landId) {
        Integer plantCost = (Integer) redisTool.get(LAND_PLANT_COST + landId);
        if (plantCost == null) {
            redisTool.set(LAND_PLANT_COST + landId, 0, Long.parseLong(gameConfig.get("plantYieldRecoverCycle")));
        } else {
            redisTool.increment(LAND_PLANT_COST + landId, 1);
        }
    }

    public Integer getLandPlantCost(Integer landId) {
        Object cost = redisTool.get(LAND_PLANT_COST + landId);
        if (cost != null) {
            return Integer.parseInt(cost.toString());
        } else {
            return 0;
        }
    }

    public void moveToLand(Integer landId, Animal animal) {
        redisTool.sSet(LAND_ANIMAL_KEY + landId, animal.getSimple());
    }

    public void leaveLand(Integer landId, Animal animal) {
        //离开大陆时,清除已发现的资源
        redisTool.del(EXPLORE_PLANTS_KEY + animal.getId());
        redisTool.del(EXPLORE_ANIMALS_KEY + animal.getId());
        redisTool.setRemove(LAND_ANIMAL_KEY + landId, animal.getSimple());
    }

    public Set<Object> getLandAnimals(Integer landId) {
        Set<Object> animals = redisTool.sGet(LAND_ANIMAL_KEY + landId);
        if (animals == null) {
            animals = new HashSet<>();
        }
        return animals;
    }

    @Resource
    private AnimalDataRepository animalDataRepository;

    @Resource
    private PlantRepository plantRepository;
    @Resource
    private LandRepository landRepository;
    @Resource
    private RedisTool redisTool;
    @Resource
    private GameConfigDataRepository gameConfigDataRepository;
    @Resource
    private Map<String, String> gameConfig;
}
