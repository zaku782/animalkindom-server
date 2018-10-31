package com.zhgame.animalkindom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component()
@ConfigurationProperties(prefix = "game")
public class GameConfig {
    public static int maxMapNumber;
    public static int initMapCapacity;
    public static int mapMoveInterval;
    public static float moveSatietyCostPercent;
    public static int moveVigourCost;
    public static int sleepVigourRecoverMinInterval;
    public static int sleepVigourRecover;
    public static float sleepSatietyCost;


    @Value("${game.maxMapNumber}")
    public static void setMaxMapNumber(int maxMapNumber) {
        GameConfig.maxMapNumber = maxMapNumber;
    }

    @Value("${game.initMapCapacity}")
    public static void setInitMapCapacity(int initMapCapacity) {
        GameConfig.initMapCapacity = initMapCapacity;
    }

    @Value("${game.mapMoveInterval}")
    public static void setMapMoveInterval(int mapMoveInterval) {
        GameConfig.mapMoveInterval = mapMoveInterval;
    }

    @Value("${game.moveSatietyCostPercent}")
    public static void setMoveSatietyCostPercent(float moveSatietyCostPercent) {
        GameConfig.moveSatietyCostPercent = moveSatietyCostPercent;
    }

    @Value("${game.moveVigourCost}")
    public static void setMoveVigourCost(int moveVigourCost) {
        GameConfig.moveVigourCost = moveVigourCost;
    }

    @Value("${game.sleepVigourRecoverMinInterval}")
    public static void setSleepVigourRecoverMinInterval(int sleepVigourRecoverMinInterval) {
        GameConfig.sleepVigourRecoverMinInterval = sleepVigourRecoverMinInterval;
    }

    @Value("${game.sleepVigourRecover}")
    public static void setSleepVigourRecover(int sleepVigourRecover) {
        GameConfig.sleepVigourRecover = sleepVigourRecover;
    }

    @Value("${game.sleepSatietyCost}")
    public static void setSleepSatietyCost(int sleepSatietyCost) {
        GameConfig.sleepSatietyCost = sleepSatietyCost;
    }
}
