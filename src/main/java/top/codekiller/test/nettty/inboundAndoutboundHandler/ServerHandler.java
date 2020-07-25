package top.codekiller.test.nettty.inboundAndoutboundHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author codekiller
 * @date 2020/7/24 17:55
 * @Description TODO
 */
public class ServerHandler extends SimpleChannelInboundHandler<Long> {
    /**
    * @Description 读取数据
    * @date 2020/7/24 17:56
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("从客户端"+ctx.channel().remoteAddress()+"读到的Long数据："+msg);

        //给客户端发送一个long
        ctx.writeAndFlush(987654L);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常"+cause.getMessage()+cause.getCause()+cause.getStackTrace());
        ctx.close();
    }
}
