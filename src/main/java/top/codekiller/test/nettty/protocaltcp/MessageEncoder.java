package top.codekiller.test.nettty.protocaltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @author codekiller
 * @date 2020/7/25 0:14
 * @Description TODO
 */
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MessageEncoder encode 方法被调用\n");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
