package com.idiots.devops.websocket;

import cn.hutool.core.util.RandomUtil;
import com.idiots.devops.lang.Constant;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description
 */
@Slf4j
public class IWebsocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest req, @NotNull ServerHttpResponse resp, @NotNull WebSocketHandler handler, @NotNull Map<String, Object> attributes) {
        if (req instanceof ServletServerHttpRequest) {
            attributes.put(Constant.SESSION_KEY, RandomUtil.randomString(16));
            return true;
        }

        return false;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest req, @NotNull ServerHttpResponse resp, @NotNull WebSocketHandler handler, Exception ex) {
    }
}
