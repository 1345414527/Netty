package top.codekiller.test.nettty.protocaltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import sun.plugin2.message.Message;

/**
 * @author codekiller
 * @date 2020/7/24 23:09
 * @Description TODO
 */
public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count=0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端发送10条数据

        for(int i=0;i<5;++i){
            byte[] content = ("hello server " + i + "\t").getBytes(CharsetUtil.UTF_8);
            MessageProtocol messageProtocol = new MessageProtocol(content,content.length);
            System.out.println(messageProtocol.getLen()+" "+new String(messageProtocol.getContent(),CharsetUtil.UTF_8));
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("客户端接受到服务器的信息："+new String(msg.getContent(),CharsetUtil.UTF_8)
                           +"\t数据长度"+msg.getLen());
        System.out.println("客户端接收到的信息量："+(++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
