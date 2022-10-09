package com.idiots.devops.config;

import com.idiots.devops.websocket.IWebsocketHandler;
import com.idiots.devops.websocket.IWebsocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description WebSocket配置
 */
@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {
    @Resource
    private IWebsocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ssh")
                .addInterceptors(new IWebsocketInterceptor())
                .setAllowedOrigins("*");
    }
}
