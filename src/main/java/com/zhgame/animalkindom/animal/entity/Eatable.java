package com.zhgame.animalkindom.animal.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Eatable {
    private String name;
    private Integer satietyAdd;
    private Integer vigourAdd;
    private Integer weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSatietyAdd() {
        return satietyAdd;
    }

    public void setSatietyAdd(Integer satietyAdd) {
        this.satietyAdd = satietyAdd;
    }

    public Integer getVigourAdd() {
        return vigourAdd;
    }

    public void setVigourAdd(Integer vigourAdd) {
        this.vigourAdd = vigourAdd;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
