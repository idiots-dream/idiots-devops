package com.idiots.devops.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.idiots.devops.entity.PageData;
import com.idiots.devops.entity.SshEntry;
import com.idiots.devops.lang.Constant;
import com.idiots.devops.service.TerminalService;
import com.idiots.devops.utils.TerminalUtil;
import com.jcraft.jsch.JSch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description
 */
@Slf4j
@Service
public class TerminalServiceImpl implements TerminalService {
    private static final Map<String, SshEntry> CONNECTS = new ConcurrentHashMap<>();
    @Override
    public void init(WebSocketSession session) {
        JSch jSch = new JSch();

        SshEntry entry = new SshEntry() {{
            setSession(session);
            setjSch(jSch);
        }};

        CONNECTS.put(session.getAttributes().get(Constant.SESSION_KEY).toString(), entry);
    }

    @Override
    public void close(WebSocketSession session) {
        String uuid = session.getAttributes().get(Constant.SESSION_KEY).toString();
        SshEntry entry = CONNECTS.get(uuid);
        if (entry != null && entry.getChannel() != null) {
            entry.getChannel().disconnect();
        }

        CONNECTS.remove(uuid);
    }

    @Override
    public void receive(WebSocketSession session, String message) {
        PageData data = JSONUtil.toBean((JSONObject) JSONUtil.parse(message), PageData.class);
        log.info("消息：{}", data);
        String uuid = session.getAttributes().get(Constant.SESSION_KEY).toString();
        SshEntry entry = CONNECTS.get(uuid);
        if (entry == null) {
            log.error("未找到用户[ {} ]的连接信息", uuid);
            return;
        }

        switch (data.getType()) {
            case connect:
                TerminalUtil.connect(entry, data.getConnectInfo());
                break;
            case command:
                TerminalUtil.sendCommand(entry.getChannel(), data.getCommand());
                break;
            default:
                log.warn("不支持的消息类型");
                close(session);
        }
    }
}
