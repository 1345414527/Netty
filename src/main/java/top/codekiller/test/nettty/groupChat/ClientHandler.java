package top.codekiller.test.nettty.groupChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.channels.SocketChannel;

/**
 * @author codekiller
 * @date 2020/7/23 23:47
 * @Description TODO
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    /**
    * @Description 读取信息
    * @date 2020/7/23 23:47
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg.trim());
    }


}
