package top.codekiller.test.nettty.inboundAndoutboundHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author codekiller
 * @date 2020/7/24 17:57
 * @Description TODO
 */
public class Client {
    public static void main(String[] args) {
        EventLoopGroup group=new NioEventLoopGroup();

        try {
            Bootstrap bootstrap=new Bootstrap();

            bootstrap.group(group)
                      .channel(NioSocketChannel.class)
                      .handler(new ClientInitializer());

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 50000)).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
