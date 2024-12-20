package com.foutin.nio.reactor.single.thread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author f2485
 * @Description
 * @date 2024/12/13 10:37
 */
public class EchoHandler implements Runnable{

    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    //处理器实例的状态：发送和接收，一个连接对应一个处理器实例
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;
    //构造器
    EchoHandler(Selector selector, SocketChannel c) throws IOException {
        channel = c;
        c.configureBlocking(false);
        //取得选择键，再设置感兴趣的 IO 事件
        sk = channel.register(selector, 0);
        //将 Handler 自身作为选择键的附件，一个连接对应一个处理器实例
        sk.attach(this);
        //注册 Read 就绪事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (state == SENDING) {
                //发送状态，写入数据到连接通道
                channel.write(byteBuffer);
                //写完后,准备开始从通道读,byteBuffer 切换成写入模式
                byteBuffer.clear();
                //注册 read 就绪事件，开始接收客户端数据
                sk.interestOps(SelectionKey.OP_READ);
                //修改状态,进入接收的状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                //接收状态，从通道读取数据
                int length = 0;
                while ((length = channel.read(byteBuffer)) > 0) {
                    System.out.print(new String(byteBuffer.array(), 0, length));
                }
                //读完后，准备开始写入通道,byteBuffer 切换成读取模式
                byteBuffer.flip();
                //准备写数据到通道，注册 write 就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                //注册完成后,进入发送的状态
                state = SENDING;
            }
            //处理结束了, 这里不能关闭 select key，需要重复使用
            //sk.cancel();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
