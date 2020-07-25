package top.codekiller.test.nettty.inboundAndoutboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/24 17:50
 * @Description TODO
 */
public class ByteToLongDecoder extends ByteToMessageDecoder {

    /**
    * @Description TODO
    * @date 2020/7/24 17:52
    * @param ctx  上下文对象
    * @param in  入站的ByteBuf
    * @param out  List集合，讲解码后的数据传给下一个handler
    * @return void
    */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToLongDecoder 的 decode 方法被调用...");
        //每次读取8个字节
        if(in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
