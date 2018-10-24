package com.zhgame.animalkindom.animal.entity;

import com.zhgame.animalkindom.GameConfig;
import com.zhgame.animalkindom.tools.BitArray;
import com.zhgame.animalkindom.tools.CalculateTool;
import com.zhgame.animalkindom.tools.RandomTool;

import javax.persistence.*;

@Entity
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static int INIT_VIGOUR = 100;
    private static float FEMALE_REDUCTION = 0.7f;
    private static float FEMALE_GAIN = 1.2f;

    private String name;
    private Integer gender;
    private Integer health;
    private Integer baseHealth;
    private Integer satiety;
    private Integer baseSatiety;
    private Integer vigour;
    private Integer strength;
    private Integer intelligence;
    private Integer agile;
    private Integer speed;
    private Long accountId;
    private Long typeId;
    private Integer level;
    private Integer levelStep;
    private Long sleepTime;
    private Long exploreTime;
    private Integer currentPos;
    private byte[] mapDiscovered;

    @Transient
    private String accountName;

    public Animal() {

    }

    public Animal(AnimalData animalData) {
        this.name = animalData.getName();
        this.health = animalData.getBaseHealth();
        this.baseHealth = animalData.getBaseHealth();
        this.satiety = animalData.getBaseSatiety();
        this.baseSatiety = animalData.getBaseSatiety();
        this.vigour = INIT_VIGOUR;
        this.strength = animalData.getBaseStrength();
        this.intelligence = animalData.getBaseIntelligence();
        this.agile = animalData.getBaseAgile();
        this.speed = animalData.getBaseSpeed();
        this.typeId = animalData.getId();
        this.level = 0;
        this.levelStep = animalData.getBaseLevelStep();
        this.gender = RandomTool.random.nextInt(2);
        this.currentPos = 0;
        BitArray mapDiscovered = new BitArray(GameConfig.initMapCapacity);
        mapDiscovered.set(0, true);
        this.mapDiscovered = mapDiscovered.getBits();
        femalePropertyConvert(this);
    }

    private void femalePropertyConvert(Animal animal) {
        if (animal.gender == 0) {
            this.baseHealth = convert(FEMALE_REDUCTION, this.baseHealth);
            this.health = this.baseHealth;
            this.baseSatiety = convert(FEMALE_REDUCTION, this.baseSatiety);
            this.satiety = this.baseSatiety;
            this.strength = convert(FEMALE_REDUCTION, this.strength);
            this.intelligence = convert(FEMALE_GAIN, this.intelligence);
            this.agile = convert(FEMALE_GAIN, this.agile);
        }
    }

    private Integer convert(float multi, Integer value) {
        return CalculateTool.calToInteger(multi * value);
    }

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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevelStep() {
        return levelStep;
    }

    public void setLevelStep(Integer levelStep) {
        this.levelStep = levelStep;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getSatiety() {
        return satiety;
    }

    public void setSatiety(Integer satiety) {
        this.satiety = satiety;
    }

    public Integer getVigour() {
        return vigour;
    }

    public void setVigour(Integer vigour) {
        this.vigour = vigour;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getAgile() {
        return agile;
    }

    public void setAgile(Integer agile) {
        this.agile = agile;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public static int getInitVigour() {
        return INIT_VIGOUR;
    }

    public static void setInitVigour(int initVigour) {
        INIT_VIGOUR = initVigour;
    }

    public Integer getBaseHealth() {
        return baseHealth;
    }

    public void setBaseHealth(Integer baseHealth) {
        this.baseHealth = baseHealth;
    }

    public Integer getBaseSatiety() {
        return baseSatiety;
    }

    public void setBaseSatiety(Integer baseSatiety) {
        this.baseSatiety = baseSatiety;
    }

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Long getExploreTime() {
        return exploreTime;
    }

    public void setExploreTime(Long exploreTime) {
        this.exploreTime = exploreTime;
    }

    public Integer getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(Integer currentPos) {
        this.currentPos = currentPos;
    }

    public byte[] getMapDiscovered() {
        return mapDiscovered;
    }

    public void setMapDiscovered(byte[] mapDiscovered) {
        this.mapDiscovered = mapDiscovered;
    }
}
