package top.codekiller.test.nettty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import top.codekiller.test.nettty.codec.StudentPOJO;

import java.util.Date;

/**
 * @author codekiller
 * @date 2020/7/23 10:31
 * @Description 服务器的事件处理器
 */
public class ServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        //根据dataType，显示不同的信息
        MyDataInfo.MyMessage.DateType dateType=msg.getDateType();

        if(dateType==MyDataInfo.MyMessage.DateType.StudentType){
            MyDataInfo.Student student=msg.getStudent();
            System.out.println("学生的信息:"+student.getId()+"："+student.getName());
        }else if(dateType==MyDataInfo.MyMessage.DateType.WorkerType){
            MyDataInfo.Worker worker=msg.getWorker();
            System.out.println("工人的信息："+worker.getName()+"："+worker.getAge());
        }else{
            System.out.println("传输的类型不正确");
        }
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
