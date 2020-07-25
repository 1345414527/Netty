package top.codekiller.test.nettty.inboundAndoutboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author codekiller
 * @date 2020/7/24 18:03
 * @Description TODO
 */
public class LongToByteEncoder extends MessageToByteEncoder<Long> {

    /**
    * @Description 编码器
    * @date 2020/7/24 18:03
    * @param ctx
    * @param msg
    * @param out
    * @return void
    */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("LongToByteEncoder 的 encode 方法被调用...");
        System.out.println("msg="+msg);
        out.writeLong(msg);
    }
}
