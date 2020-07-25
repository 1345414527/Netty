package top.codekiller.test.nettty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author codekiller
 * @date 2020/7/23 11:27
 * @Description TODO
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
    * @Description 当通道就绪时会触发该方法
    * @date 2020/7/23 11:27
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，server", CharsetUtil.UTF_8));
    }

    /**
    * @Description 当通道有读取事件时，会触发
    * @date 2020/7/23 11:29
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("服务器回复的消息："+((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }
}
