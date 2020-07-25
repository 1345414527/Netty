package top.codekiller.test.nettty.protocaltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/25 0:16
 * @Description TODO
 */
public class MessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MessageDecoder decode 被调用");
        //将得到的二进制字节码->MessageProtocol数据包(对象)
        int len=in.readInt();

        byte[] content=new byte[len];
        in.readBytes(content);
        out.add(new MessageProtocol(content,len));
    }
}
