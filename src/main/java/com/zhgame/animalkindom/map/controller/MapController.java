package com.zhgame.animalkindom.map.controller;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.map.entity.Map;
import com.zhgame.animalkindom.map.service.MapService;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
public class MapController {

    @RequestMapping("/maps/")
    public NetMessage maps(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Account account = accountService.getLoginAccount(request);
        if(account==null){
            return new NetMessage(NetMessage.STATUS_ACCOUNT_ERROR, NetMessage.DANGER);
        }
        List<Map> maps = mapService.getMaps(animalService.getByAccount(accountService.getLoginAccount(request)));
        return new NetMessage("", NetMessage.SUCCESS, maps);
    }

    @Resource
    MapService mapService;
    @Resource
    AccountService accountService;
    @Resource
    AnimalService animalService;
}
