package top.codekiller.test.nettty.groupChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author codekiller
 * @date 2020/7/23 23:32
 * @Description TODO
 */
public class Client {
    private final String host;
    private final  int port;

    public Client(String host,int port){
        this.host=host;
        this.port=port;
    }

    public void run(){
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast("handler",new ClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port)).sync();

            Channel channel = channelFuture.channel();
            System.out.println("--------"+channel.localAddress()+"-----------");

            //客户端需要输入信息，创建一个扫描器
            Scanner scanner=new Scanner(System.in);
            while(scanner.hasNext()){
                String str=scanner.nextLine();
                //通过channel发送到服务器
                channel.writeAndFlush(str);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventExecutors.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Client("127.0.0.1",50000).run();
    }
}
