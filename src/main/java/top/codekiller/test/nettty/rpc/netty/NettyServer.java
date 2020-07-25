package top.codekiller.test.nettty.rpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author codekiller
 * @date 2020/7/25 16:38
 * @Description 服务启动
 */
public class NettyServer {


    public static void startServer(String host,int port){
        startServer0(host,port);
    }


    private static  void startServer0(String host,Integer port){
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG,128)
                            .childOption(ChannelOption.SO_KEEPALIVE,true)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel sc) throws Exception {
                                    ChannelPipeline pipeline = sc.pipeline();

                                    pipeline.addLast(new StringDecoder());
                                    pipeline.addLast(new StringEncoder());
                                    //业务处理器
                                    pipeline.addLast(new NettyServerHandler());
                                }
                            });
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();


            //进行监听
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println(Thread.currentThread().getName());
                    if(future.isSuccess()){
                        System.out.println("服务提供方开始 提供服务成功");
                    }else{
                        System.out.println("服务提供方开始 提供服务失败");
                    }
                }
            });

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
