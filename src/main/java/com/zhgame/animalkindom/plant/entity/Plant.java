package com.zhgame.animalkindom.plant.entity;

import com.zhgame.animalkindom.animal.entity.Eatable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "plant")
public class Plant extends Eatable implements Serializable {

    @Id
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
