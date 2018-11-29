package com.zhgame.animalkindom.account.service;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.land.service.LandService;
import com.zhgame.animalkindom.redis.service.RedisService;
import com.zhgame.animalkindom.tools.CookieTool;
import com.zhgame.animalkindom.tools.MD5Tool;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

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

    private boolean inputCheck(Account account) {
        return !(account.getName() == null || account.getName().equals("")) && !(account.getPassword() == null || account.getPassword().equals(""));
    }

    private NetMessage signUpValidation(Account account) throws Exception {

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

    private Optional<Account> signInValidation(Account account) throws Exception {

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
        animalService.metempsychosis(accountDb);
        this.LogAccount(request, response, accountDb);
        return new NetMessage("ok", NetMessage.SUCCESS);
    }

    public NetMessage signOut(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        this.clearAccount(request, response);
        return new NetMessage("ok", NetMessage.SUCCESS);
    }

    public Account getLoginAccount(HttpServletRequest request) throws Exception {
        return (Account) request.getSession().getAttribute("login_account");
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
    private void LogAccount(HttpServletRequest request, HttpServletResponse response, Account account) throws Exception {
        String token = UUID.randomUUID().toString();
        CookieTool.setCookies(response, "ak_token", token);
        Account accountSession = new Account(account.getId(), account.getName());
        request.getSession().setAttribute("login_account", accountSession);
        Animal animal = animalService.getByAccount(accountSession);
        redisService.moveToLand(animal.getCurrentLand(), animal);
        redisService.addToken(token, accountSession);
    }

    private void clearAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<String> cookie = CookieTool.getCookies(request, "ak_token");
        if (cookie.isPresent()) {
            redisService.removeToken(cookie.get());
            CookieTool.cleanCookies(response, "ak_token");
        }
        Animal animal = animalService.getByAccount(getLoginAccount(request));
        redisService.leaveLand(animal.getCurrentLand(), animal);
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

    @Resource
    private AccountRepository accountRepository;
    @Resource
    private AnimalService animalService;
    @Resource
    private RedisService redisService;
    @Resource
    private LandService landService;
}
