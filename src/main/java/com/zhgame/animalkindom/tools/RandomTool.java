package com.zhgame.animalkindom.tools;

import java.util.Random;

public class RandomTool {
    public static boolean happen(int rate, int base) {
        return new Random().nextInt(base) < rate;
    }
}
