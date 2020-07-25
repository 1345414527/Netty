package top.codekiller.test.nettty.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

/**
 * @author codekiller
 * @date 2020/7/25 17:21
 * @Description TODO
 */
public class NettyClient {

    /**
     * 创建线程池
     */
      //不推荐
//    private static ExecutorService executor=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private  static ThreadFactory threadFactory=new DefaultThreadFactory("netty-client");

    private static  ExecutorService executor=new ThreadPoolExecutor(10,10,
                                60L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10),threadFactory);

    //使用guava提供的ThreadFactoryBuilder来创建线程池(推荐)
//    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
//            .setNameFormat("demo-pool-%d").build();

    private static NettyClientHandler clientHandler;

    public Object getBean(final Class<?> serviceClass,final  String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{serviceClass},
                                    ((proxy, method, args) -> {
                                        if(clientHandler==null){
                                            initClient();
                                        }
                                        //设置要发给服务器端的信息
                                          //协议头+调用远程api的参数
                                        clientHandler.setParam(providerName+args[0]);
                                        return executor.submit(clientHandler).get();
                                    }));
    }

    private static void initClient(){
        clientHandler=new NettyClientHandler();
        //创建EventLoopGroup
        EventLoopGroup group=new NioEventLoopGroup();

        try {
            Bootstrap bootstrap=new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true) //不延迟
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(clientHandler);
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 50000)).sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
