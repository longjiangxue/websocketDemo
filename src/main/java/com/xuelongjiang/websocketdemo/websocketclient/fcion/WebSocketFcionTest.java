package com.xuelongjiang.websocketdemo.websocketclient.fcion;


import com.xuelongjiang.websocketdemo.websocketclient.common.WebSocketService;

/**
 * @author xuelongjiang
 */
public class WebSocketFcionTest {


    public static void main(String[] args) {

        String fcionUri = "wss://ws.fcoin.com/api/v2/ws";
        String ping = "{\"cmd\":\"ping\",\"args\":[1532070885000]}";
        String getData = "{\"id\":\"tickers\",\"cmd\":\"sub\",\"args\":[\"all-tickers\"]}";
        String walle = "ws://118.178.123.203:8080/demo";

        String walle_register = "{\"action\":2,\"data\":{\"botWxId\":\"wxid_9uw5pzcz64tu22\",\"content\":\"\",\"msg\":\"\",\"name\":\"A云图小芸\",\"picLinkList\":[],\"receiver\":\"\",\"receiverList\":[],\"type\":0},\"seq\":1528682340965}";

        WebSocketService service = new BuissnesWebSocketServiceImpl();
        WebSocketFcionClient client = new WebSocketFcionClient(fcionUri,service,ping);

        client.start();
        client.addChannel(getData);


    }

}
