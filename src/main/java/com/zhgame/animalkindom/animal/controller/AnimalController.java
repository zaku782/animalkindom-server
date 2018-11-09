package com.zhgame.animalkindom.animal.controller;

import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.entity.SleepEnd;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.plant.entity.ExploreEnd;
import com.zhgame.animalkindom.tools.BitArray;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
public class AnimalController {

    @RequestMapping("/animal/getInfo/")
    public NetMessage getAnimal(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        byte[] map = animal.getLandDiscovered();
        BitArray bitArray = new BitArray(map);
        //bitArray.show();
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animal);
    }

    @RequestMapping("/animal/sleep/")
    public NetMessage sleep(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        SleepEnd sleepEnd = animalService.sleep(animal);
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, sleepEnd);
    }

    @RequestMapping("/animal/explore/")
    public NetMessage explore(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        ExploreEnd exploreEnd = animalService.explore(animal);
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, exploreEnd);
    }

    @RequestMapping("/animal/isExploring/")
    public NetMessage isExploring(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        boolean isExploring = animal.getExploreTime() != null;
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, isExploring);
    }

    @RequestMapping("/animal/getFinds/")
    public NetMessage getFinds(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animalService.getFinds(animal));
    }

    @RequestMapping("/animal/eatAtOnce/{plantName}/")
    public NetMessage eatAtOnce(@PathVariable("plantName") String plantName, HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animalService.eatAtOnce(animal, plantName));
    }

    @Resource
    AnimalService animalService;
    @Resource
    AccountService accountService;
}
