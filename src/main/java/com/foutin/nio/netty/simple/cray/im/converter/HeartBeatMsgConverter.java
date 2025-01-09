package com.foutin.nio.netty.simple.cray.im.converter;

import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import com.foutin.nio.netty.simple.cray.im.bean.User;
import com.foutin.nio.netty.simple.cray.im.session.ClientSession;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:29
 */
public class HeartBeatMsgConverter extends BaseConverter {

    private final User user;

    public HeartBeatMsgConverter(User user, ClientSession session) {
        super(ProtoMsg.HeadType.HEART_BEAT, session);
        this.user = user;
    }

    public ProtoMsg.Message build() {

        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);

        ProtoMsg.MessageHeartBeat.Builder inner =
                ProtoMsg.MessageHeartBeat.newBuilder()
                        .setSeq(0)
                        .setJson("{\"from\":\"client\"}")
                        .setUid(user.getUid());
        return outerBuilder.setHeartBeat(inner).build();
    }

}
