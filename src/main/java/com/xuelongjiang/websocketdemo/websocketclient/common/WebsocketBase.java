package com.xuelongjiang.websocketdemo.websocketclient.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

/**
 * @author xuelongjiang
 */
public class WebsocketBase {


    private Logger logger = LoggerFactory.getLogger(WebsocketBase.class);
    private WebSocketService service = null;
    private Timer timerTask = null;
    private MoniterTask moniter = null;
    private EventLoopGroup group = null;
    private Bootstrap bootstrap = null;
    private Channel channel = null;
    private String url = null;
    private ChannelFuture future = null;
    private boolean isAlive = false;
    private String pingMessage = null;


    private Set<String> subscribChannel = new HashSet<String>();//记录请求的api，断线重连


    public WebsocketBase(String url, WebSocketService service){
        this.url = url;
        this.service = service;
    }


    public WebsocketBase(String url, WebSocketService service, String pingMessage){
        this.url = url;
        this.service = service;
        this.pingMessage = pingMessage;
    }

    public  boolean futureStatus(){
        return future.isSuccess();
    }


    public void start(){

        if(url == null){
            logger.info("WebSocketClient start error url can not be null");
            return;
        }

        if(service == null){
            logger.info("WebSocketClient start error WebSocketService can not be null");
            return;
        }
        // 定时任务
        moniter = new MoniterTask(this);//把这个对象作为任务放入定时任务里
        this.connect();//连接
        timerTask = new Timer();
        timerTask.schedule(moniter, 1000, 5000);//启动后，延时1S执行，每5S执行一次run方法

    }

    public void setStatus(boolean flag) {
        this.isAlive = flag;
    }


    public void addChannel(String message){

        // 发送消息
        this.sendMessage(message);
        subscribChannel.add(message);

    }


    public void removeChannel(String message){

        if(message != null){
            return ;
        }
        this.sendMessage(message);
        subscribChannel.remove(message);

    }

    /**
     * 同时支持ws wss
     *
     * wss可能是没有直接提供端口的，需要判断 一般是默认的443端口
     */
    private void connect(){

        try {
            final URI uri = new URI(url);
            if(uri == null){
                logger.info("WebSocket connect error uri can not is null");
                return;
            }

            String scheme = uri.getScheme() == null? "ws" : uri.getScheme();
            final String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
            final int port;
            if (uri.getPort() == -1) {
                if ("ws".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    port = 443;
                } else {
                    port = -1;
                }
            } else {
                port = uri.getPort();
            }
            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                logger.info("Only WS(S) is supported.");
                return;
            }

            final boolean ssl = "wss".equalsIgnoreCase(scheme);
            //ssl
            final SslContext sslCtx;
            if (ssl) {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } else {
                sslCtx = null;
            }

            group = new NioEventLoopGroup(1);
            bootstrap = new Bootstrap();
            final WebSocketClientHandler handler = new WebSocketClientHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13,null, false,
                            new DefaultHttpHeaders(), Integer.MAX_VALUE), service, moniter);

            bootstrap.group(group).option(ChannelOption.TCP_NODELAY, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            //判读是否是ssl
                            if(sslCtx != null){
                                p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }
                            p.addLast(new HttpClientCodec(),
                                    new HttpObjectAggregator(8192), handler);//加入处理器
                        }
                    });

            future = bootstrap.connect(uri.getHost(), port);

            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {

                }
            });
            channel = future.sync().channel();
            handler.handshakeFuture().sync();
            this.setStatus(true);
        }catch (Exception e){
            logger.info("WebSocketClient start error", e);
            group.shutdownGracefully();
            this.setStatus(false);
        }
    }


    private  void sendMessage(String message){
        if(!isAlive){
            logger.info("WebSocket is nort Alive AddChannel error");
        }
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    public void sendPing(){
        logger.info("ping......进行中");
        this.sendMessage(this.pingMessage);
    }


    public void reConnect( ) {
        try {
            this.group.shutdownGracefully();
            this.group = null;
            this.connect();
            if(future.isSuccess()){
                this.setStatus(true);
                this.sendPing();
                Iterator<String> it = subscribChannel.iterator();
                while (it.hasNext()) {
                    String message = it.next();
                    this.addChannel(message);
                }
            }
        }catch (Exception e){
            logger.error("重连异常:{}",e);
        }
    }


    public void setUrl(String url ){
        this.url = url;
    }


}



















