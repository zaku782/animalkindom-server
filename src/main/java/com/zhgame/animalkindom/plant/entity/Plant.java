package com.zhgame.animalkindom.plant.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "plant")
public class Plant {

    @Id
    private Integer id;

    private String name;

    private Integer satietyAdd;
    private Integer vigourAdd;
    private Integer weight;

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
