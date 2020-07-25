package top.codekiller.test.nettty.protocaltcp;

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
public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count=0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("服务器接受到的数据："+new String(msg.getContent(),CharsetUtil.UTF_8)
                            +"\t数据长度"+msg.getLen());

        System.out.println("服务器接受到的信息量："+(++this.count));

        //回送数据,返回一个UUID
        String id= UUID.randomUUID().toString();
        ctx.writeAndFlush(new MessageProtocol(id.getBytes(),id.getBytes().length));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
