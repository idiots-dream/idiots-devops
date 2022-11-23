package com.idiots.devops.websocket;

import com.idiots.devops.lang.Constant;
import com.idiots.devops.service.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.annotation.Resource;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description websocket处理器
 */
@Slf4j
@Component
public class IWebsocketHandler implements WebSocketHandler {
    @Resource
    private TerminalService terminalService;

    /**
     * 用户连接之前的回调函数
     *
     * @param session WebSocket会话对象
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("用户[ {} ]成功连接!", session.getAttributes().get(Constant.SESSION_KEY));
        // 初始化连接信息
        terminalService.init(session);
    }

    /**
     * 收到WebSocket消息
     *
     * @param session WebSocket会话对象
     * @param message 接收到的消息
     */
    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) {
        if (message instanceof TextMessage) {
            log.info("用户[ {} ]发送命令: {}", session.getAttributes().get(Constant.SESSION_KEY), message);

            // 处理接收到的终端命令
            terminalService.receive(session, ((TextMessage) message).getPayload());
        }
    }

    /**
     * 出现错误时的回调
     *
     * @param session   WebSocket会话对象
     * @param exception 错误信息
     */
    @Override
    public void handleTransportError(WebSocketSession session, @NotNull Throwable exception) {
        log.error("用户[ {} ]出现错误", session.getAttributes().get(Constant.SESSION_KEY), exception);
    }

    /**
     * 断开连接后的回调
     *
     * @param session     WebSocket会话对象
     * @param closeStatus 关闭状态对象
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus closeStatus) {
        log.info("用户[ {} ]已断开连接", session.getAttributes().get(Constant.SESSION_KEY));

        // 关闭连接
        terminalService.close(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
