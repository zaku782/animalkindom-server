package com.zhgame.animalkindom.animal.service;

import com.zhgame.animalkindom.animal.entity.AnimalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalDataRepository extends JpaRepository<AnimalData, Long> {

}
