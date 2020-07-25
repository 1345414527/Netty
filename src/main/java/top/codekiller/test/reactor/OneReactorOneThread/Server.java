package top.codekiller.test.reactor.OneReactorOneThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author codekiller
 * @date 2020/7/22 18:29
 * @Description 服务器
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //1.获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //2.切换成非阻塞模式
        ssChannel.configureBlocking(false);

        //3.绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        //4.获取一个选择器
        Selector selector = Selector.open();
        System.out.println(selector);

        //5.将通道注册到选择器上,并且指定监听事件(这里是接受事件)
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        Handler handler=new Handler();

        //6.轮询式的获取选择器上已经”准备就绪“的事件
        while((selector.select())>0){
            //7.获取当前选择器中所有注册的“选择键(已就绪的监听事件)”
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                //8.获取准备就绪的事件
                SelectionKey sk = it.next();
                System.out.println(sk.isAcceptable()+" "+sk.isConnectable()+" "+sk.isReadable()+" "+sk.isWritable()+" "+sk.isValid());
                //9.判断具体是什么事件准备就绪
                if(sk.isAcceptable()){
                    //10.若接受就绪，获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();

                    //11.切换到非阻塞模式
                    sChannel.configureBlocking(false);

                    //12.将通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);

                    System.out.println(sChannel.getRemoteAddress()+" 上线...");
                }else if(sk.isReadable()){
                   handler.readData(sk,selector);
                }
                //15. 取消选择键
                it.remove();
            }
        }

    }


}

class Handler{
    public  void readData(SelectionKey sk,Selector selector) throws IOException {
        SocketChannel sChannel=null;
        try {
            //13.获取当前选择器上“读就绪”状态的通道
            sChannel =(SocketChannel) sk.channel();

            //14.读取数据
            ByteBuffer buffer=ByteBuffer.allocate(1024);
            int count=sChannel.read(buffer);
            if(count>0){
                String msg = new String(buffer.array());
                System.out.println("form 客户端: " + msg.trim());
                //向其它的客户端转发消息(去掉自己), 专门写一个方法来处理
                sendInfoToOtherClients(msg, sChannel,selector);
            }
        } catch (IOException e) {
            try {
                System.out.println(sChannel.getRemoteAddress() + " 离线了..");
                //取消注册
                sk.cancel();
                //关闭通道
                sChannel.close();
            }catch (IOException e2) {
                e2.printStackTrace();;
            }
        }finally {

        }
    }

    public void sendInfoToOtherClients(String msg,SocketChannel self,Selector selector) throws IOException {
        //遍历 所有注册到 selector 上的 SocketChannel,并排除 self
        for (SelectionKey selectedKey : selector.keys()) {
            Channel targetChannel = selectedKey.channel();
            if(targetChannel instanceof SocketChannel&&targetChannel!=self){
                SocketChannel dest=(SocketChannel)targetChannel;
                ByteBuffer buffer=ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
                System.out.println("发送数据");
            }
        }

    }
}
