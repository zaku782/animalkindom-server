package com.zhgame.animalkindom.plant.service;

import com.zhgame.animalkindom.plant.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlantRepository extends JpaRepository<Plant, Long> {

}
