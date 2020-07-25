package top.codekiller.test.nettty.asynchronousPool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author codekiller
 * @date 2020/7/23 10:31
 * @Description 服务器的事件处理器
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    static final EventExecutorGroup group=new DefaultEventExecutorGroup(16);

    private final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    /**
    * @Description 读取数据事件(这里我们可以读取客户端发送的消息)
    * @date 2020/7/23 10:33
    * @param ctx  上下文对象，含有：管道(pipeline),通道(channel),地址
    * @param msg  客户端发送的数据
    * @return void
    */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("接收到的消息："+buf.toString(CharsetUtil.UTF_8)+"  "+ LocalDateTime.now().format(formatter) +"  当前线程----"+Thread.currentThread().getName());

        //任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("服务端发送消息1  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端1\t",CharsetUtil.UTF_8));
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("服务端发送消息2  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端2\t",CharsetUtil.UTF_8));
            }
        });

        //定时任务
        ctx.channel().eventLoop().schedule(()->{
            System.out.println("服务端发送消息3  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端4\t",CharsetUtil.UTF_8));
        },10,TimeUnit.SECONDS);


        System.out.println("服务端发送消息4  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端4\t",CharsetUtil.UTF_8));



//        //将任务提交到group线程池
//        group.submit(new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//                System.out.println("服务端发送消息4  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端4\t",CharsetUtil.UTF_8));
//                return null;
//            }
//        });

        System.out.println("发送消息...");
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
