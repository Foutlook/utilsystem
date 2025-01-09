package com.foutin.nio.netty.simple.cray.im.sender;

import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import com.foutin.nio.netty.simple.cray.im.bean.ChatMsg;
import com.foutin.nio.netty.simple.cray.im.converter.ChatMsgConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:16
 */
@Slf4j
public class ChatSender extends BaseSender {

    public void sendChatMsg(String touid, String content) {
        log.info("发送消息 startConnectServer");
        ChatMsg chatMsg = new ChatMsg(getUser());
        chatMsg.setContent(content);
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setTo(touid);
        chatMsg.setMsgId(System.currentTimeMillis());
        ProtoMsg.Message message =
                ChatMsgConverter.build(chatMsg, getUser(), getSession());

        super.sendMsg(message);
    }

    @Override
    protected void sendSucced(ProtoMsg.Message message) {
        log.info("发送成功:" + message.getMessageRequest().getContent());
    }


    @Override
    protected void sendfailed(ProtoMsg.Message message) {
        log.info("发送失败:" + message.getMessageRequest().getContent());
    }
}
