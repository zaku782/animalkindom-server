package com.zhgame.animalkindom.config.service;

import com.zhgame.animalkindom.config.entity.GameConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GameConfigService {

    private static Map<String, String> gameConfig = new HashMap<>();

    public Map<String, String> getGameConfig() {
        synchronized (gameConfig) {
            if (gameConfig.size() == 0) {
                List<GameConfig> configs = gameConfigRepository.findAll();
                configs.stream().forEach(config -> gameConfig.put(config.getProp(), config.getValue()));
            }
            return gameConfig;
        }
    }


    @Resource
    private GameConfigRepository gameConfigRepository;
}
