package top.codekiller.test.nettty.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import top.codekiller.test.nettty.codec.StudentPOJO;

import java.util.Random;

/**
 * @author codekiller
 * @date 2020/7/23 11:27
 * @Description TODO
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
    * @Description 当通道就绪时会触发该方法
    * @date 2020/7/23 11:27
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;
        System.out.println(random);
        if(random==0){
            myMessage=MyDataInfo.MyMessage.newBuilder().setDateType(MyDataInfo.MyMessage.DateType.StudentType)
                                              .setStudent(MyDataInfo.Student.newBuilder().setName("重庆刘德华").setId(2).build()).build();
        }else{
            myMessage=MyDataInfo.MyMessage.newBuilder().setDateType(MyDataInfo.MyMessage.DateType.WorkerType)
                                             .setWorker(MyDataInfo.Worker.newBuilder().setName("香港徐大虾").setAge(21).build()).build();
        }


        ctx.writeAndFlush(myMessage);
    }

    /**
    * @Description 当通道有读取事件时，会触发
    * @date 2020/7/23 11:29
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("服务器回复的消息："+((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }
}
