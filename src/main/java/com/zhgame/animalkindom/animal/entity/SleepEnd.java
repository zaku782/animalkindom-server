package com.zhgame.animalkindom.animal.entity;

public class SleepEnd {
    private int vigourRecover;
    private int satietyCost;

    public SleepEnd(int vigourRecover, int satietyCost) {
        this.vigourRecover = vigourRecover;
        this.satietyCost = satietyCost;
    }

    public int getVigourRecover() {
        return vigourRecover;
    }

    public void setVigourRecover(int vigourRecover) {
        this.vigourRecover = vigourRecover;
    }

    public int getSatietyCost() {
        return satietyCost;
    }

    public void setSatietyCost(int satietyCost) {
        this.satietyCost = satietyCost;
    }
}
