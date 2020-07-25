package top.codekiller.test.nettty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @author codekiller
 * @date 2020/7/24 23:09
 * @Description TODO
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count=0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端发送10条数据
        for(int i=0;i<10;++i){
            ctx.writeAndFlush(Unpooled.copiedBuffer(("hello server "+i+"\t"), CharsetUtil.UTF_8));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("客户端接受到服务器的信息："+msg.toString(CharsetUtil.UTF_8));
        System.out.println("客户端接收到的信息量："+(++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
