package com.zhgame.animalkindom.animal.entity;

import com.zhgame.animalkindom.tools.BitArray;
import com.zhgame.animalkindom.tools.CalculateTool;
import com.zhgame.animalkindom.tools.DateTool;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static int INIT_VIGOUR = 100;

    private String name;
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
    private Integer typeId;
    private Integer growDay;
    private Integer growLevel;
    private Long sleepTime;
    private Long exploreTime;
    private Integer currentLand;
    private byte[] landDiscovered;
    private Long moveTime;
    private Long metempsychosisTime;
    private Integer souls;
    private Integer point;

    @Transient
    private Integer days;

    @Transient
    private String accountName;

    public Animal() {

    }

    public Animal(AnimalData animalData, Map<String, String> gameConfig) {
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
        this.growDay = animalData.getGrowDay();
        this.growLevel = animalData.getGrowLevel();
        this.currentLand = 0;
        this.metempsychosisTime = DateTool.getNowMillis();
        this.souls = 0;
        BitArray mapDiscovered = new BitArray(Integer.parseInt(gameConfig.get("maxLandNumber")));
        mapDiscovered.set(0, true);
        this.landDiscovered = mapDiscovered.getBits();
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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getGrowLevel() {
        return growLevel;
    }

    public void setGrowLevel(Integer growLevel) {
        this.growLevel = growLevel;
    }

    public Integer getGrowDay() {
        return growDay;
    }

    public void setGrowDay(Integer growDay) {
        this.growDay = growDay;
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

    public Integer getCurrentLand() {
        return currentLand;
    }

    public void setCurrentLand(Integer currentLand) {
        this.currentLand = currentLand;
    }

    public byte[] getLandDiscovered() {
        return landDiscovered;
    }

    public void setLandDiscovered(byte[] landDiscovered) {
        this.landDiscovered = landDiscovered;
    }

    public Long getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(Long moveTime) {
        this.moveTime = moveTime;
    }

    public Long getMetempsychosisTime() {
        return metempsychosisTime;
    }

    public void setMetempsychosisTime(Long metempsychosisTime) {
        this.metempsychosisTime = metempsychosisTime;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getSouls() {
        return souls;
    }

    public void setSouls(Integer souls) {
        this.souls = souls;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
