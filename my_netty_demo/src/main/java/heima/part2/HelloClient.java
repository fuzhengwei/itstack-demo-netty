package heima.part2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                //添加eventloop
                .group(new NioEventLoopGroup())
                //选额客户端channel实现
                .channel(NioSocketChannel.class)
                //添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new StringEncoder());
                    }
                })
                //链接服务器
                .connect(new InetSocketAddress("localhost", 8888))
                .sync()//同步方法会阻塞，等连接建立后才会往下面执行
                .channel();

        //从创建线程处理控制台的输入发给服务器，q则退出服务
        new Thread(()->{
            Scanner sc = new Scanner(System.in);
            while (true){
                String line = sc.nextLine();
                if ("q".equals(line)){
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        },"input").start();

        channel.writeAndFlush("yuguguguu");//发送数据，发送和接受数据都会走handler
        System.out.println("debug");
    }
}
