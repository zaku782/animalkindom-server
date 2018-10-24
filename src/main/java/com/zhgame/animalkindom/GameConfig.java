package com.zhgame.animalkindom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component()
@ConfigurationProperties(prefix = "game")
public class GameConfig {
    public static int maxMapNumber;
    public static int initMapCapacity;

    @Value("${game.maxMapNumber}")
    public static void setMaxMapNumber(int maxMapNumber) {
        GameConfig.maxMapNumber = maxMapNumber;
    }

    @Value("${game.initMapCapacity}")
    public static void setInitMapCapacity(int initMapCapacity) {
        GameConfig.initMapCapacity = initMapCapacity;
    }
}
