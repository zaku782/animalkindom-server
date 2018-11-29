package com.zhgame.animalkindom.land.service;

import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.service.AnimalRepository;
import com.zhgame.animalkindom.land.entity.Land;
import com.zhgame.animalkindom.land.entity.MoveEnd;
import com.zhgame.animalkindom.redis.service.RedisService;
import com.zhgame.animalkindom.tools.BitArray;
import com.zhgame.animalkindom.tools.DateTool;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class LandService {

    public List<Land> getLands(Animal animal) {
        Integer currentLand = animal.getCurrentLand();
        byte[] landDiscovered = animal.getLandDiscovered();
        BitArray bitArray = new BitArray(landDiscovered);
        List<Integer> landDiscoveredIds = new ArrayList<>();
        bitArray.forEach((id, discovered) -> {
            if (discovered) {
                landDiscoveredIds.add(id);
            }
        });
        List<Land> landData = redisService.getLandData();
        //过滤出探索到的大陆
        List<Land> landDiscoveredList = landData.stream().filter(
                land -> landDiscoveredIds.contains(land.getId())).collect(toList()
        );
        //将当前所在大陆放置在列表第一个
        landDiscoveredList.add(0, landData.stream().filter(
                land -> land.getId().intValue() == currentLand.intValue()
        ).findFirst().get());
        //显示更新后的产出率
        landDiscoveredList.forEach(land -> land.setPlantRate(getPlantRate(land)));
        return landDiscoveredList;
    }

    /**
     * 植物真实产出率：原产出率-过多采集导致的产出率减益,最小为0
     *
     * @param land 大陆
     * @return 植物真实产出率
     */
    public Integer getPlantRate(Land land) {
        return Math.max(land.getPlantRate() -
                new BigDecimal(redisService.getLandPlantCost(land.getId()))
                        .divide(new BigDecimal(gameConfig.get("plantYieldDescPerCost")), BigDecimal.ROUND_DOWN).intValue(), 0);
    }

    public NetMessage enter(Animal animal, int landId) {
        //check move
        Integer currentLand = animal.getCurrentLand();
        int[] round = round(animal.getCurrentLand());
        byte[] landDiscovered = animal.getLandDiscovered();
        BitArray bitArray = new BitArray(landDiscovered);
        if (Arrays.stream(round).anyMatch(i -> i == landId) && bitArray.get(landId)) {

            long now = DateTool.getNowMillis();
            Long last = animal.getMoveTime();

            if (last != null) {
                long between = now - last;
                if (new BigDecimal(between).divide(new BigDecimal(1000), BigDecimal.ROUND_HALF_UP).intValue() < Integer.parseInt(gameConfig.get("landMoveInterval"))) {
                    return new NetMessage(NetMessage.LAND_MOVE_WAIT, NetMessage.WARNING);
                }
            }

            String satietyCost = gameConfig.get("moveSatietyCost");
            String vigourCost = gameConfig.get("moveVigourCost");

            Integer needSatiety = new BigDecimal(satietyCost).multiply(new BigDecimal(animal.getBaseSatiety())).intValue();

            if (animal.getSatiety() < needSatiety) {
                return new NetMessage(NetMessage.NOT_ENOUGH_SATIETY, NetMessage.WARNING);
            }
            if (animal.getVigour() < Integer.parseInt(vigourCost)) {
                return new NetMessage(NetMessage.NOT_ENOUGH_VIGOUR, NetMessage.WARNING);
            }
            animal.setSatiety(animal.getSatiety() - needSatiety);
            animal.setVigour(animal.getVigour() - Integer.parseInt(vigourCost));
            animal.setCurrentLand(landId);
            animal.setMoveTime(now);
            animalRepository.save(animal);

            move(currentLand, landId, animal);

            return new NetMessage("", NetMessage.SUCCESS, new MoveEnd(vigourCost, needSatiety.toString()));
        } else {
            return new NetMessage(NetMessage.STATUS_INVALID_OPERATION, NetMessage.DANGER);
        }
    }

    private void move(Integer leave, Integer moveTo, Animal animal) {
        redisService.leaveLand(leave, animal);
        redisService.moveToLand(moveTo, animal);
    }

    private int loop2Count(int loop) {
        return (int) Math.pow(3 + 2 * (loop - 1), 2);
    }

    private int count2Loop(int count) {
        return new BigDecimal(Math.sqrt(count))
                .subtract(new BigDecimal(3))
                .divide(new BigDecimal(2), BigDecimal.ROUND_UP)
                .intValue() + 1;
    }

    private Integer loopName(int number) {
        return Stream.iterate(1, n -> n + 1).map(this::loop2Count).filter(n -> n > number).findFirst().map(this::count2Loop).orElse(-1);
    }

    private List<Integer> loopCorner(int loop) {
        if (loop == 0) {
            return Arrays.asList(0, 0, 0, 0);
        }
        return Stream.iterate(loop2Count(loop) - 1, n -> n - loop * 2).limit(4).collect(toList());
    }

    private String moveDirection(int number) {
        List<Integer> corner = loopCorner(loopName(number));

        if (corner.contains(number)) {
            if (number == corner.get(0)) {
                return "leftUp";
            } else if (number == corner.get(1)) {
                return "leftDown";
            } else if (number == corner.get(2)) {
                return "rightDown";
            } else {
                return "rightUp";
            }
        } else {
            if (number > corner.get(1)) {
                return "up";
            } else if (number < corner.get(1) && number > corner.get(2)) {
                return "left";
            } else if (number < corner.get(2) && number > corner.get(3)) {
                return "down";
            } else {
                return "right";
            }
        }
    }

    /**
     * @param number 地图编号
     * @return 地图上下左右的地图编号
     */
    private int[] round(int number) {

        if (number == 0) {
            return new int[]{1, 3, 5, 7};
        }

        String direction = moveDirection(number);
        //current loop
        int loopName = loopName(number);
        //current loop corner
        List<Integer> loopCorner = loopCorner(loopName);
        //outer loop corner
        List<Integer> outLoopCorner = loopCorner(loopName + 1);
        //inner loop corner
        List<Integer> inLoopCorner = loopCorner(loopName - 1);

        //bias from none-corner to corner
        int bias = 0;

        switch (direction) {
            case "leftUp":
                return new int[]{number + 1, inLoopCorner.get(0) + 1, number - 1, outLoopCorner.get(3) - 1};
            case "rightUp":
                return new int[]{outLoopCorner.get(3) - 1, outLoopCorner.get(3) + 1, number + 1, number - 1};
            case "rightDown":
                return new int[]{number - 1, outLoopCorner.get(2) - 1, outLoopCorner.get(2) + 1, number + 1};
            case "leftDown":
                return new int[]{number + 1, number - 1, outLoopCorner.get(1) - 1, outLoopCorner.get(1) + 1};
            case "up":
                bias = loopCorner.get(0) - number;
                return new int[]{number + 1, inLoopCorner.get(0) - (bias - 1), number - 1, outLoopCorner.get(0) - (bias + 1)};
            case "down":
                bias = loopCorner.get(2) - number;
                return new int[]{number - 1, outLoopCorner.get(2) - (bias + 1), number + 1, inLoopCorner.get(2) - (bias - 1)};
            case "left":
                bias = loopCorner.get(1) - number;
                return new int[]{inLoopCorner.get(1) - (bias - 1), number - 1, outLoopCorner.get(1) - (bias + 1), number + 1};
            case "right":
                bias = loopCorner.get(3) - number;
                return new int[]{outLoopCorner.get(3) - (bias + 1), number + 1, inLoopCorner.get(3) - (bias - 1), number - 1};
            default:
                return new int[]{};
        }
    }

    @Resource
    private LandRepository landRepository;
    @Resource
    private AnimalRepository animalRepository;
    @Resource
    private Map<String, String> gameConfig;
    @Resource
    private RedisService redisService;
}
