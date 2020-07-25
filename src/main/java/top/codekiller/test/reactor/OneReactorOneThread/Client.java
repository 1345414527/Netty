package top.codekiller.test.reactor.OneReactorOneThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author codekiller
 * @date 2020/7/22 18:29
 * @Description 客户端
 */
public class Client {

    private static Selector selector;

    public static void main(String[] args) throws IOException {
        //1.获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        //2.切换成非阻塞模式
        sChannel.configureBlocking(false);

        //3.分配指定大小的缓冲区
        ByteBuffer buffer=ByteBuffer.allocate(1024);

        selector=Selector.open();

        sChannel.register(selector, SelectionKey.OP_READ);


        //4.发送数据给服务端
//        buffer.put(new Date().toString().getBytes());
//        buffer.flip();
//        sChannel.write(buffer);

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        readInfo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()){
            String str=scanner.next();
            buffer.put((new Date().toString()+"\t"+str).getBytes());
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();

        }



        //5.关闭通道
        sChannel.close();
    }

    private static void readInfo() throws IOException{
        while (selector.select()>0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey sk = it.next();
                System.out.println(sk.isAcceptable()+" "+sk.isConnectable()+" "+sk.isReadable()+" "+sk.isWritable()+" "+sk.isValid());
                if(sk.isReadable()){
                   SocketChannel socketChannel=(SocketChannel) sk.channel();
                   ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
                   socketChannel.read(byteBuffer);
                   System.out.println(new String(byteBuffer.array()).trim());
                }
            }
            it.remove();
        }
    }
}
