package com.idiots.devops.entity;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description
 */
public class SshEntry {
    /**
     * 用户的WebSocket会话信息
     */
    private WebSocketSession session;

    /**
     * JSch对象信息
     */
    private JSch jSch;

    /**
     * shell通道
     */
    private Channel channel;

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public JSch getjSch() {
        return jSch;
    }

    public void setjSch(JSch jSch) {
        this.jSch = jSch;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
