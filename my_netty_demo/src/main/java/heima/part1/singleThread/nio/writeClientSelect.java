package heima.part1.singleThread.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class writeClientSelect {
    public static void main(String[] args) throws IOException {
        SocketChannel sc =SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8888));

        int count = 0;
        //接受数据
        while (true){
            ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
            count += sc.read(buffer);
            log.debug("接收到的字节数为：{}",count);
            buffer.clear();
        }
    }
}
