package top.codekiller.test.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author codekiller
 * @date 2020/7/22 15:50
 * @Description TODO
 */
public class NewIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(7001));
        ServerSocket socket = serverSocketChannel.socket();

        //创建buffer
        ByteBuffer buffer=ByteBuffer.allocate(1024*4);

        while(true){
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readcount=0;
            while(readcount!=-1){
                readcount = socketChannel.read(buffer);
                buffer.rewind();
            }
        }
    }
}
