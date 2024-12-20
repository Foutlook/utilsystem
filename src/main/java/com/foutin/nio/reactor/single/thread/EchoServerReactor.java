package com.foutin.nio.reactor.single.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author f2485
 * @Description EchoServer的功能很简单：读取客户端的输入，回显到客户端，所以也叫回显服务
 * 器。基于Reactor反应器模式来实现，设计3个重要的类：
 * （1）设计一个反应器类：EchoServerReactor类。
 * （2）设计两个处理器类：AcceptorHandler新连接处理器、EchoHandler回显处理器。
 * @date 2024/12/13 10:22
 */
public class EchoServerReactor implements Runnable {

    Selector selector;
    ServerSocketChannel serverSocket;

    // 构造器
    EchoServerReactor() throws IOException {
        // 获取选择器、开启 serverSocket 服务监听通道
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(18899));

        // 注册 serverSocket 的 accept 新连接接收事件
        SelectionKey sk = serverSocket.register(selector,
                SelectionKey.OP_ACCEPT);
        // 将新连接处理器作为附件，绑定到 sk 选择键
        sk.attach(new AcceptorHandler());
    }

    // 轮询和分发事件
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select(1000);
                Set<SelectionKey> selected = selector.selectedKeys();
                if (null == selected || selected.size() == 0) {
                    continue;
                }
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    // 反应器负责 dispatch 收到的事件
                    SelectionKey sk = it.next();
                    dispatch(sk);
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 反应器的事件分发
    void dispatch(SelectionKey sk) {
        Runnable handler = (Runnable) sk.attachment();
        // 调用之前 attach 绑定到选择键的 handler 处理器对象
        if (handler != null) {
            handler.run();
        }
    }

    // Handler 之一:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                if (channel != null)
                    new EchoHandler(selector, channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }
}
