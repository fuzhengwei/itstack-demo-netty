package heima.part1.singleThread.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import static heima.part1.buffer.ByteBufferUtil.debugAll;

@Slf4j
public class serverSelect {

    private static void split(ByteBuffer source){
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n'){
                int length = i+1-source.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }

        }
        source.compact();
    }
    public static void main(String[] args) throws IOException {
        //select实现aio
        //创建selector,用于管理多个channel
        Selector selector = Selector.open();


        //创建服务器
        ServerSocketChannel ssc= ServerSocketChannel.open();
        //将服务器设置为非阻塞模式
        ssc.configureBlocking(false);

        //将channel注册到selector里面
        //selectionkey在事件发生后可以通过它知道是那个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);



        //绑定监听端口
        ssc.bind(new InetSocketAddress( 8888));
        while (true){
            //select方法在没有事件发生是阻塞的，有事件未处理是不会阻塞的，可以想象成blockingqueue
            selector.select();
            //处理事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //处理key的时候需要把他从selectKeys中删除，
                //因为他是不会自动删除的，如果不删除，事件又被处理过的，获得的channel为空，会报空指针异常
                //迭代器中可以删除，增强fori循环是不能做一边遍历一边删除的
                log.debug("used key:{}",key);
                //区分事件类型
                if (key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);//放入附件，一个channel对应一个buffer
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("channel:{}",sc);
                }else if (key.isReadable()){
                    try {
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        int read = channel.read(buffer);
                        //read ==-1表示服务端是正常断开
                        if (read == -1){
                            key.cancel();
                        }else {
                            split(buffer);
                            if (buffer.position() == buffer.limit()){
                                ByteBuffer newbuffer = ByteBuffer.allocate(buffer.capacity()*2);
                                buffer.flip();
                                newbuffer.put(buffer);
                                key.attach(newbuffer);
                            }
                        }
                        buffer.clear();
                    }catch (Exception e){
                        e.printStackTrace();
                        key.cancel();//因为客户端异常断开了，需要把发生的事件取消。
                    }
                }
                iterator.remove();
            }
        }

    }
}
