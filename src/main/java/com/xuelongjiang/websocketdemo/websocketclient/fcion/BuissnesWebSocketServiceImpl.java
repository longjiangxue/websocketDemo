package com.xuelongjiang.websocketdemo.websocketclient.fcion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.xuelongjiang.websocketdemo.websocketclient.common.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订阅信息处理类需要实现WebSocketService接口
 *
 * 根据返回消息处理具体的业务
 * @author okcoin
 *
 */
public class BuissnesWebSocketServiceImpl implements WebSocketService{

    private Logger log = LoggerFactory.getLogger(BuissnesWebSocketServiceImpl.class);


    private String get_rate_usedToCNY = "https://www.fcoin.com/api/common/get_rate?from=USD&to=CNY";



    public BuissnesWebSocketServiceImpl() {
    }


    @Override
    public void onReceive(String msg){

        log.info("WebSocket Client 接收到消息: " + msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String topic = jsonObject.getString("topic");
        if(topic != null &&topic.equals("all-tickers")){
            JSONArray jsonArray = jsonObject.getJSONArray("tickers");
            for(int i =0; i< jsonArray.size(); i++){

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if(jsonObject1.getString("symbol").equals("btcusdt")){
                    Double usdPrice =jsonObject1.getJSONArray("ticker").getDouble(0);
                    log.info("fcion当前价格:{}",usdPrice.toString());
                }

            }
        }


    }
}

