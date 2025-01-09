package com.foutin.nio.netty.simple.cray.im.handler;

import com.foutin.nio.netty.simple.cray.im.InvalidFrameException;
import com.foutin.nio.netty.simple.cray.im.client.CommandController;
import com.foutin.nio.netty.simple.cray.im.session.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:24
 */
@Slf4j
@ChannelHandler.Sharable
public class ExceptionHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    private CommandController commandController;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // ..

        if (cause instanceof InvalidFrameException) {
            log.error(cause.getMessage());
            ClientSession.getSession(ctx).close();
        } else {

            //捕捉异常信息
//             cause.printStackTrace();
            log.error(cause.getMessage());
            ctx.close();


            if (null == commandController) {
                return;
            }
            //开始重连
            commandController.setConnectFlag(false);
            commandController.startConnectServer();
        }
//        super.exceptionCaught(ctx,cause);

    }

    /**
     * 通道 Read 读取 Complete 完成
     * 做刷新操作 ctx.flush()
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


}
