package com.foutin.nio.netty.simple.cray.im.handler;

import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import com.foutin.nio.netty.simple.cray.im.bean.User;
import com.foutin.nio.netty.simple.cray.im.converter.HeartBeatMsgConverter;
import com.foutin.nio.netty.simple.cray.im.session.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:27
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatClientHandler  extends ChannelInboundHandlerAdapter {


    //心跳的时间间隔，单位为s
    private static final int HEARTBEAT_INTERVAL = 50;

    //在Handler被加入到Pipeline时，开始发送心跳
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        HeartBeatMsgConverter builder =
                new HeartBeatMsgConverter(user, session);

        ProtoMsg.Message message = builder.build();
        //发送心跳
        heartBeat(ctx, message);
    }



    //使用定时器，发送心跳报文
    public void heartBeat(ChannelHandlerContext ctx,
                          ProtoMsg.Message heartbeatMsg) {
        ctx.executor().schedule(() -> {

            if (ctx.channel().isActive()) {
                log.info(" 发送 HEART_BEAT  消息 to server");
                ctx.writeAndFlush(heartbeatMsg);

                //递归调用，发送下一次的心跳
                heartBeat(ctx, heartbeatMsg);
            }

        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 接受到服务器的心跳回写
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = pkg.getType();
        if (headType.equals(ProtoMsg.HeadType.HEART_BEAT)) {

            log.info(" 收到回写的 HEART_BEAT  消息 from server");

            return;
        } else {
            super.channelRead(ctx, msg);

        }

    }
}
