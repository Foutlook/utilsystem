package com.foutin.nio.netty.simple.cray.im.handler;

import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import com.foutin.nio.netty.simple.cray.im.bean.ProtoInstant;
import com.foutin.nio.netty.simple.cray.im.session.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:26
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChatMsgHandler chatMsgHandler;

    @Autowired
    private HeartBeatClientHandler heartBeatClientHandler;



    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = ((ProtoMsg.Message) msg).getType();
        if (!headType.equals(ProtoMsg.HeadType.LOGIN_RESPONSE)) {
            super.channelRead(ctx, msg);
            return;
        }


        //判断返回是否成功
        ProtoMsg.LoginResponse info = pkg.getLoginResponse();

        ProtoInstant.ResultCodeEnum result =
                ProtoInstant.ResultCodeEnum.values()[info.getCode()];

        if (!result.equals(ProtoInstant.ResultCodeEnum.SUCCESS)) {
            //登录失败
            log.info(result.getDesc());
        } else {
            //登录成功
            ClientSession.loginSuccess(ctx, pkg);
            ChannelPipeline p = ctx.pipeline();
            //移除登录响应处理器
            p.remove(this);
            //在编码器后面，动态插入心跳处理器
            p.addAfter("encoder", "heartbeat", heartBeatClientHandler);
            p.addAfter("encoder", "chat",chatMsgHandler);

            heartBeatClientHandler.channelActive(ctx);

        }

    }
}
