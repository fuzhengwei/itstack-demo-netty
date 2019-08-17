package org.itstack.demo.test;

import org.itstack.demo.netty.server.NettyServer;

/**
 * 虫洞栈：https://bugstack.cn
 * 公众号：bugstack虫洞栈  ｛获取学习源码｝
 * 虫洞群：①群5398358 ②群5360692
 * Create by fuzhengwei on 2019
 */
public class NettyServerTest {

    public static void main(String[] args) {
        //启动服务
        new NettyServer().bing(7397);
    }

}
