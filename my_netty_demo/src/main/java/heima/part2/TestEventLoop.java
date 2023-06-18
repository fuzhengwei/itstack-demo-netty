package heima.part2;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) throws InterruptedException {
        //创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup(3);//处理io事件，普通任务，定时任务

        //这个lopp是头尾相接的循环，next方法还会获得下一个事件循环对象，多调用几次会回到开头
        log.debug("{}",group.next());
        log.debug("{}",group.next());
        log.debug("{}",group.next());
        log.debug("{}",group.next());
        //10:35:55 [DEBUG] [main] h.p.TestEventLoop - io.netty.channel.nio.NioEventLoop@3cc2931c
        //10:35:55 [DEBUG] [main] h.p.TestEventLoop - io.netty.channel.nio.NioEventLoop@20d28811
        //10:35:55 [DEBUG] [main] h.p.TestEventLoop - io.netty.channel.nio.NioEventLoop@3967e60c
        //10:35:55 [DEBUG] [main] h.p.TestEventLoop - io.netty.channel.nio.NioEventLoop@3cc2931c

        //提交任务执行
        /*group.next().execute(()->{
            log.debug("子线程");
        });

        log.debug("主线程");*/

        //执行定时任务

        group.scheduleAtFixedRate(() ->{
            log.debug("定时任务");
        },0,5, TimeUnit.SECONDS);

        Thread.sleep(100000);

    }
}
