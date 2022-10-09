package com.idiots.devops.entity;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description WebSocket传输过来消息的类型
 */
public enum MsgType {
    /**
     * 连接指令
     */
    connect(),
    /**
     * 终端命令
     */
    command()
}
