package org.itstack.demo.netty.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 微信公众号：bugstack虫洞栈 | 专注原创技术专题案例，以最易学习编程的方式分享知识，让萌新、小白、大牛都能有所收获。目前已完成的专题有；Netty4.x从入门到实战、用Java实现JVM、基于JavaAgent的全链路监控等，其他更多专题还在排兵布阵中。
 * 论坛：http://bugstack.cn
 * Create by 付政委 on @2019
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

    @Override
    public void completed(AsynchronousSocketChannel result, AioServer attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new AioServerHandler(result));
    }

    @Override
    public void failed(Throwable exc, AioServer attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }

}
