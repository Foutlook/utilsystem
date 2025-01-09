package com.foutin.nio.netty.decoding;

import com.foutin.nio.netty.echo.NettyEchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @author f2485
 * @Description 半包问题的实践案例
 * 可以看出存在三种类型的输出：
 * （1）读到一个完整的客户端输入ByteBuf；  --- 全包
 * （2）读到多个客户端的ByteBuf输入，但是“粘”在了一起；  -- 粘包
 * （3）读到部分ByteBuf的内容，并且有乱码。  --- 半包
 * 为了简单起见，也可以将“粘包”的情况看成特殊的“半包”。“粘包”和“半包”
 * 可以统称为传输的“半包问题”。
 * @date 2024/12/20 10:46
 */
public class NettyDumpSendClient {

    private int serverPort;
    private String serverIp;
    Bootstrap b = new Bootstrap();

    public NettyDumpSendClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    public void runClient() {
        // 创建反应器轮询组
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
            // 5 装配子通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                // 有连接到达时会创建一个通道
                protected void initChannel(SocketChannel ch) {
                    // 管理子通道中的 Handler 业务处理器
                    // 向子通道流水线添加一个 Handler 业务处理器
                    ch.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            ChannelFuture f = b.connect();
            // 阻塞,直到连接完成
            f.sync();
            Channel channel = f.channel();
            // 发送大量的文字
            String content = "疯狂创客圈：高性能学习者社群!";
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < 1000; i++) {
                // 发送 ByteBuf
                ByteBuf buffer = channel.alloc().buffer();
                buffer.writeBytes(bytes);
                channel.writeAndFlush(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int port = 18899;
        String ip = "127.0.0.1";
        new NettyDumpSendClient(ip, port).runClient();
    }
}
