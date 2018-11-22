package com.zhgame.animalkindom.event.controller;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.event.service.EventService;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class EventController {
    @RequestMapping("/event/friendEvent/")
    public NetMessage friendEvent(HttpServletRequest request) throws Exception {
        Account account = accountService.getLoginAccount(request);
        Animal animal = animalService.getByAccount(account);
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, eventService.friendEvents(animal.getId()));
    }

    @Resource
    private AnimalService animalService;
    @Resource
    private AccountService accountService;
    @Resource
    private EventService eventService;
}
