package com.foutin.nio.netty.serialize.json;

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
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author f2485
 * @Description
 * @date 2024/12/20 16:53
 */
public class JsonSendClient {


    static String content = "疯狂创客圈：高性能学习社群!";

    private int serverPort;
    private String serverIp;
    Bootstrap b = new Bootstrap();

    public JsonSendClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    //...省略成员属性,构造器
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
                    // 客户端通道流水线添加 2 个 Handler 业务处理器
                    // 先使用Netty内置StringEncoder编码器将JSON字符串编码成二进制字节数组。然后，使用Netty内置LengthFieldPrepender编码器
                    // 将二进制字节数组编码成Head-Content二进制数据包。
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                }
            });
            ChannelFuture f = b.connect();
            // ...
            // 阻塞,直到连接完成
            f.sync();
            Channel channel = f.channel();
            // 发送 Json 字符串对象
            for (int i = 0; i < 1000; i++) {
                JsonMsg user = build(i, i + "->" + content);
                channel.writeAndFlush(user.convertToJson());
                System.out.println("发送报文：" + user.convertToJson());
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

    // 构建 Json 对象
    public JsonMsg build(int id, String content) {
        JsonMsg user = new JsonMsg();
        user.setId(id);
        user.setContent(content);
        return user;
    }


    public static void main(String[] args) {
        new JsonSendClient("127.0.0.1", 18899).runClient();
    }

}
