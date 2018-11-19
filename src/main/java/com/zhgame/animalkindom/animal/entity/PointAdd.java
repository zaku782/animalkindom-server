package com.zhgame.animalkindom.animal.entity;

public class PointAdd {
    private Integer strengthAdd;
    private Integer intelligenceAdd;
    private Integer agileAdd;
    private Integer speedAdd;

    public Integer sum() {
        return this.strengthAdd + this.intelligenceAdd + this.agileAdd + this.speedAdd;
    }

    public Integer getStrengthAdd() {
        return strengthAdd;
    }

    public void setStrengthAdd(Integer strengthAdd) {
        this.strengthAdd = strengthAdd;
    }

    public Integer getIntelligenceAdd() {
        return intelligenceAdd;
    }

    public void setIntelligenceAdd(Integer intelligenceAdd) {
        this.intelligenceAdd = intelligenceAdd;
    }

    public Integer getAgileAdd() {
        return agileAdd;
    }

    public void setAgileAdd(Integer agileAdd) {
        this.agileAdd = agileAdd;
    }

    public Integer getSpeedAdd() {
        return speedAdd;
    }

    public void setSpeedAdd(Integer speedAdd) {
        this.speedAdd = speedAdd;
    }
}
