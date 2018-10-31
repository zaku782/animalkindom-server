package com.zhgame.animalkindom.map.service;

import com.zhgame.animalkindom.GameConfig;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.service.AnimalRepository;
import com.zhgame.animalkindom.map.entity.Map;
import com.zhgame.animalkindom.tools.BitArray;
import com.zhgame.animalkindom.tools.CalculateTool;
import com.zhgame.animalkindom.tools.DateTool;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class MapService {

    private static List<Map> mapData = new ArrayList();

    public List<Map> getMapData() {
        if (mapData.size() == 0) {
            mapData = mapRepository.findAll();
        }
        return mapData;
    }

    public List<Map> getMaps(Animal animal) {
        Integer currentMap = animal.getCurrentPos();
        byte[] mapDiscovered = animal.getMapDiscovered();
        BitArray bitArray = new BitArray(mapDiscovered);
        List<Integer> mapDiscoveredIds = new ArrayList<>();
        bitArray.forEach((id, discovered) -> {
            if (discovered) {
                mapDiscoveredIds.add(id);
            }
        });
        List<Map> mapData = getMapData();
        List<Map> mapDiscoveredList = mapData.stream().filter(map -> mapDiscoveredIds.contains(map.getId())).collect(toList());
        mapDiscoveredList.add(0, mapData.stream().filter(map -> map.getId().intValue() == currentMap.intValue()).findFirst().get());
        return mapDiscoveredList;
    }

    public NetMessage enter(Animal animal, int mapId) {
        //check move
        int[] round = round(animal.getCurrentPos());
        byte[] mapDiscovered = animal.getMapDiscovered();
        BitArray bitArray = new BitArray(mapDiscovered);
        if (Arrays.stream(round).anyMatch(i -> i == mapId) && bitArray.get(mapId)) {
            long last = animal.getMoveTime();
            long now = DateTool.getNowMillis();
            long between = now - last;
            if (between / 1000 < GameConfig.mapMoveInterval) {
                return new NetMessage(NetMessage.MAP_MOVE_WAIT, NetMessage.WARNING);
            }

            System.out.println(GameConfig.moveSatietyCostPercent + "     " + animal.getBaseSatiety());
            Integer needSatiety = CalculateTool.calToInteger(GameConfig.moveSatietyCostPercent * animal.getBaseSatiety());

            if (animal.getSatiety() < needSatiety) {
                return new NetMessage(NetMessage.NOT_ENOUGH_SATIETY, NetMessage.WARNING);
            }
            if (animal.getVigour() < GameConfig.moveVigourCost) {
                return new NetMessage(NetMessage.NOT_ENOUGH_VIGOUR, NetMessage.WARNING);
            }
            animal.setSatiety(animal.getSatiety() - needSatiety);
            animal.setVigour(animal.getVigour() - GameConfig.moveVigourCost);
            animal.setCurrentPos(mapId);
            animal.setMoveTime(now);
            animalRepository.save(animal);
        } else {
            return new NetMessage(NetMessage.STATUS_INVALID_OPERATION, NetMessage.DANGER);
        }
        return new NetMessage("", NetMessage.SUCCESS);
    }

    //cal map numbers round the given map number-------start------------------------
    private int loop2Count(int loop) {
        return (int) Math.pow(3 + 2 * (loop - 1), 2);
    }

    int count2Loop(int count) {
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
     * @param number given pos
     * @return numbers in pos up/right/down/left
     */
    int[] round(int number) {

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
    //cal map numbers round the given map number-------end------------------------

    @Resource
    private MapRepository mapRepository;
    @Resource
    private AnimalRepository animalRepository;
}
