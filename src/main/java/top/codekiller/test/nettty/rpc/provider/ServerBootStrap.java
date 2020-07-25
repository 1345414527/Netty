package top.codekiller.test.nettty.rpc.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import top.codekiller.test.nettty.rpc.netty.NettyServer;

/**
 * @author codekiller
 * @date 2020/7/25 16:05
 * @Description 服务提供者
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        //启动服务
        NettyServer.startServer("127.0.0.1",50000);
    }
}
