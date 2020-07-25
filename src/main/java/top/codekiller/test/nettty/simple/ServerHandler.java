package top.codekiller.test.nettty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author codekiller
 * @date 2020/7/23 10:31
 * @Description 服务器的事件处理器
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {



    /**
    * @Description 读取数据事件(这里我们可以读取客户端发送的消息)
    * @date 2020/7/23 10:33
    * @param ctx  上下文对象，含有：管道(pipeline),通道(channel),地址
    * @param msg  客户端发送的数据
    * @return void
    */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("服务器读取线程"+Thread.currentThread().getName());
//        System.out.println("server ctx = "+ctx);
//        System.out.println("看看channel和pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表
//
//        //将msg转为一个ByteBuf
//        //ByteBuf是Netty提供的，不是NIO的ByteBuffer
//        ByteBuf buf=(ByteBuf) msg;
//        System.out.println("客户端发送消息："+buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址："+ctx.channel().remoteAddress());

        //比如我们有一个非常耗时的业务->异步执行->提交该channel对应的NioEventLoop的taskQueue中
//        Thread.sleep(10000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端",CharsetUtil.UTF_8));
//        System.out.println("go on ...");

        //解决方案1 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端1",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端2",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        //解决方案二 用户自定义定时任务
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端3",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        },5, TimeUnit.SECONDS);

        System.out.println("go on ...");
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
