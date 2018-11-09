package com.zhgame.animalkindom.animal.entity;

public class EatEnd {
    private int vigourAdd;
    private int satietyAdd;

    public EatEnd(int satietyAdd, int vigourAdd) {
        this.satietyAdd = satietyAdd;
        this.vigourAdd = vigourAdd;
    }

    public int getVigourAdd() {
        return vigourAdd;
    }

    public void setVigourAdd(int vigourAdd) {
        this.vigourAdd = vigourAdd;
    }

    public int getSatietyAdd() {
        return satietyAdd;
    }

    public void setSatietyAdd(int satietyAdd) {
        this.satietyAdd = satietyAdd;
    }
}
