package com.idiots.devops.entity;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.ToString;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description
 */
@Data
@ToString
public class PageData {
    private MsgType type;

    /**
     * 消息。
     * <br>
     * 如果MsgType == connect, 这里的message类型就是{@link ConnectInfo}.
     * <br>
     * 如果MsgType == command, 这里的message类型就是{@link String}.
     */
    private Object message;

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getCommand() {
        if (MsgType.command.equals(this.type)) {
            return message.toString();
        }

        return null;
    }

    public ConnectInfo getConnectInfo() {
        if (MsgType.connect.equals(this.type)) {
            JSONObject json = (JSONObject) JSONUtil.parse(this.message.toString());
            return JSONUtil.toBean(json, ConnectInfo.class);
        }

        return null;
    }
}
