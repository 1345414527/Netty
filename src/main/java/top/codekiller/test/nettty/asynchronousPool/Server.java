package top.codekiller.test.nettty.asynchronousPool;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.IOException;

/**
 * @author codekiller
 * @date 2020/7/23 10:12
 * @Description 服务端
 */
public class Server {

    static final EventExecutorGroup group=new DefaultEventExecutorGroup(2);

    public static void main(String[] args) throws IOException {
        //创建BossGroup和WorkerGroup
        //bossGroup和workerGroup含有的子线程的个数，默认为cpu核数*2
        EventLoopGroup bossGroup=new NioEventLoopGroup(5);
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap  bootstrap=new ServerBootstrap();

        try {
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup)  //设置两个线程
                    .channel(NioServerSocketChannel.class)  //使用NioServerSocketChannel，作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列里连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //handler对应的是boosGruop，childHandler对应的是workerGroup
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //可以使用一个集合管理SocketChannel，在推送消息时，可以将业务加入到各个channel对应的
                            // NioEventLoop的taskQueue或者scheduleTaskQueue中
                            System.out.println("客户端socketChannel hashcode="+socketChannel.hashCode());

                            //如果我们在addLast添加handler，前面有指定EventExecutorGroup,name该handler会优先加入到该线程池中
                            socketChannel.pipeline().addLast(group,new ServerHandler());
                        }
                    });  //给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器 is ready ...");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给 cf 注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("监听端口成功");
                    }else{
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
