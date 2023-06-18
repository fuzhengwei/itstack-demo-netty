package heima.part1.buffer;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        try(FileChannel channel = new FileInputStream("data.txt").getChannel()){
            ByteBuffer buffer = ByteBuffer.allocate(10);
            //打印buffer内容
            while (true){
                int len = channel.read(buffer);
                log.debug("读取到的字节数：{}",len);
                if (len == -1) break;
                buffer.flip();//切换到读模式
                while (buffer.hasRemaining()){
                    //检查还有剩余
                    byte b = buffer.get();
                    log.debug("读取内容为：{}",(char)b);
                }
                buffer.clear();
            }
        }catch (IOException e){
            log.debug(e.toString());
        }
    }

}
