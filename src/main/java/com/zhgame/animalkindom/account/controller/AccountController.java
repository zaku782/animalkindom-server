package com.zhgame.animalkindom.account.controller;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class AccountController {

    @RequestMapping("/signIn/")
    public NetMessage signIn(@RequestBody Account account, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return accountService.signIn(account, request, response);
    }

    @RequestMapping("/isLogin/")
    public NetMessage isLogin(HttpServletRequest request) throws Exception {
        Account account = accountService.getLoginAccount(request);
        return account == null ? new NetMessage(NetMessage.STATUS_ACCOUNT_ERROR, NetMessage.DANGER) : new NetMessage("", NetMessage.SUCCESS);
    }

    @RequestMapping("/signUp/")
    public NetMessage signUp(@RequestBody Account account, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return accountService.signUp(account, request, response);
    }


    @RequestMapping("/signOut/")
    public NetMessage singOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return accountService.signOut(request, response);
    }

    @Resource
    AccountService accountService;
}
