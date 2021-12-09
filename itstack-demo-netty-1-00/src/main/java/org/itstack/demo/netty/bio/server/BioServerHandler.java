package org.itstack.demo.netty.bio.server;

import org.itstack.demo.netty.bio.ChannelAdapter;
import org.itstack.demo.netty.bio.ChannelHandler;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 微信公众号：bugstack虫洞栈 | 专注原创技术专题案例，以最易学习编程的方式分享知识，让萌新、小白、大牛都能有所收获。目前已完成的专题有；Netty4.x从入门到实战、用Java实现JVM、基于JavaAgent的全链路监控等，其他更多专题还在排兵布阵中。
 * 论坛：http://bugstack.cn
 * Create by 付政委 on @2019
 */
public class BioServerHandler extends ChannelAdapter {

    public BioServerHandler(Socket socket, Charset charset) {
        super(socket, charset, false);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("发生异常了");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void channelActive(ChannelHandler ctx) {
        System.out.println("链接报告LocalAddress:" + ctx.socket().getLocalAddress());
        ctx.writeAndFlush("hi! 我是bugstack虫洞栈 BioServer to msg for you \r\n");
    }

    @Override
    public void channelRead(ChannelHandler ctx, Object msg)  {
        System.out.println("[Server]" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" + msg);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ctx.writeAndFlush("hi 我已经收到你的消息Success！\r\n");
    }

}
