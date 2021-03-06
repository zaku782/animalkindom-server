package com.zhgame.animalkindom.land.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "land")
public class Land implements Serializable {

    @Id
    private Integer id;

    private String name;

    private Integer plantRate;

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

    public Integer getPlantRate() {
        return plantRate;
    }

    public void setPlantRate(Integer plantRate) {
        this.plantRate = plantRate;
    }
}
