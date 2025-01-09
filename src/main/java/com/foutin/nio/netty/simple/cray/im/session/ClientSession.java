package com.foutin.nio.netty.simple.cray.im.session;

import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import com.foutin.nio.netty.simple.cray.im.bean.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * @author f2485
 * @Description 实现客户端 Session会话
 * @date 2024/12/24 11:36
 */
@Slf4j
@Data
public class ClientSession {

    public static final AttributeKey<ClientSession> SESSION_KEY =
            AttributeKey.valueOf("SESSION_KEY");
    private Channel channel;
    private User user;
    private String sessionId; // 保存登录后的服务端 sessionid
    private Boolean connected = false;
    private Boolean login = false;

    // 绑定通道
    public ClientSession(Channel channel) {
        this.channel = channel;
        this.sessionId = String.valueOf(-1);
        // 重要：ClientSession 绑定到 Channel 上
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }

    // 登录成功之后,设置 sessionId
    public static void loginSuccess(
            ChannelHandlerContext ctx, ProtoMsg.Message pkg) {
        Channel channel = ctx.channel();
        ClientSession session =
                channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(pkg.getSessionId());
        session.setLogin(true);
        log.info("登录成功");
    }

    // 获取通道
    public static ClientSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ClientSession session =
                channel.attr(ClientSession.SESSION_KEY).get();
        return session;
    }

    // 把 protobuf 数据包写入通道
    public ChannelFuture witeAndFlush(Object pkg) {
        ChannelFuture f = channel.writeAndFlush(pkg);
        return f;
    }

    public void writeAndClose(Object pkg) {
        ChannelFuture future = channel.writeAndFlush(pkg);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    //关闭通道
    public void close() {
        connected = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.error("连接顺利断开");
                }
            }
        });
    }
}
