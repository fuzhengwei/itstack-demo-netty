package heima.part1.singleThread.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8888));
        sc.write(Charset.defaultCharset().encode("1234567890yugugugu\n"));
        System.out.println("waitting");
    }
}
