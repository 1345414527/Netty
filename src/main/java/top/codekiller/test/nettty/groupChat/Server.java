package top.codekiller.test.nettty.groupChat;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
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
 * @date 2020/7/23 22:50
 * @Description TODO
 */
public class Server {
    /**
     * 监听端口
     */
    private int port;

    public Server(int port){
        this.port=port;
    }

    //编写run方法处理客户端的请求
    public void run(){
        //创建两个线程组
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG,128)
                            .childOption(ChannelOption.SO_KEEPALIVE,true)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    //获取到pipeline
                                    ChannelPipeline pipeline = ch.pipeline();
                                    //向pipeline加入解码器
                                    pipeline.addLast("decoder",new StringDecoder());
                                    //向pipeline加入编码器
                                    pipeline.addLast("encoder",new StringEncoder());
                                    //加入自己的业务处理handler
                                    pipeline.addLast(new ServerHandler());
                                }
                            });

            System.out.println("服务器启动");
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();

            //监听关闭事件
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Server(50000).run();
    }
}
