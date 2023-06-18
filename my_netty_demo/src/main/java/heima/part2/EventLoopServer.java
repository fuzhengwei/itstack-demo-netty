package heima.part2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        //可以细分出一个eventloopgroup来处理事件，也就是加线程
        EventLoopGroup group = new DefaultEventLoopGroup();

        new ServerBootstrap()
                //这里创建两个group,一个负责放boos,一个负责放worker
                //boos只负责serversocketchannel上的accept事件，worker只负责连接上来的socketchannel上的读写事件
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf  = (ByteBuf) msg;
                                log.debug(buf.toString(Charset.defaultCharset())+1);
                                ctx.fireChannelRead(msg);//把消息传给下一个handler
                            }
                        }).addLast(group,new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf  = (ByteBuf) msg;
                                log.debug(buf.toString(Charset.defaultCharset())+2);
                                ctx.fireChannelRead(msg);//把消息传给下一个handler
                            }
                        });
                     }
                })
                .bind(8888);
    }
}