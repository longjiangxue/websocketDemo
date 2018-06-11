package com.xuelongjiang.websocketdemo;

import com.xuelongjiang.websocketdemo.websocket.WebSocketDemoHanlder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author xuelongjiang
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig  implements WebSocketConfigurer{

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new WebSocketDemoHanlder(),"/demo").setAllowedOrigins("*");
    }
}
