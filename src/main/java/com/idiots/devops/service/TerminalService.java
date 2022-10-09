package com.idiots.devops.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description
 */
public interface TerminalService {
    /**
     * 初始化WebSocket连接
     *
     * @param session WebSocket会话对象
     */
    void init(WebSocketSession session);

    /**
     * 关闭WebSocket连接
     *
     * @param session WebSocket会话对象
     */
    void close(WebSocketSession session);

    /**
     * 接收到页面发过来的命令
     *
     * @param session WebSocket会话对象
     * @param message 终端命令
     */
    void receive(WebSocketSession session, String message);
}
