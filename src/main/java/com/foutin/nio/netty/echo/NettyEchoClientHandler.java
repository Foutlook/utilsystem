package com.foutin.nio.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

/**
 * @author f2485
 * @Description
 * @date 2024/12/19 19:25
 */
@ChannelHandler.Sharable
public class NettyEchoClientHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoClientHandler INSTANCE
            = new NettyEchoClientHandler();

    // 入站处理方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf byteBuf = (ByteBuf) msg;
        int len = byteBuf.readableBytes();
        byte[] arr = new byte[len];
        byteBuf.getBytes(0, arr);
        System.out.println("client received: " + new String(arr, "UTF-8"));
        // 释放 ByteBuf 的两种方法
        // 方法一：手动释放 ByteBuf
        byteBuf.release();
        // 方法二：调用父类的入站方法，将 msg 向后传递
        // super.channelRead(ctx,msg);
    }

}
