package top.codekiller.test.nettty.inboundAndoutboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/24 20:27
 * @Description TODO
 */
public class ByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToLongDecoder2 的 decode 方法被调用...");
        out.add(in.readLong());
    }
}
