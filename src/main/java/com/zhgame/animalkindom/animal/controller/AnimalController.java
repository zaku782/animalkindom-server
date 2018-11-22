package com.zhgame.animalkindom.animal.controller;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.entity.PointAdd;
import com.zhgame.animalkindom.animal.entity.SleepEnd;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.plant.entity.ExploreEnd;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
public class AnimalController {

    @RequestMapping("/animal/getInfo/")
    public NetMessage getAnimal(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
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

    @RequestMapping("/animal/getFindPlants/")
    public NetMessage getFindPlants(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animalService.getFindPlants(animal));
    }

    @RequestMapping("/animal/eatAtOnce/{plantName}/")
    public NetMessage eatAtOnce(@PathVariable("plantName") String plantName, HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animalService.eatAtOnce(animal, plantName));
    }

    @RequestMapping("/animal/eatFromBag/{itemId}/")
    public NetMessage eatFromBag(@PathVariable("itemId") Long itemId, HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animalService.eatFromBag(animal, itemId));
    }

    @RequestMapping("/animal/getBagLoad/")
    public NetMessage getBagLoad(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animalService.getBagLoad(animal));
    }

    @RequestMapping("/animal/collectPlant/{plantName}/")
    public NetMessage collectPlant(@PathVariable("plantName") String plantName, HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        return animalService.collectPlant(animal, plantName);
    }

    @RequestMapping("/animal/getBagItems/")
    public NetMessage getBagItems(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animalService.getBagItems(animal));
    }

    @RequestMapping("/animal/metempsychosis/{useSouls}")
    public NetMessage metempsychosis(@PathVariable("useSouls") Integer useSouls, HttpServletRequest request) throws Exception {
        Account account = accountService.getLoginAccount(request);
        Animal animal = animalService.getByAccount(account);
        return animalService.metempsychosis(animal, account, useSouls);
    }

    @RequestMapping("/animal/addPoint/")
    public NetMessage addPoint(@RequestBody PointAdd pointAdd, HttpServletRequest request) throws Exception {
        Account account = accountService.getLoginAccount(request);
        Animal animal = animalService.getByAccount(account);
        return animalService.addPoint(animal, pointAdd);
    }

    @RequestMapping("/animal/makeFriend/{animalId}")
    public NetMessage makeFriend(@PathVariable("animalId") String animalId, HttpServletRequest request) throws Exception {
        Account account = accountService.getLoginAccount(request);
        Animal animal = animalService.getByAccount(account);
        animalService.makeFriend(animal, animalId);
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS);
    }

    @Resource
    private AnimalService animalService;
    @Resource
    private AccountService accountService;
}
