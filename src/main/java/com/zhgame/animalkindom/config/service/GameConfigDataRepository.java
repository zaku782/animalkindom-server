package com.zhgame.animalkindom.config.service;

import com.zhgame.animalkindom.config.entity.GameConfigData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GameConfigDataRepository extends JpaRepository<GameConfigData, Long> {

}
