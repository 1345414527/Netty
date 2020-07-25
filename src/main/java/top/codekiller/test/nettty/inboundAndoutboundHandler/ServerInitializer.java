package top.codekiller.test.nettty.inboundAndoutboundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import javax.swing.*;

/**
 * @author codekiller
 * @date 2020/7/24 17:48
 * @Description TODO
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //入站的handler进行解码
        pipeline.addLast(new ByteToLongDecoder2());

        //加入一个出站的handler，对数据进行编码
        pipeline.addLast(new LongToByteEncoder());

        //自定义handler
        pipeline.addLast(new ServerHandler());
    }
}
