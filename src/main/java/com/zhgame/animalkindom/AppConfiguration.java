package com.zhgame.animalkindom;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.animal.entity.Animal;
import com.zhgame.animalkindom.animal.service.AnimalService;
import com.zhgame.animalkindom.event.service.EventService;
import com.zhgame.animalkindom.redis.service.RedisService;
import com.zhgame.animalkindom.tools.CookieTool;
import com.zhgame.animalkindom.tools.JsonTool;
import com.zhgame.animalkindom.tools.NetMessage;
import com.zhgame.animalkindom.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Configuration
public class AppConfiguration extends WebMvcConfigurerAdapter {

    @Value("${system.env}")
    private String env;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws Exception {
                request.getContextPath();
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Access-Control-Allow-Headers",
                        "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                //response.setHeader("Access-Control-Allow-Origin", "http://www.animalkindom.win");
                response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
                if (request.getRequestURL().indexOf("/signIn/") == -1
                        && request.getRequestURL().indexOf("/signOut/") == -1
                        && request.getRequestURL().indexOf("/signUp/") == -1) {

                    boolean isLogin = true;
                    Account account = accountService.getLoginAccount(request);

                    if (account == null) {
                        Optional<String> cookie = CookieTool.getCookies(request, "ak_token");
                        if (!cookie.isPresent()) {
                            isLogin = false;
                        } else {
                            String token = cookie.get();
                            Account accountRedis = redisService.getAccountByToken(token);
                            if (accountRedis != null) {
                                request.getSession().setAttribute("login_account", accountRedis);
                                //从cookie登陆成功,获取未读事件
                                Animal animal = animalService.getByAccount(accountRedis);
                                if (eventService.hasUnReadEvent(animal.getId())) {
                                    WebSocketServer.sendInfo(NetMessage.UNREAD_MSG, animal.getId().toString());
                                }
                            } else {
                                isLogin = false;
                            }
                        }
                    }

                    if (!isLogin) {
                        if (!request.getRequestURI().equals("/")) {
                            response.getWriter().write(JsonTool
                                    .toString(new NetMessage(NetMessage.STATUS_LOGIN_STATUS_ERROR, NetMessage.DANGER)));
                            return false;
                        }
                    }
                }
                return true;
            }
        }).addPathPatterns("/**");
    }

    @Resource
    AccountService accountService;
    @Resource
    AnimalService animalService;
    @Resource
    RedisService redisService;
    @Resource
    EventService eventService;
}
