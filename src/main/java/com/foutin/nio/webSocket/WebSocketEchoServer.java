package com.foutin.nio.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author f2485
 * @Description
 * @date 2025/1/9 11:13
 */
@Slf4j
public class WebSocketEchoServer {


    static class EchoInitializer extends
            ChannelInitializer<SocketChannel> {
        @Override
        public void initChannel(SocketChannel ch) {
            ChannelPipeline pipeline = ch.pipeline();
            // HTTP 请求解码器
            pipeline.addLast(new HttpRequestDecoder());
            // HTTP 响应编码器
            pipeline.addLast(new HttpResponseEncoder());
            // HttpObjectAggregator 将 HTTP 消息的多个部分合成一条完整的 HTTP 消息
            pipeline.addLast(new HttpObjectAggregator(65535));
            // WebSocket 协议处理器，配置 WebSocket 的监听 URI、协议包长度限制
            pipeline.addLast(
                    new WebSocketServerProtocolHandler("/ws", "echo",
                            true, 10 * 1024));
            // 增加网页的处理逻辑
            pipeline.addLast(new WebPageHandler());
            // TextWebSocketFrameHandler 是自定义 WebSocket 业务处理器，
            pipeline.addLast(new TextWebSocketFrameHandler());
        }
    }


    /**
     * 启动
     */
    public static void start(String ip) throws Exception {
        // 创建连接监听 reactor 轮询组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 创建连接处理 reactor 轮询组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 服务端启动引导实例
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new EchoInitializer());
            // 监听端口，返回同步通道
            Channel ch = b.bind(18899).sync().channel();
            log.info("WebSocket 服务已经启动 http://{}:{}/", ip, 18899);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        // 前端在resource下 index.html
        try {
            start("127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
