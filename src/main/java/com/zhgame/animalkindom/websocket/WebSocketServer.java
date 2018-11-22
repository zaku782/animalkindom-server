package com.zhgame.animalkindom.websocket;

import com.zhgame.animalkindom.redis.service.RedisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/websocket/{animalId}", configurator = WsEndpointConfigure.class)
@Component
public class WebSocketServer {

    static Log log = LogFactory.getLog(WebSocketServer.class);
    public static Map<String, Session> sessions = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("animalId") String animalId) {
        sessions.put(animalId, session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("animalId") String animalId) {
        //从set中删除
        sessions.remove(animalId);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //item.sendMessage(message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public static void sendInfo(String message, String animalId) throws IOException {
        Session session = sessions.get(animalId);
        if (session != null) {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message);
            } else {
                sessions.remove(animalId);
            }
        }
    }

    @Resource
    private RedisService redisService;
}
