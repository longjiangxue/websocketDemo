package com.xuelongjiang.websocketdemo.websocketclient.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 定时任务，周期发送ping 及判断是否掉线，如果掉线，则重连
 * @author xuelongjiang
 */
public class MoniterTask extends TimerTask {

    private Logger log = LoggerFactory.getLogger(MoniterTask.class);
    private long startTime = System.currentTimeMillis();//在hanlder处理器的channelRead0方法更新，
    private int checkTime = 5000;
    private WebsocketBase client = null;

    public void updateTime() {
        log.info("startTime is update");
        startTime = System.currentTimeMillis();
    }

    public MoniterTask(WebsocketBase client) {
        this.client = client;
        log.info("TimerTask is starting.... ");
    }

    public void run() {
        if (System.currentTimeMillis() - startTime > checkTime) {// 如果5m，没有收到服务端的任何消息则认为掉线了，需要重新连接
            client.setStatus(false);
            log.info("Moniter reconnect....... ");
            log.info("重新连接中......");
            client.reConnect();
        }
        client.sendPing();
        log.info("Moniter ping data sent.... ");
    }
}


