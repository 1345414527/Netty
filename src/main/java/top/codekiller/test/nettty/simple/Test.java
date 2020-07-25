package top.codekiller.test.nettty.simple;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * @author codekiller
 * @date 2020/7/23 11:39
 * @Description TODO
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors());
    }
}
