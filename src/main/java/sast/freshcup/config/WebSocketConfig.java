package sast.freshcup.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import sast.freshcup.entity.Account;
import sast.freshcup.mapper.AccountMapper;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author: 風楪fy
 * @create: 2022-01-21 05:35
 **/
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    private static AccountMapper accountMapper;

    /**
     * 非单例，只能static注入
     * @param accountMapper
     */
    @Autowired
    public void setAccountMapper(AccountMapper accountMapper) {
        WebSocketConfig.accountMapper = accountMapper;
    }

    /**
     * ServerEndpointExporter
     * 自动注册使用了@ServerEndPoint注解的Bean
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 自定义握手参数
     * @param sec
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        String username = request.getQueryString();
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Account account = accountMapper.selectOne(queryWrapper);
        sec.getUserProperties().put("account", account);
    }
}
