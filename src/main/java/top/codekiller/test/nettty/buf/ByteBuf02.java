package top.codekiller.test.nettty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @author codekiller
 * @date 2020/7/23 22:29
 * @Description TODO
 */
public class ByteBuf02 {

    public static void main(String[] args) {
        //创建ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("hello,world!", Charset.forName("utf-8"));

        //使用相关的方法
        if(buf.hasArray()){
            byte[] content = buf.array();
            //将content转为字符串
            System.out.println(new String(content,Charset.forName("utf-8")));

            System.out.println(buf.toString(CharsetUtil.UTF_8));

            System.out.println(buf.arrayOffset());
            System.out.println(buf.readerIndex());
            System.out.println(buf.writerIndex());
            System.out.println(buf.capacity());
            System.out.println("----------------");

            //可读的字节数
            int len=buf.readableBytes();
            System.out.println(len);

            //取出各个字节
            for(int i=0;i<len;i++){
                System.out.print(buf.readerIndex()+"："+buf.readByte()+"   ");
            }
            System.out.println("\n从第0个开始，读取4个："+buf.getCharSequence(0,4,CharsetUtil.UTF_8));
            System.out.println("从第4个开始，读取6个："+buf.getCharSequence(4,6,CharsetUtil.UTF_8));


        }
    }
}
