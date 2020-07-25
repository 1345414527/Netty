package top.codekiller.test.nettty.rpc.consumer;

import top.codekiller.test.nettty.rpc.netty.NettyClient;
import top.codekiller.test.nettty.rpc.provider.HelloServerImpl;
import top.codekiller.test.nettty.rpc.publicInterface.IHelloService;

/**
 * @author codekiller
 * @date 2020/7/25 19:32
 * @Description TODO
 */
public class ClientBootStrap {

    private static final  String providerName="HelloService#hello#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient consumer = new NettyClient();

        //创建一个代理对象
        IHelloService helloService=(IHelloService)consumer.getBean(IHelloService.class,providerName);

        //调用服务提供者的方法
        String res = helloService.hello("你好呀");
        System.out.println("调用的结果res="+res);

    }
}
