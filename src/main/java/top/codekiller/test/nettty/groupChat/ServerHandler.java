package top.codekiller.test.nettty.groupChat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import jdk.nashorn.internal.ir.CallNode;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author codekiller
 * @date 2020/7/23 23:06
 * @Description TODO
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 使用一个hashMap管理
     */
    public static Map<User,Channel> channels=new ConcurrentHashMap<>();

    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor.INSTANCE 是全局的时间执行器，是一个单利
     */
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private  DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
    * @Description 表示连接建立，一旦连接，第一个被执行
     *              将当前channel接入到channelGroup
    * @date 2020/7/23 23:11
    * @param ctx
    * @return void
    */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
        //将该客户端加入聊天的信息推送给其它在线的客户端
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天\n");

        //这里可以私聊
        channels.put(new User(123456L),channel);
    }

    /**
    * @Description 表示channel处于活跃状态，提示xxx上线
    * @date 2020/7/23 23:18
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线");
    }


    /**
    * @Description 读取数据
    * @date 2020/7/24 0:15
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取当前channel
        Channel channel = ctx.channel();

        //这里我们遍历一下,自己不需要获取自己的消息
        for (Channel ch : channelGroup) {
            if(ch!=channel) {
                ch.writeAndFlush("[客户]"+ch.remoteAddress()+ LocalDateTime.now().format(formatter) +"发送消息："+msg+"\n");
            }else{
                ch.writeAndFlush(LocalDateTime.now().format(formatter)+"发送了消息："+msg+"[自己]\n");
            }
        }
    }

    /**
    * @Description 处理异常
    * @date 2020/7/23 23:29
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭
        ctx.close();
    }

    /**
    * @Description 表示channel处于非活跃状态，提示xxx离线
    * @date 2020/7/23 23:21
    * @param ctx
    * @return void
    */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+LocalDateTime.now().format(formatter) +"离线");
    }

    /**
    * @Description 断开连接，将xx客户端离开信息推送给当前在线的客户
    * @date 2020/7/23 23:22
    * @param ctx
    * @return void
    */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+LocalDateTime.now().format(formatter) +"离开聊天\n");
        System.out.println("channelGroup的大小："+channelGroup.size());
    }
}
