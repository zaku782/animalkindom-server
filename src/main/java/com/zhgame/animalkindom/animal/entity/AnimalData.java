package com.zhgame.animalkindom.animal.entity;

import javax.persistence.*;

@Entity
@Table(name = "animal_data")
public class AnimalData {

    @Id
    private Long id;

    private String name;
    private Integer baseHealth;
    private Integer baseSatiety;
    private Integer baseStrength;
    private Integer baseIntelligence;
    private Integer baseAgile;
    private Integer baseSpeed;
    private Integer maxGrowLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getMaxGrowLevel() {
        return maxGrowLevel;
    }

    public void setMaxGrowLevel(Integer maxGrowLevel) {
        this.maxGrowLevel = maxGrowLevel;
    }

    public Integer getBaseSatiety() {
        return baseSatiety;
    }

    public void setBaseSatiety(Integer baseSatiety) {
        this.baseSatiety = baseSatiety;
    }
}
