package top.codekiller.test.nettty.webSocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author codekiller
 * @date 2020/7/24 13:56
 * @Description TextWebSocketFrame类型，表示一个文本帧
 */
public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * @Description 当web客户端连接后，触发方法
     * @date 2020/7/24 14:03
     * @param ctx
     * @return void
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id 表示唯一的一个值，LongText是唯一的,ShortText不是唯一的
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asShortText());

    }

    /**
    * @Description 读取数据
    * @date 2020/7/24 13:59
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务端收到消息："+msg.text());

        //回复客户端
        Channel channel = ctx.channel();
        channel.writeAndFlush(new TextWebSocketFrame("服务器时间："+ LocalDateTime.now()+" "+msg.text()));
    }

    /**
    * @Description 异常处理
    * @date 2020/7/24 14:06
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 "+cause.getMessage());
        ctx.close();
    }

    /**
    * @Description 当web客户端断开连接后，触发方法
    * @date 2020/7/24 14:05
    * @param ctx
    * @return void
    */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asShortText());
    }
}
