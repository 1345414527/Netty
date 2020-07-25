package top.codekiller.test.nettty.rpc.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Callable;

/**
 * @author codekiller
 * @date 2020/7/25 17:04
 * @Description TODO
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    /**
     * 上下文
     */
    private ChannelHandlerContext context;
    /**
     * 返回的结果
     */
    private String result;
    /**
     * 客户端调用方法时，传入的参数
     */
    private String param;

    /**
    * @Description 与服务器的连接创建后，方法会被调用(1)
    * @date 2020/7/25 17:07
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //我在其他方法会用到ctx
        this.context=ctx;
    }


    /**
    * @Description 收到服务器的数据后，方法会被调用(4)
    * @date 2020/7/25 17:08
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("从服务端获取消息  "+Thread.currentThread().getName());
        this.result=msg.toString();
        //唤醒等待的线程
        notify();
    }

    /**
    * @Description 被代理对象调用，真正发送数据给服务器-->wait-->等待被唤醒，获取结果(3)-->(5)
    * @date 2020/7/25 17:12
    * @return java.lang.Object
    */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("代理发送消息  "+Thread.currentThread().getName());

        context.writeAndFlush(param);
        //进行wait,等待channelRead方法获取服务器的结果后，唤醒
        wait();
        //返回服务方返回的结果
        return this.result;
    }

    @Override
    public  void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    
    /**
    * @Description 设置参数(2)
    * @date 2020/7/25 17:20
    * @param param  
    * @return void
    */
    public void setParam(String param){
        this.param=param;
    }
    
}
