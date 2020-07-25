package top.codekiller.test.nettty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author codekiller
 * @date 2020/7/23 10:31
 * @Description 服务器的事件处理器
 */
public class ServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
        System.out.println("客户端发送的数据："+msg.getId()+"："+msg.getName());
    }

    /**
    * @Description 数据读取完毕
    * @date 2020/7/23 10:46
    * @param ctx  
    * @return void
    */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲并刷新
        //一般来讲，我们队发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello,客户端",CharsetUtil.UTF_8));
    }

    /**
    * @Description 处理异常，一般是需要管理通道
    * @date 2020/7/23 11:13
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
