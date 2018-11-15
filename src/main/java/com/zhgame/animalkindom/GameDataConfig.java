package com.zhgame.animalkindom;

import com.zhgame.animalkindom.config.entity.GameConfigData;
import com.zhgame.animalkindom.config.service.GameConfigDataRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class GameDataConfig {

    @Bean
    public Map<String, String> gameConfig() {
        Map<String, String> gameConfig = new HashMap<>();
        List<GameConfigData> configs = gameConfigDataRepository.findAll();
        configs.forEach(config -> gameConfig.put(config.getProp(), config.getValue()));
        return gameConfig;
    }

    @Resource
    private GameConfigDataRepository gameConfigDataRepository;
}