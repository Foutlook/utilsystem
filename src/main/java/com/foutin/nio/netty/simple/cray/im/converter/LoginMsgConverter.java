package com.foutin.nio.netty.simple.cray.im.converter;

import com.foutin.nio.netty.simple.cray.im.ProtoMsg;
import com.foutin.nio.netty.simple.cray.im.bean.User;
import com.foutin.nio.netty.simple.cray.im.session.ClientSession;

/**
 * @author f2485
 * @Description
 * @date 2024/12/24 16:20
 */
public class LoginMsgConverter extends BaseConverter {


    private final User user;

    public LoginMsgConverter(User user, ClientSession session) {
        super(ProtoMsg.HeadType.LOGIN_REQUEST, session);
        this.user = user;
    }

    public ProtoMsg.Message build() {

        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);


        ProtoMsg.LoginRequest.Builder lb =
                ProtoMsg.LoginRequest.newBuilder()
                        .setDeviceId(user.getDevId())
                        .setPlatform(user.getPlatform().ordinal())
                        .setToken(user.getToken())
                        .setUid(user.getUid());

        ProtoMsg.Message requestMsg = outerBuilder.setLoginRequest(lb).build();

        return requestMsg;
    }


    public static ProtoMsg.Message build(User user, ClientSession session) {
        LoginMsgConverter converter = new LoginMsgConverter(user, session);
        return converter.build();

    }
}
