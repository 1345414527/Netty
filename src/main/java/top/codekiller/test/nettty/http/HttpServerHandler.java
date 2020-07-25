package top.codekiller.test.nettty.http;

import com.sun.jndi.toolkit.url.Uri;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author codekiller
 * @date 2020/7/23 17:08
 * @Description TODO
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
    * @Description 读取客户端的数据
    * @date 2020/7/23 17:21
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
       //判断msg是不是httpRequest请求
        if(msg instanceof HttpRequest){
            System.out.println("msg类型："+msg.getClass());
            System.out.println("客户端地址："+ctx.channel().remoteAddress());
            System.out.println("pipeline："+ctx.pipeline().hashCode()+"  channel："+ctx.channel().hashCode());

            //获取到
            HttpRequest httpRequest=(HttpRequest) msg;
            //获取uri
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 /favicon.ico ,不做响应");
                return;
            }

            //回复信息给浏览器
            ByteBuf buf= Unpooled.copiedBuffer("hello，我是服务器", CharsetUtil.UTF_8);

            //构造相应的http的相应，即httpresponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,buf.readableBytes());

            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
