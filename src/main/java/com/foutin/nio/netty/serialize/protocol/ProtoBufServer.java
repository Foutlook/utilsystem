package com.foutin.nio.netty.serialize.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * @author f2485
 * @Description
 * @date 2024/12/23 10:32
 */
public class ProtoBufServer {

    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();

    public ProtoBufServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        // 创建反应器线程组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        try {
            // 1 设置反应器轮询组
            b.group(bossLoopGroup, workerLoopGroup);
            // 2 设置 nio 类型的通道
            b.channel(NioServerSocketChannel.class);
            // 3 设置监听端口
            b.localAddress(serverPort);
            // 4 设置通道的参数
            b.option(ChannelOption.SO_KEEPALIVE, true);
            // 5 装配子通道流水线
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                // 有连接到达时会创建一个通道
                protected void initChannel(SocketChannel ch) {
                    // 流水线管理子通道中的 Handler 业务处理器
                    // 向子通道流水线添加 3 个 Handler 业务处理器
                    ch.pipeline().addLast(
                            new ProtobufVarint32FrameDecoder());
                    ch.pipeline().addLast(
                            new ProtobufDecoder(MsgProtos.Msg.getDefaultInstance()));
                    ch.pipeline().addLast(new ProtobufBussinessDecoder());
                }
            });
            // 6 开始绑定服务器
            // 通过调用 sync 同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = b.bind().sync();
            System.out.println(" 服务器启动成功，监听端口: " +
                    channelFuture.channel().localAddress());
            // 7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等待通道关闭的异步任务结束
            ChannelFuture closeFuture =
                    channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8 优雅关闭 EventLoopGroup
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

    // 服务器端的 Protobuf 业务处理器
    static class ProtobufBussinessDecoder
            extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(
                ChannelHandlerContext ctx, Object msg) {
            MsgProtos.Msg protoMsg = (MsgProtos.Msg) msg;
            // 经过流水线的各个解码器，到此取得了 POJO 实例
            System.out.println("收到一个 Protobuf POJO =》");
            System.out.println("protoMsg.getId():=" + protoMsg.getId());
            System.out.println("protoMsg.getContent():=" +
                    protoMsg.getContent());


        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ProtoBufServer(18899).runServer();
    }
}
