package top.codekiller.test.nettty.inboundAndoutboundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author codekiller
 * @date 2020/7/24 18:00
 * @Description TODO
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();



        //加入一个出站的handler，对数据进行编码
        pipeline.addLast(new LongToByteEncoder());

        //计入一个入站的handler，对数据进行解码
        pipeline.addLast(new ByteToLongDecoder());

        //加入自定义的handler，处理业务
        pipeline.addLast(new ClientHandler());
    }
}
