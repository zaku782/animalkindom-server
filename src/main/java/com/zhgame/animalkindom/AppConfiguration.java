package com.zhgame.animalkindom;

import com.zhgame.animalkindom.account.entity.Account;
import com.zhgame.animalkindom.account.service.AccountService;
import com.zhgame.animalkindom.config.service.GameConfigService;
import com.zhgame.animalkindom.tools.CookieTool;
import com.zhgame.animalkindom.tools.JsonTool;
import com.zhgame.animalkindom.tools.NetMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
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

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        //.allowedOrigins("http://www.animalkindom.win")
                        .allowedOrigins("http://localhost:8888")
                        .allowCredentials(true);
            }
        };
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
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:8888");
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
                            Account accountRedis = (Account) accountService.getRedisTool().get(token);
                            if (accountRedis != null) {
                                request.getSession().setAttribute("login_account", accountRedis);
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

    @Bean
    public Map<String, String> gameConfig() {
        return gameConfigService.getGameConfig();
    }

    @Resource
    AccountService accountService;
    @Resource
    GameConfigService gameConfigService;
}
