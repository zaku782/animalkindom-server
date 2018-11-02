package com.zhgame.animalkindom.map.entity;

public class MoveEnd {
    private String vigourCost;
    private String satietyCost;

    public MoveEnd(String vigourCost, String satietyCost) {
        this.vigourCost = vigourCost;
        this.satietyCost = satietyCost;
    }

    public String getVigourCost() {
        return vigourCost;
    }

    public void setVigourCost(String vigourCost) {
        this.vigourCost = vigourCost;
    }

    public String getSatietyCost() {
        return satietyCost;
    }

    public void setSatietyCost(String satietyCost) {
        this.satietyCost = satietyCost;
    }
}
