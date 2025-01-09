package com.foutin.nio.netty.simple.cray.im.converter;

import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import com.foutin.nio.netty.simple.cray.im.bean.ChatMsg;
import com.foutin.nio.netty.simple.cray.im.bean.User;
import com.foutin.nio.netty.simple.cray.im.session.ClientSession;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:19
 */
public class ChatMsgConverter extends BaseConverter {

    private ChatMsg chatMsg;
    private User user;


    private ChatMsgConverter(ClientSession session) {
        super(ProtoMsg.HeadType.MESSAGE_REQUEST, session);


    }


    public ProtoMsg.Message build(ChatMsg chatMsg, User user) {

        this.chatMsg = chatMsg;
        this.user = user;

        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);


        ProtoMsg.MessageRequest.Builder cb =
                ProtoMsg.MessageRequest.newBuilder();
        //填充字段
        this.chatMsg.fillMsg(cb);
        ProtoMsg.Message requestMsg = outerBuilder.setMessageRequest(cb).build();

        return requestMsg;
    }

    public static ProtoMsg.Message build(
            ChatMsg chatMsg,
            User user,
            ClientSession session) {

        ChatMsgConverter chatMsgConverter = new ChatMsgConverter(session);


        return chatMsgConverter.build(chatMsg, user);

    }
}
