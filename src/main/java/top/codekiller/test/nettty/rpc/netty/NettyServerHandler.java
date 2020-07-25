package top.codekiller.test.nettty.rpc.netty;

import com.sun.xml.internal.ws.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.StringUtil;
import top.codekiller.test.nettty.rpc.provider.HelloServerImpl;

/**
 * @author codekiller
 * @date 2020/7/25 16:46
 * @Description 服务器的业务处理
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用我们的服务
        System.out.println(Thread.currentThread().getName()+"  msg="+msg);

        //客户端在调用服务器的api时，我们需要定义一个协议
        //比如我们要求 每次发送消息都是必须以某个字符串开头"HelloService#hello#"
        if(msg.toString().startsWith("HelloService#hello#")){
            String  result= new HelloServerImpl().hello(msg.toString().substring(msg.toString().lastIndexOf('#')+1));
            ctx.writeAndFlush(result);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常关闭"+cause.getMessage());
        ctx.close();
    }
}
