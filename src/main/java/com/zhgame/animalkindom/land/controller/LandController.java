package com.zhgame.animalkindom.land.controller;

import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.land.entity.Land;
import com.zhgame.animalkindom.land.service.LandService;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
public class LandController {

    @RequestMapping("/land/lands/")
    public NetMessage lands(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Land> lands = landService.getLands(animalService.getByAccount(accountService.getLoginAccount(request)));
        return new NetMessage("", NetMessage.SUCCESS, lands);
    }

    @RequestMapping("/land/enter/{landId}")
    public NetMessage enter(@PathVariable("landId") int landId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return landService.enter(animalService.getByAccount(accountService.getLoginAccount(request)), landId);
    }

    @Resource
    private LandService landService;
    @Resource
    private AccountService accountService;
    @Resource
    private AnimalService animalService;
}
