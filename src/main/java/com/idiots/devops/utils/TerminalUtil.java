package com.idiots.devops.utils;

import com.idiots.devops.entity.ConnectInfo;
import com.idiots.devops.entity.SshEntry;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description
 */
@Slf4j
public class TerminalUtil {
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(2, 10,
                           1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(5, true),
                           Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    /**
     * 连接终端
     *
     * @param info 连接主机用户信息
     */
    public static void connect(SshEntry entry, ConnectInfo info) {
        EXECUTOR.execute(() -> {
            Session session;
            try {
                session = entry.getjSch().getSession(info.getUsername(), info.getHost(), info.getPort());
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(info.getPassword());
                // 连接, 并设置连接超时时间
                session.connect(info.getTimeout() * 1000);

                // 打开shell通道
                Channel channel = session.openChannel("shell");
                channel.connect(info.getTimeout() * 1000);

                entry.setChannel(channel);
            } catch (Exception e) {
                String errorMsg = String.format("Could not connect to '%s' (port %s): Connection failed.", info.getHost(), info.getPort());
                sendMessage(entry.getSession(), errorMsg.getBytes());
                return;
            }

            sendCommand(entry.getChannel(), "\r");

            InputStream is = null;
            try {
                is = entry.getChannel().getInputStream();

                byte[] buffer = new byte[1024];
                int i = 0;
                while ((i = is.read(buffer)) != -1) {
                    sendMessage(entry.getSession(), Arrays.copyOfRange(buffer, 0, i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                entry.getChannel().disconnect();
                session.disconnect();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 将命令发送给终端
     *
     * @param channel shell通道
     * @param command 命令
     */
    public static void sendCommand(Channel channel, String command) {
        if (channel == null) {
            return;
        }
        try {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            log.error("发送[ {} ]指令失败", command);
        }
    }

    /**
     * 发送消息给页面
     *
     * @param session WebSocket会话对象
     * @param message 要发送的信息
     */
    public static void sendMessage(WebSocketSession session, byte[] message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error("发送消息失败");
        }
    }
}
