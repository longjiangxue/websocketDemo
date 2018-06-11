package com.xuelongjiang.websocketdemo.schedule;

import com.xuelongjiang.websocketdemo.websocket.WebSocketDemoHanlder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * 定时任务
 * @author xuelongjiang
 */
@Service
public class ScheduleTask {

    private Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Scheduled(cron = "* * 0/1 * * ?")
    public void sendMessage(){

        String message = "你好";

        Map<String,WebSocketSession> map = WebSocketDemoHanlder.getUserIdSessionMap();

        WebSocketSession session = map.get("xuelongjiang");//这里用户ID的获取可以根据具体业务，这里为了更简单的演示。
        if(session != null){
            try {
                session.sendMessage(new TextMessage(message));
            }catch (Exception e){
                logger.error("定时任务异常:{}",e);
            }
        }



    }

}
