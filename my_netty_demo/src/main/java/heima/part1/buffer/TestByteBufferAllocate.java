package heima.part1.buffer;

import java.nio.ByteBuffer;

public class TestByteBufferAllocate {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());

        //class java.nio.HeapByteBuffer --表示分配的空间为堆内存，读写效率低并且会受GC影响
        //class java.nio.DirectByteBuffer --直接内存，读写效率高
    }
}
