package com.huangshihe.home.socket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by root on 5/16/16.
 */
@ServerEndpoint(value = "/ws/chat/{nickName}")
public class MainSocket {
    /**
     * 连接对象集合
     */
    private static final Set<MainSocket> connections = new CopyOnWriteArraySet<MainSocket>();

    private String nickName;

    /**
     * WebSocket Session
     */
    private Session session;

    public MainSocket() {
    }

    /**
     * 打开连接
     *
     * @param session
     * @param nickName
     */
    @OnOpen
    public void onOpen(Session session,
                       @PathParam(value = "nickName") String nickName) {

        this.session = session;
        this.nickName = nickName;

        connections.add(this);
        String message = String.format("System> %s %s", this.nickName,
                " has joined.");
        MainSocket.broadCast(message);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        connections.remove(this);
        String message = String.format("System> %s, %s", this.nickName,
                " has disconnection.");
        MainSocket.broadCast(message);
    }

    /**
     * 接收信息
     *
     * @param message
     * @param nickName
     */
    @OnMessage
    public void onMessage(String message,
                          @PathParam(value = "nickName") String nickName) {
        MainSocket.broadCast(nickName + ">" + message);
    }

    /**
     * 错误信息响应
     *
     * @param throwable
     */
    @OnError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    /**
     * 发送或广播信息
     *
     * @param message
     */
    private static void broadCast(String message) {
        for (MainSocket socket : connections) {
            try {
                synchronized (socket) {
                    socket.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                connections.remove(socket);
                try {
                    socket.session.close();
                } catch (IOException e1) {
                }
                MainSocket.broadCast(String.format("System> %s %s", socket.nickName,
                        " has bean disconnection."));
            }
        }
    }
}
