package org.itstack.demo.test;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * create by fuzhengwei3 on 2019/8/9
 */
public class ApiTest {

    public static void main(String[] args) {
        int max = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));

        System.out.println(max);
    }

}
