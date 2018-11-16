package com.zhgame.animalkindom.animal.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "animal_data")
public class AnimalData implements Serializable {

    @Id
    private Integer id;

    private String name;
    private Integer baseHealth;
    private Integer baseSatiety;
    private Integer baseStrength;
    private Integer baseIntelligence;
    private Integer baseAgile;
    private Integer baseSpeed;
    private Integer growDay;
    private Integer growLevel;
    private Integer level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseHealth() {
        return baseHealth;
    }

    public void setBaseHealth(Integer baseHealth) {
        this.baseHealth = baseHealth;
    }

    public Integer getBaseStrength() {
        return baseStrength;
    }

    public void setBaseStrength(Integer baseStrength) {
        this.baseStrength = baseStrength;
    }

    public Integer getBaseIntelligence() {
        return baseIntelligence;
    }

    public void setBaseIntelligence(Integer baseIntelligence) {
        this.baseIntelligence = baseIntelligence;
    }

    public Integer getBaseAgile() {
        return baseAgile;
    }

    public void setBaseAgile(Integer baseAgile) {
        this.baseAgile = baseAgile;
    }

    public Integer getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(Integer baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public Integer getBaseSatiety() {
        return baseSatiety;
    }

    public void setBaseSatiety(Integer baseSatiety) {
        this.baseSatiety = baseSatiety;
    }

    public Integer getGrowDay() {
        return growDay;
    }

    public void setGrowDay(Integer growDay) {
        this.growDay = growDay;
    }

    public Integer getGrowLevel() {
        return growLevel;
    }

    public void setGrowLevel(Integer growLevel) {
        this.growLevel = growLevel;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
