package heima.part1.singleThread.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

@Slf4j
public class writerServerSelect {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8888));

        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector,SelectionKey.OP_READ,null);

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 30000000; i++) {
                        sb.append("f");
                    }
                    ByteBuffer sendBuffer = Charset.defaultCharset().encode(sb.toString());

                    //判断是否有剩余内容
                    if (sendBuffer.hasRemaining()){
                         //有剩余内容则在已有事件的基础上关注可写事件
                        sckey.interestOps(sckey.interestOps() + SelectionKey.OP_WRITE);
                        sckey.attach(sendBuffer);
                    }
                }else if(key.isWritable()){
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel socketCh = (SocketChannel) key.channel();

                    int write = socketCh.write(buffer);
                    log.debug("发送数据字节数为：{}",write);

                    //buffer空了需要清理这个buffer，以免占用内存
                    if (!buffer.hasRemaining()){
                        key.attach(null);
                        key.interestOps(key.interestOps()-SelectionKey.OP_WRITE);
                    }

                }
            }
        }

    }
}
