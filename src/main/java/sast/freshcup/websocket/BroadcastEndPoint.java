package sast.freshcup.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sast.freshcup.common.enums.AuthEnum;
import sast.freshcup.common.enums.ErrorEnum;
import sast.freshcup.config.WebSocketConfig;
import sast.freshcup.entity.Account;
import sast.freshcup.exception.LocalRunTimeException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 風楪fy
 * @create: 2022-01-21 05:31
 **/
@Slf4j
@Component
@ServerEndpoint(value = "/broadcast", configurator = WebSocketConfig.class)
public class BroadcastEndPoint {

    private static Map<Key, BroadcastEndPoint> onlineUsers = new ConcurrentHashMap<>();

    //websocket的session
    private Session session;

    private Account account;


    /**
     * 连接建立时执行
     *
     * @param session
     * @param config
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws InterruptedException {
        this.session = session;
        this.account = (Account) config.getUserProperties().get("account");

        //将当前对象存储到容器中
        onlineUsers.put(new Key(account), this);
        log.info("==========================================");
        log.info("websocket");
        log.info("username:{},role:{}建立连接", account.getUsername(), account.getRole());
        log.info("==========================================");
    }

    /**
     * 接收到客户端发送的数据时执行
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        if (AuthEnum.STUDENT.getCode().equals(jsonObject.getInteger("role"))) {
            broadcast(jsonObject.getString("content"), AuthEnum.STUDENT);
        } else if (AuthEnum.ADMIN.getCode().equals(jsonObject.getInteger("role"))) {
            broadcast(jsonObject.getString("content"), AuthEnum.ADMIN);
        }
    }

    /**
     * 连接关闭时执行
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("==========================================");
        log.info("websocket");
        log.info("username:{},role:{}退出连接", account.getUsername(), account.getRole());
        log.info("==========================================");
    }

    /**
     * 抛异常时执行
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 给指定的用户群发公告
     *
     * @param message
     * @param authEnum
     */
    public void broadcast(String message, AuthEnum authEnum) {
        try {
            Set<Key> keys = onlineUsers.keySet();
            for (Key key : keys) {
                if (key.role.equals(authEnum.getCode())) {
                    BroadcastEndPoint broadcastEndPoint = onlineUsers.get(key);
                    broadcastEndPoint.session.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new LocalRunTimeException(ErrorEnum.WEBSOCKEY_ERROR);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Key {
        private String username;
        private Integer role;

        public Key(Account account) {
            this.username = account.getUsername();
            this.role = account.getRole();
        }
    }
}
