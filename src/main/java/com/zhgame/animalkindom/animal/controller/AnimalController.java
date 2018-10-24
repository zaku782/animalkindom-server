package com.zhgame.animalkindom.animal.controller;

import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.tools.BitArray;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
public class AnimalController {

    @RequestMapping("/animal/getInfo/")
    public NetMessage getAnimal(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        byte[] map = animal.getMapDiscovered();
        BitArray bitArray = new BitArray(map);
        //bitArray.show();
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, animal);
    }

    @RequestMapping("/animal/sleep/")
    public NetMessage sleep(HttpServletRequest request) throws Exception {
        Animal animal = animalService.getByAccount(accountService.getLoginAccount(request));
        animalService.sleep(animal);
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS);
    }

    @Resource
    AnimalService animalService;
    @Resource
    AccountService accountService;
}
