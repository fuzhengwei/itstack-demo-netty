package heima.part1.buffer;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});

        buffer.flip();

        //这里，调了clear但是实际上是没有清除的，只是重写设置了position和limit指针的位置
        // 说明他是不会做删除操作的，只会做覆盖
        //flip和compact也是同理
        //gei(index)不会改变position和limit位置，只会获得索引的值
        buffer.clear();
        log.warn("被清除的位置值为{}",(char)buffer.get(2));
        //21:35:09 [WARN ] [main] h.TestByteBufferRead - 被清除的位置值为c

        //rewind重写开始读取,把position置于开头
        byte[] bytes = new byte[4];
        buffer.get(bytes);
        buffer.rewind();
        log.warn("从头开始读第一个为——{}",(char)buffer.get());
        buffer.rewind();
        //21:35:09 [WARN ] [main] h.TestByteBufferRead - 从头开始读第一个为——a


        //mark & reset
        //mark是做一个标记，记录position位置，reset是将position重置到mark位置
        log.warn("指针后退一位{}",(char)buffer.get());
        log.warn("指针后退一位{}",(char)buffer.get());
        buffer.mark();//记录索引2位置
        log.warn("指针后退一位{}",(char)buffer.get());
        log.warn("指针后退一位{}",(char)buffer.get());
        buffer.reset();//将position重置到索引位置
        log.warn("指针后退一位{}",(char)buffer.get());
        log.warn("指针后退一位{}",(char)buffer.get());
        //21:41:36 [WARN ] [main] h.TestByteBufferRead - 指针后退一位a
        //21:41:36 [WARN ] [main] h.TestByteBufferRead - 指针后退一位b
        //21:41:36 [WARN ] [main] h.TestByteBufferRead - 指针后退一位c
        //21:41:36 [WARN ] [main] h.TestByteBufferRead - 指针后退一位d
        //21:41:36 [WARN ] [main] h.TestByteBufferRead - 指针后退一位c
        //21:41:36 [WARN ] [main] h.TestByteBufferRead - 指针后退一位d

    }
}
