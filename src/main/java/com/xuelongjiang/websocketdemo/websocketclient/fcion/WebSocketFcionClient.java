package com.xuelongjiang.websocketdemo.websocketclient.fcion;

import com.xuelongjiang.websocketdemo.websocketclient.common.WebSocketService;
import com.xuelongjiang.websocketdemo.websocketclient.common.WebsocketBase;

/**
 * @author xuelongjiang
 */
public class WebSocketFcionClient extends WebsocketBase {

    public WebSocketFcionClient(String url, WebSocketService service) {
        super(url, service);
    }


    public WebSocketFcionClient(String url, WebSocketService service, String pingMessage) {
        super(url, service, pingMessage);
    }
}
