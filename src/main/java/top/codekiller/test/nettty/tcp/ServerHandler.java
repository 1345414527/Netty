package top.codekiller.test.nettty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @author codekiller
 * @date 2020/7/24 23:17
 * @Description TODO
 */
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count=0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("服务器接受到的数据："+msg.toString(CharsetUtil.UTF_8));
        System.out.println("服务器接受到的信息量："+(++this.count));

        //回送数据,返回一个UUID
        String id= UUID.randomUUID().toString();
        ctx.writeAndFlush(Unpooled.copiedBuffer(id+"\n",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
