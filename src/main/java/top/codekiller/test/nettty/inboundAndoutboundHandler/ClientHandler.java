package top.codekiller.test.nettty.inboundAndoutboundHandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author codekiller
 * @date 2020/7/24 18:02
 * @Description TODO
 */
public class ClientHandler extends SimpleChannelInboundHandler<Long> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送数据...");

        //发送一个Long
        ctx.writeAndFlush(123456L);

//        ctx.writeAndFlush(Unpooled.copiedBuffer("asdfzxcaaaaaaaaa", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("收到服务器消息："+msg);
    }


}
