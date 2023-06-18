package heima.part1.singleThread.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static heima.part1.buffer.ByteBufferUtil.debugRead;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        //nio和bio实现
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //创建服务器
        ServerSocketChannel  ssc= ServerSocketChannel.open();

        //将服务器设置为非阻塞模式
        ssc.configureBlocking(false);

        //绑定监听端口
        ssc.bind(new InetSocketAddress( 8888));

        //连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true){
//            log.debug("connecting...");
            //这里服务器是阻塞的就是是阻塞的方法，服务器是非阻塞的就是非阻塞的方法 ，非阻塞返回null
            SocketChannel sc = ssc.accept();
            if (sc != null){
                log.debug("connected---{}",sc);
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for(SocketChannel channel : channels){
                //接收客户端发送的数据
                //channel阻塞的就是是阻塞的方法，非阻塞的就是非阻塞的方法,没有读到数据返回0
                int read = channel.read(buffer);
                if (read > 0 ){
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                    log.debug("afterRead---{}",channel);
                }
            }
        }
    }
}
