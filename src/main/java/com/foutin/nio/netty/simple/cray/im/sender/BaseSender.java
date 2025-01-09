package com.foutin.nio.netty.simple.cray.im.sender;

import com.foutin.nio.netty.simple.cray.im.bean.User;
import com.foutin.nio.netty.simple.cray.im.session.ClientSession;
import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 11:35
 */
@Data
@Slf4j
public abstract class BaseSender {

    private User user;
    private ClientSession session;


    public void sendMsg(ProtoMsg.Message message) {
        if (null == getSession() || !isConnected()) {
            log.info("连接还没成功");
            return;
        }
        Channel channel = getSession().getChannel();
        ChannelFuture f = channel.writeAndFlush(message);
        f.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) {
                // 回调
                if (future.isSuccess()) {
                    sendSucced(message);
                } else {
                    sendfailed(message);
                }
            }
        });
        //…
    }

    protected void sendSucced(ProtoMsg.Message message) {
        System.out.println("发送成功");
    }

    protected void sendfailed(ProtoMsg.Message message) {
        System.out.println("发送失败");
    }

    public boolean isConnected() {
        if (null == session) {
            log.info("session is null");
            return false;
        }
        return session.getConnected();
    }

    public boolean isLogin() {
        if (null == session) {
            log.info("session is null");
            return false;
        }

        return session.getLogin();
    }

}
