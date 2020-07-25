package top.codekiller.test.nettty.heartBeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author codekiller
 * @date 2020/7/24 0:51
 * @Description TODO
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
    * @Description 事件触发器
    * @date 2020/7/24 0:51
    * @param ctx
    * @param evt
    * @return void
    */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event=(IdleStateEvent)evt;

            String eventType=null;
            switch (event.state()){
                case READER_IDLE: eventType="读空闲";break;
                case WRITER_IDLE: eventType="写空闲";break;
                case  ALL_IDLE  : eventType="读写空闲";break;
            }
            System.out.println(ctx.channel().remoteAddress()+"--超时事件--"+eventType);
            System.out.println("服务器做相应处理...");
        }
    }
}
