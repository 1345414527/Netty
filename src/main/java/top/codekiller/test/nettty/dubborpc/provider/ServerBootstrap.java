package top.codekiller.test.nettty.dubborpc.provider;


import top.codekiller.test.nettty.dubborpc.netty.NettyServer;

//ServerBootstrap 会启动一个服务提供者，就是 NettyServer
public class ServerBootstrap {
    public static void main(String[] args) {

        //代码代填..
        NettyServer.startServer("127.0.0.1", 50000);
    }
}
