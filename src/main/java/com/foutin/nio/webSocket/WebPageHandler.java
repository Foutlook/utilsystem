package com.foutin.nio.webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;

/**
 * @author f2485
 * @Description
 * @date 2025/1/9 11:15
 */
@Slf4j
public class WebPageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        /**
         * HTTP请求的格式不正确
         */
        if (!request.decoderResult().isSuccess()) {
            HttpProtocolHelper.sendError(ctx, BAD_REQUEST);
            return;
        }

        // 只允许 GET 请求
        if (!GET.equals(request.method())) {
            HttpProtocolHelper.sendError(ctx, FORBIDDEN);
            return;
        }


        /**
         * 缓存HTTP协议的版本号
         */
        HttpProtocolHelper.cacheHttpProtocol(ctx, request);

        String webContent = IOUtil.loadResourceFile("com/foutin/nio/webSocket/index.html");

        /**
         * 发送web 操作页面
         */
        HttpProtocolHelper.sendWebPage(ctx, webContent);
    }


}
