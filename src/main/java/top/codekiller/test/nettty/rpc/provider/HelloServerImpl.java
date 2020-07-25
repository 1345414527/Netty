package top.codekiller.test.nettty.rpc.provider;

import top.codekiller.test.nettty.rpc.publicInterface.IHelloService;

/**
 * @author codekiller
 * @date 2020/7/25 16:32
 * @Description TODO
 */
public class HelloServerImpl implements IHelloService {

    /**
    * @Description 当消费方调用该方法时，就返回一个结果
    * @date 2020/7/25 16:32
    * @param msg
    * @return java.lang.String
    */
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息："+msg);
        //根据msg，返回不同的结果
        if(msg!=null){
            return "你好客户端，我已经收到你的消息["+msg+"]";
        }else{
           return "大妹子，你没发送信息呀";
        }

    }
}
