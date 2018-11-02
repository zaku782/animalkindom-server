package com.zhgame.animalkindom.config.service;

import com.zhgame.animalkindom.config.entity.GameConfig;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GameConfigRepository extends JpaRepository<GameConfig, Long> {

}
