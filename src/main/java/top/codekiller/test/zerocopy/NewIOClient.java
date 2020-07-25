package top.codekiller.test.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

/**
 * @author codekiller
 * @date 2020/7/22 15:55
 * @Description TODO
 */
public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 7001));
        FileChannel fileChannel = FileChannel.open(Paths.get("F:\\Animation\\2.txt"), StandardOpenOption.CREATE_NEW, StandardOpenOption.READ);


        //传统方式下的读写
//        ByteBuffer buffer=ByteBuffer.allocate(1024);
//        while(fileChannel.read(buffer)!=-1){
//            buffer.flip();
//            socketChannel.write(buffer);
//            buffer.clear();
//        }

        //transferTo底层使用的就是零拷贝
        //在linux下，一个transferTo方法就可以全部传输
        //在win下，一个transferTo只能发送8m，就需要分段传输文件，而且要指明传输的位置
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送的总的字节数："+transferCount);
        fileChannel.close();
        socketChannel.close();
    }
}
