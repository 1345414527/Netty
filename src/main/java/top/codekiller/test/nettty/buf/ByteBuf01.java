package top.codekiller.test.nettty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * @author codekiller
 * @date 2020/7/23 21:01
 * @Description TODO
 */
public class ByteBuf01 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        //1.创建对象，该对象包含一个数组arr，是一个byte[10]
        //2.在netty的buffer中，不需要使用flip进行反转,
        //  底层维护了readerindex和writerindex
        ByteBuf buffer = Unpooled.buffer(10);

        for(int i=0;i<10;++i){
            buffer.writeByte(i);
        }

        System.out.println("capacity："+buffer.capacity());


        //输出
        for(int i=0;i<buffer.capacity();++i){
            System.out.println(buffer.getByte(i));
            System.out.println(buffer.readByte());
        }
    }
}
