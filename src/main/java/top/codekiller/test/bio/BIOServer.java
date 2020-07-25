package top.codekiller.test.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author codekiller
 * @date 2020/7/22 12:00
 * @Description TODO
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        //线程池机制

        //1.创建一个线程池
        //2.如果有客户端连接，就创建一个线程，与之通信
        ExecutorService newCachedThreadPool= Executors.newCachedThreadPool();

        //创建ServerSocket
        ServerSocket serverSocket=new ServerSocket(6666);
        System.out.println("服务器启动了");

        while(true){
            //监听，等待客户端连接
            System.out.println("等待连接...");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端了");

            //就创建一个线程，与之通信
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    /**
    * @Description 编写一个handler方法，和客户端通讯
    * @date 2020/7/22 12:09
    * @param socket
    * @return void
    */
    public static void handler(Socket socket){
        try {
            System.out.println("线程信息 id ="+Thread.currentThread().getId()+" 名字="+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();

            while(true){
                System.out.println("read...");
                int read=inputStream.read(bytes);
                if(read!=-1){
                    //输出客户端发送的数据
                    System.out.println(new String(bytes,0,read));
                }else{
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
