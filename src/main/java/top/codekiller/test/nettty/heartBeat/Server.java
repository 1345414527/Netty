package top.codekiller.test.nettty.heartBeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author codekiller
 * @date 2020/7/24 0:32
 * @Description TODO
 */
public class Server {
    public static void main(String[] args) {
        //创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG,128)
                            .childOption(ChannelOption.SO_KEEPALIVE,true)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    //加入一个netty提供的idleStateHandler
                                    /*
                                    说明
                                        1.idleStateHandler 是netty 提供的处理空闲状态的处理器
                                        2.long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包，检测是否连接
                                        3.long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包，检测是否连接
                                        4.long allIdleTime:表示多长时间既没有读也没有写，就会发送一个心跳检测包，检测是否连接
                                        5.当IdealStateEvent触发后，就会传递给管道的下一个handler去处理,
                                          通过调用(触发)下一个handler的userEventTriggered()，在该方法中去处理
                                     */
                                    pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                                    //加入一个对空闲检测进一步处理的handler(自定义)
                                    pipeline.addLast(new ServerHandler());

                                }
                            });

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(50000)).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
