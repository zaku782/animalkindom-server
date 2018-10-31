package com.zhgame.animalkindom.account.service;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.tools.CookieTool;
import com.zhgame.animalkindom.tools.MD5Tool;
import com.zhgame.animalkindom.tools.NetMessage;
import com.zhgame.animalkindom.tools.RedisTool;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@Component
public class AccountService {

    public Account getById(Long id) throws Exception {
        return accountRepository.getOne(id);
    }

    public Account getByName(String name) {
        return accountRepository.getByName(name);
    }

    public void save(Account a) {
        accountRepository.save(a);
    }

    public boolean inputCheck(Account account) {

        if (account.getName() == null || account.getName().equals("")) {
            return false;
        }
        if (account.getPassword() == null || account.getPassword().equals("")) {
            return false;
        }

        return true;
    }

    public NetMessage signUpValidation(Account account) throws Exception {

        boolean valid = inputCheck(account);

        if (!valid) {
            return new NetMessage(NetMessage.STATUS_ACCOUNT_ERROR, NetMessage.DANGER);
        }

        Account accountDb = accountRepository.getByName(account.getName());

        if (accountDb != null) {
            return new NetMessage(NetMessage.STATUS_ACCOUNT_INVALID, NetMessage.DANGER);
        }

        return null;
    }

    public Optional<Account> signInValidation(Account account) throws Exception {

        boolean valid = inputCheck(account);

        if (!valid) {
            return Optional.empty();
        }

        Optional<Account> accountDb = accountRepository.getByIdentity(account.getName());

        if (!accountDb.isPresent()) {
            return Optional.empty();
        } else {
            if (!accountDb.get().getPassword().equals(MD5Tool.getEncrypted(account.getPassword() + accountDb.get().getSalt()))) {
                return Optional.empty();
            }
            return accountDb;
        }
    }

    @Transactional
    public NetMessage signIn(Account account, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Optional<Account> accountDb = signInValidation(account);
        if (!accountDb.isPresent()) {
            return new NetMessage(NetMessage.STATUS_ACCOUNT_ERROR, NetMessage.DANGER);
        }

        this.LogAccount(request, response, accountDb.get());
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS);
    }

    @Transactional
    public NetMessage signUp(Account account, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        NetMessage message = signUpValidation(account);
        if (message != null) {
            return message;
        }

        encrypt(account);
        Account accountDb = accountRepository.save(account);
        //create an animal associate with the account
        Animal animal = animalService.createAnimal(account);
        animalService.save(animal);


        this.LogAccount(request, response, accountDb);

        return new NetMessage("ok", NetMessage.SUCCESS);
    }

    public NetMessage signOut(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        this.clearAccount(request, response);
        return new NetMessage("ok", NetMessage.SUCCESS);
    }

    public Account getLoginAccount(HttpServletRequest request) throws Exception {
        Object accountId = request.getSession().getAttribute("login_account");
        if (accountId != null) {
            return getById(Long.parseLong(accountId.toString()));
        }
        return null;
    }

    /**
     * <p>
     * Description:record session and cookie
     * </p>
     *
     * @param request  *
     * @param response *
     * @param account  *
     * @throws Exception *
     */
    public void LogAccount(HttpServletRequest request, HttpServletResponse response, Account account) throws Exception {
        String tokenOri = account.getName() + new Date().getTime() + CookieTool.getIpAddr(request);
        String token = MD5Tool.getEncrypted(tokenOri);
        CookieTool.setCookies(response, "ak_token", token);
        request.getSession().setAttribute("login_account", account.getId());
        redisTool.set(token, account.getId(), 300);
    }

    private void clearAccount(HttpServletRequest request, HttpServletResponse response) {
        CookieTool.cleanCookies(response, "ak_cookie");
        request.getSession().setAttribute("login_account", null);
    }

    private void encrypt(Account account) throws NoSuchAlgorithmException {
        SecureRandom csprng = new SecureRandom();
        byte[] randomBytes = new byte[32];
        csprng.nextBytes(randomBytes);
        String salt = MD5Tool.bytes2Hex(randomBytes);
        account.setPassword(MD5Tool.getEncrypted(account.getPassword() + salt));
        account.setSalt(salt);
    }

    public RedisTool getRedisTool() {
        return redisTool;
    }

    @Resource
    private AccountRepository accountRepository;
    @Resource
    private AnimalService animalService;
    @Resource
    private RedisTool redisTool;
}
