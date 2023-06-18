package heima.part2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                //选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //boss负责处理连接（accept）事件 worker(child)负责处理读写事件，决定了woker*(child)能执行哪 些操作
                .childHandler(
                        //channel是与客户端进行数据读写的通道，initializer初始化负责添加别的hander
                        new ChannelInitializer<NioSocketChannel>() {
                            //链接建立后就会调用这个initChannel方法
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //添加距离handler
                        ch.pipeline().addLast(new StringDecoder());//解码器，将bytebuf转为字符串
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){//自定义hanler
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("{}",msg);
                            }
                        });
                    }
                })
                .bind(8888);
    }
}
