package com.foutin.nio.netty.simple.cray.im;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author f2485
 * @Description 自定义Protobuf编码器，通过继承Netty中基础的MessageToByteEncoder编码器类，实现
 * 其抽象的编码方法encode(…)，在该方法中把以下内容写入到目标ByteBuf：
 * （1）写入待发送的Protobuf POJO实例的二进制字节长度；
 * （2）写入其他的字段，如魔数、版本号；
 * （3）写入Protobuf POJO实例的二进制字节码内容。
 * @date 2024/12/24 11:14
 */
public class ProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ProtoMsg.Message msg, ByteBuf out){
        byte[] bytes = msg.toByteArray();// 将对象转换为字节
        int length = bytes.length;// 读取消息的长度
        // 将消息长度写入，这里只用两个字节，最大为 32767
        out.writeShort(length);
        // 省略魔数、版本号的写入，写入的方式写入长度是类似的
        out.writeLong(11111L);
        // 消息体中包含我们要发送的数据
        out.writeBytes(msg.toByteArray());
    }
}
