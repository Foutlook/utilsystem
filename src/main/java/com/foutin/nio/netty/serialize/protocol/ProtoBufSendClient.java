package com.foutin.nio.netty.serialize.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author f2485
 * @Description
 * @date 2024/12/23 10:46
 */
public class ProtoBufSendClient {
    static String content = "疯狂创客圈：高性能学习社群!";

    private int serverPort;
    private String serverIp;
    Bootstrap b = new Bootstrap();

    public ProtoBufSendClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    public void runClient() {
        // 创建反应器线程组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        try {
            // 1 设置反应器 轮询组
            b.group(workerLoopGroup);
            // 2 设置 nio 类型的通道
            b.channel(NioSocketChannel.class);
            // 3 设置监听端口
            b.remoteAddress(serverIp, serverPort);
            // 4 设置通道的参数
            b.option(ChannelOption.ALLOCATOR,
                    PooledByteBufAllocator.DEFAULT);
            // 5 装配通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                // 初始化客户端通道
                protected void initChannel(SocketChannel ch) {
                    // 客户端流水线添加 2 个 Handler 业务处理器
                    ch.pipeline().addLast(
                            new ProtobufVarint32LengthFieldPrepender());
                    ch.pipeline().addLast(new ProtobufEncoder());
                }
            });
            ChannelFuture f = b.connect();
            //...
            // 阻塞,直到连接完成
            f.sync();
            Channel channel = f.channel();
            // 发送 Protobuf 对象
            for (int i = 0; i < 1000; i++) {
                MsgProtos.Msg user = build(i, i + "->" + content);
                channel.writeAndFlush(user);
                System.out.println("发送报文数：" + i);
            }
            channel.flush();
            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭 EventLoopGroup，
            // 释放掉所有资源，包括创建的线程
            workerLoopGroup.shutdownGracefully();
        }
    }

    // 构建 ProtoBuf 对象
    public MsgProtos.Msg build(int id, String content) {
        MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
        builder.setId(id);
        builder.setContent(content);
        return builder.build();
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 18899;
        String ip = "127.0.0.1";
        new ProtoBufSendClient(ip, port).runClient();
    }
}
