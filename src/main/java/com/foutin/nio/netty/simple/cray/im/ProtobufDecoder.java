package com.foutin.nio.netty.simple.cray.im;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author f2485
 * @Description 自定义Protobuf解码器，通过继承Netty中基础的ByteToMessageDecoder解码器类实现，
 * 在其继承的decode方法中，将ByteBuf字节码解码成Protobuf的POJO实例，大致的过程如
 * 下：
 * （1）首先读取长度，如果长度位数不够，则终止读取。
 * （2）然后读取魔数、版本号等其他的字段。
 * （3）最后按照净长度读取内容。如果内容的字节数不够，则恢复到之前的起始位置
 * （也就是长度的位置），然后终止读取。
 * @date 2024/12/24 11:21
 */
public class ProtobufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 标记一下当前的读指针 readIndex 的位置
        in.markReaderIndex();
        // 判断包头的长度
        if (in.readableBytes() < 2)// 不够包头中的长度
        {
            return;
        }
        long l = in.readLong();
        System.out.println("读结果：" + l);
        int length = in.readShort(); // 读取传送过来的消息的长度。
        if (length < 0)// 长度如果小于 0
        {
            ctx.close();// 非法数据，关闭连接
        }
        if (length > in.readableBytes()) // 可读字节少于预期消息长度
        {
            in.resetReaderIndex(); // 重置读取位置
            return;
        }
        // 省略：读取魔数、版本号等其他的数据
        // 省略：读取内容
        byte[] array;
        if (in.hasArray()) // 堆缓冲
        {
            ByteBuf slice = in.slice();
            array = slice.array();
        } else {
            array = new byte[length]; // 直接缓冲
            in.readBytes(array, 0, length);
        }
        // 字节转成 Protobuf 的 POJO 对象
        ProtoMsg.Message outmsg = ProtoMsg.Message.parseFrom(array);
        if (outmsg != null) {
            out.add(outmsg);// Protobuf 的 POJO 实例加入到出站 List 容器
        }
    }
}
