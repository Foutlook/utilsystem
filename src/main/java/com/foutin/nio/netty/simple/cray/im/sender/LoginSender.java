package com.foutin.nio.netty.simple.cray.im.sender;

import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import com.foutin.nio.netty.simple.cray.im.converter.LoginMsgConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:17
 */
@Slf4j
public class LoginSender extends BaseSender{

    public void sendLoginMsg() {
        if (!isConnected()) { log.info("还没有建立连接!");return; }

        log.info("构造登录消息");

        ProtoMsg.Message message =
                LoginMsgConverter.build(getUser(), getSession());
        log.info("发送登录消息");
        super.sendMsg(message);
    }

}
