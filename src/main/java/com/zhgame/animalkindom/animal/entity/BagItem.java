package com.zhgame.animalkindom.animal.entity;

import javax.persistence.*;

@Entity
@Table(name = "bag_item")
public class BagItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private Integer satietyAdd;
    private Integer vigourAdd;
    private Long animalId;
    private Integer weight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
