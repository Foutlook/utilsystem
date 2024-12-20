package com.foutin.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author f2485
 * @Description
 * @date 2024/12/11 15:28
 */
public class NioDiscardServer {

    /**
     * Java NIO将NIO事件进行了简化，只定义了四个事件，这四种事件用SelectionKey的四
     * 个常量来表示：
     * （1）可读：SelectionKey.OP_READ
     * （2）可写：SelectionKey.OP_WRITE
     * （3）连接：SelectionKey.OP_CONNECT
     * （4）接收：SelectionKey.OP_ACCEPT
     * 什么是IO事件呢？
     * 这个概念容易混淆，这里特别说明一下。这里的IO事件不是对通道的IO操作，而是通
     * 道处于某个IO操作的就绪状态，表示通道具备执行某个IO操作的条件。比方说某个
     * SocketChannel传输通道，如果完成了和对端的三次握手过程，则会发生“连接就绪”
     * （OP_CONNECT）的事件。再比方说某个ServerSocketChannel服务器连接监听通道，在监
     * 听到一个新连接的到来时，则会发生“接收就绪”（OP_ACCEPT）的事件。还比方说，
     * 一个SocketChannel通道有数据可读，则会发生“读就绪”（OP_READ）事件；一个等待
     * 写入数据的SocketChannel通道，会发生写就绪（OP_WRITE）事件。
     * @throws IOException
     */
    public static void startServer() throws IOException {
        // 1.获取选择器
        // 2.获取通道
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 3.设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 4.绑定连接
            serverSocketChannel.bind(new InetSocketAddress(18899));
            System.out.println("服务器启动成功");
            // 5.将通道注册的“接收新连接”IO 事件，注册到选择器上
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 6.轮询感兴趣的 IO 就绪事件（选择键集合）
            while (selector.select() > 0) {
                // 7.获取选择键集合
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    // 8.获取单个的选择键，并处理
                    SelectionKey selectedKey = selectedKeys.next();
                    // 9.判断 key 是具体的什么事件
                    if (selectedKey.isAcceptable()) {
                        // 10.若选择键的 IO 事件是“连接就绪”事件,就获取客户端连接
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        // 11.将新连接切换为非阻塞模式
                        socketChannel.configureBlocking(false);
                        // 12.将该新连接的通道的可读事件，注册到选择器上
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectedKey.isReadable()) {
                        // 13.若选择键的 IO 事件是“可读”事件, 读取数据
                        SocketChannel socketChannel =(SocketChannel) selectedKey.channel();
                        // 14.读取数据，然后丢弃
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int length = 0;
                        while ((length = socketChannel.read(byteBuffer)) > 0) {
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array(), 0, length));
                            byteBuffer.clear();
                        }
                        socketChannel.close();
                    }
                    // 15.移除选择键
                    selectedKeys.remove();
                }
            }
        }

    }


    public static void main(String[] args) throws IOException {startServer();}

}
