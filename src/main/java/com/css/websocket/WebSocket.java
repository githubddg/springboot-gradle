package com.css.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author jiashaoqing
 * @date 2019/11/25
 */
@ServerEndpoint(value = "/ws")
@Component
public class WebSocket {
    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);
    /**
     * 在线数量
     */
    private static int onlineNumber = 0;
    /**
     * 线程安全的set concurrent包中,用来存储每个客户端对应的webSocket对象
     */
    private static Set<WebSocket> webSocketSet = Collections.synchronizedSet(new CopyOnWriteArraySet<>());

    /**
     * 与客户端的连接会话
     */
    private Session session;
    public static synchronized int onlineNumber() {
        return onlineNumber;
    }

    private static synchronized void increaseOnlineNumber() {
        onlineNumber = onlineNumber + 1;
    }

    private static synchronized void decreaseOnlineNumber() {
        onlineNumber = onlineNumber - 1;
    }

    @OnOpen
    public void onOpen(Session session){
        logger.info("客户端准备建立连接");
        this.session = session;
        webSocketSet.add(this);
        logger.info("有新连接加入:{}",session.getId());
        increaseOnlineNumber();
        logger.info("当前在线人数:{}",onlineNumber());
    }

    /**
     *  心跳
     */
    @OnMessage
    public void onMessage(String message) {
        logger.info("服务端接受客户端的消息:{}",message);
    }

    @OnClose
    public void onClose() {
        decreaseOnlineNumber();
        logger.info("关闭连接成功");
        logger.info("当前在线人数" + onlineNumber());
    }

    @OnError
    public void onError(){
        logger.error("WebSocket发生错误");
    }

    /**
     * 消息群发
     * @param message
     */
    public static void sendMessage(String message) {
       webSocketSet.forEach((final WebSocket item) ->{
           try {
               item.session.getBasicRemote().sendText(message);
               logger.info("消息发送成功");
           }catch (IOException e){
               logger.error("消息发送失败:{}",item.session.getId());
           }
       });
    }

}
