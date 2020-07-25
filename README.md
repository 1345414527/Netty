## 介绍

### 概述

- Netty 是由 JBOSS 提供的一个 **Java** **开源框架**，现为 **Github** **上的独立项目**。 

-  Netty 是一个**异步**的、**基于事件驱动**的网络应用框架，用以快速开发高性能、高可靠性的网络 IO 程序。

- Netty 主要针对在 TCP 协议下，面向 Clients 端的高并发应用，或者 Peer-to-Peer 场景下的大量数据持续传输的 

  应用。 

- Netty 本质是一个 NIO 框架，适用于服务器通讯相关的多种应用场景

  ![](https://img-blog.csdnimg.cn/2020072211263226.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



- 要透彻理解 Netty ， 需要先学习 NIO ， 这样我们才能阅读 Netty 的源码

![](https://img-blog.csdnimg.cn/20200722170349962.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### 原生NIO存在的问题

- NIO 的类库和 API 繁杂，使用麻烦：需要熟练掌握 Selector、ServerSocketChannel、SocketChannel、ByteBuffer 

  等

- 需要具备其他的额外技能：要熟悉 Java 多线程编程，因为 NIO 编程涉及到 Reactor 模式，你必须对多线程 

  和网络编程非常熟悉，才能编写出高质量的 NIO 程序

- 开发工作量和难度都非常大：例如客户端面临断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常流 

  的处理等等

- JDK NIO 的 Bug：例如臭名昭著的 Epoll Bug，它会导致 Selector 空轮询，最终导致 CPU 100%。直到 JDK 1.7 

  版本该问题仍旧存在，没有被根本解决



<br/>

### 优点

Netty 对 JDK 自带的 NIO 的 API 进行了封装，解决了上述问题

- 设计优雅：适用于各种传输类型的统一 API 阻塞和非阻塞 Socket；基于灵活且可扩展的事件模型，可以清晰 

  地分离关注点；高度可定制的线程模型 - 单线程，一个或多个线程池.

- 使用方便：详细记录的 Javadoc，用户指南和示例；没有其他依赖项，JDK 5（Netty 3.x）或 6（Netty 4.x）就 

  足够了

- 高性能、吞吐量更高：延迟更低；减少资源消耗；最小化不必要的内存复制。

- 安全：完整的 SSL/TLS 和 StartTLS 支持

- 社区活跃、不断更新：社区活跃，版本迭代周期短，发现的 Bug 可以被及时修复，同时，更多的新功能会被 

  加入 

<br/>





### 应用场景

#### 互联网行业

- 互联网行业：在分布式系统中，各个节点之间需要远程服务调用，高性能的 RPC 框架必不可少，Netty 作为 

  异步高性能的通信框架，往往作为基础通信组件被这些 RPC 框架使用。 

- 典型的应用有：阿里分布式服务框架 Dubbo 的 RPC 框架使用 Dubbo 协议进行节点间通信，Dubbo 协议默 

  认使用 Netty 作为基础通信组件，用于实现各进程节点之间的内部通信 

<br/>







#### 游戏

- 无论是手游服务端还是大型的网络游戏，Java 语言得到了越来越广泛的应用 

-  Netty 作为高性能的基础通信组件，提供了 TCP/UDP 和 HTTP 协议栈，方便定制和开发私有协议栈，账号登 

录服务器 

- 地图服务器之间可以方便的通过 Netty 进行高性能的通信

<br/>



#### 大数据

- 经典的 Hadoop 的高性能通信和序列化组件 Avro 的 RPC 框架，默认采用 Netty 进行跨界点通信 
- 它的 Netty Service 基于 Netty 框架二次封装实现

![](https://img-blog.csdnimg.cn/20200722113203314.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



## I/O模型

### 基本说明

1. I/O 模型简单的理解：就是用什么样的通道进行数据的发送和接收，很大程度上决定了程序通信的性能 

2. Java 共支持 3 种网络编程模型/IO 模式：BIO、NIO、AIO 

   - **Java BIO** ： `同步并阻塞(传统阻塞型)`，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器 

     端就需要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销

     ![](https://img-blog.csdnimg.cn/20200722114701711.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

     

     

   - **Java NIO** ： `同步非阻塞`，服务器实现模式为一个线程处理多个请求(连接)，即客户端发送的连接请求都会注 

     册到多路复用器上，多路复用器轮询到连接有 I/O 请求就进行处理

     ![](https://img-blog.csdnimg.cn/20200722114915306.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

     

     

   - **Java AIO(NIO.2)** ： `异步非阻塞`，AIO 引入异步通道的概念，采用了 Proactor 模式，简化了程序编写，有效 

     的请求才启动线程，它的特点是先由操作系统完成后才通知服务端程序启动线程去处理，一般适用于连接数较 

     多且连接时间较长的应用 

<br/>



### BIO、NIO、AIO 适用场景分析

- BIO 方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，

  JDK1.4 以前的唯一选择，但程序简单易理解

- NIO 方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，弹幕系统，服务器间通讯等。 

  编程比较复杂，JDK1.4 开始支持。

- AIO 方式使用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用 OS 参与并发操作， 

  编程比较复杂，JDK7 开始支持。



<br/>



### 对比

|          | BIO      | NIO        | AIO        |
| -------- | -------- | ---------- | ---------- |
| IO模型   | 同步阻塞 | 同步非阻塞 | 异步非阻塞 |
| 编程难度 | 简单     | 复杂       | 复杂       |
| 可靠性   | 差       | 好         | 好         |
| 吞吐量   | 低       | 高         | 高         |

<br/>



### 阻塞例子

- 同步阻塞: 效率是最低的，实际程序中，就是fd未设置O_NONBLOCK 标志位的read/write操作。
  - 老王用水壶烧水，并且站在那里(阻塞)，不管水开没开，每隔一定时间看看水开了没(同步->轮询)。 
- 同步非阻塞: 实际上效率是低下的，注意对fd设置O_NONBLOCK 标志位。
  - 老王用水壶烧水，不再傻傻的站在那里，跑去做别的事情(非阻塞)，但是还是会每个一段时间过来看看水开了没，没开就继续去做的事情(同步->轮询)。
- 异步阻塞: 异步操作是可以被阻塞住的，只不过它不是在处理消息时阻塞，而是在等待消息时被阻塞。比如select数，假如传入的最后一个timeout函数为NULL,那么如果所关注的事件没有一个被触发，程序就会一直阻塞在select调用处。
  - 老王用响水壶烧水，站在那里(阻塞)，但是不再去看水开了没，而是等水开了，水壶会自动通知它(异步，内核通知进程)。 
-  异步非阻塞: 效率更高，注册一个回调函数，就可以去做别的事情。
  - 老王用响水壶烧水。跑去做别的事情(非阻塞)，等待响水壶烧开水自动通知它(异步，内核通知进程)

<br/>



## BIO

### 基本介绍

- Java **BIO** 就是传统的 **java io** **编程**，其相关的类和接口在 java.io 

- BIO(**blocking I/O**) ： 同步阻塞，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器端就需 要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销，可以通过线程池机制改善(实 现多个客户连接服务器)。

- BIO 方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4 以前的唯一选择，程序简单易理解 

<br/>



### 工作机制

![](https://img-blog.csdnimg.cn/20200722115540993.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



### 编程流程

- 服务器端启动一个 ServerSocket 
- 客户端启动 Socket 对服务器进行通信，默认情况下服务器端需要对每个客户 建立一个线程与之通讯
- 客户端发出请求后, 先咨询服务器是否有线程响应，如果没有则会等待，或者被拒绝
- 如果有响应，客户端线程会等待请求结束后，再继续执行

<br/>



### 应用实例

```java
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
```

<br/>



**客户端连接**

![](https://img-blog.csdnimg.cn/20200722122651366.png)

```java
telnet 127.0.0.1 6666 
    
按ctrl+]
    
send hello
```



<br/>



**查看控制台**

```java
服务器启动了
等待连接...
连接到一个客户端了
等待连接...
线程信息 id =11 名字=pool-1-thread-1
read...
hello
read...
```

> 可以看到，等待连接那里会阻塞，read...也会阻塞

<br/>



### 问题分析

- 每个请求都需要创建独立的线程，与对应的客户端进行数据 Read，业务处理，数据 Write 。
- 当并发数较大时，需要创建大量线程来处理连接，系统资源占用较大。
- 连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在 Read 操作上，造成线程资源浪费





<br/>



## NIO

&emsp;:point_right: ​[NIO学习记录](https://blog.csdn.net/qq_44766883/article/details/107507548)



<br/>



## NIO的拷贝和零拷贝

### 零拷贝的基本介绍

- 零拷贝是网络编程的关键，很多性能优化都离不开。 

- ## Java中的零拷贝说的是只是用户态的零拷贝，不是操作系统层面的零拷贝 (CPU拷贝)

- 在 Java 程序中，常用的零拷贝有 mmap(内存映射) 和 sendFile。那么，他们在 OS 里，到底是怎么样的一个 

  的设计？我们分析 mmap 和 sendFile 这两个零拷贝 

- 另外我们看下 NIO 中如何使用零拷贝



<br/>



### 传统 IO 数据读写

&emsp;Java 传统 IO 和 网络编程的一段代码

![](https://img-blog.csdnimg.cn/20200722131546587.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

&emsp;传统 IO 模型 

![](https://img-blog.csdnimg.cn/20200722153838936.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

> 这是一个从磁盘文件中读取并且通过Socket写出的过程，对应的系统调用如下。

- 程序使用`read()`系统调用，系统由用户态转换为内核态，磁盘中的数据由`DMA`（Direct memory access）的方式读取到**内核读缓冲区（kernel buffer）**。DMA过程中CPU不需要参与数据的读写，**而是DMA处理器直接将硬盘数据通过总线传输到内存中。**
- 系统由内核态转为用户态，当程序要读的数据已经完全存入内核读缓冲区以后，**程序会将数据由内核读缓冲区，写入到用户缓冲区，**这个过程需要CPU参与数据的读写。
- 程序使用`write()`系统调用，**系统由用户态切换到内核态**，数据从用户缓冲区写入到**网络缓冲区（Socket Buffer）**，这个过程需要CPU参与数据的读写。
- 系统由内核态切换到用户态，**网络缓冲区**的数据通过DMA的方式传输到**协议栈（存储缓冲区）中（protocol engine）**



> 可以看到，**普通的拷贝过程经历了四次内核态和用户态的切换（上下文切换），两次CPU从内存中进行数据的读写过程，**这种拷贝过程相对来说比较消耗系统资源。



<br/>



### mmap

&emsp;mmap 通过内存映射，将**文件映射到内核缓冲区**，同时，**用户空间可以共享内核空间的数据**。这样，在进行网 络传输时，就可以减少内核空间到用户空间的拷贝次数。

![](https://img-blog.csdnimg.cn/20200722153856394.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

1. `mmap()`系统调用首先会使用DMA的方式将磁盘数据读取到内核缓冲区，然后通过内存映射的方式，**使用户缓冲区和内核读缓冲区的内存地址为同一内存地址，也就是说不需要CPU再讲数据从内核读缓冲区复制到用户缓冲区。**
2. 当使用`write()`系统调用的时候，cpu将内核缓冲区（等同于用户缓冲区）的数据直接写入到**网络发送缓冲区（socket buffer）**，然后通过DMA的方式将数据传入到协议栈中准备发送。

> **可以看到这种内存映射的方式减少了CPU的读写次数，但是用户态到内核态的切换（上下文切换）依旧有四次，**同时需要注意在进行这种内存映射的时候，有可能会出现并发线程操作同一块内存区域而导致的严重的数据不一致问题，所以需要进行合理的并发编程来解决这些问题。



<br/>



### 内核空间内部传输I/O

&emsp;Linux 2.1 版本 提供了 sendFile 函数，其基本原理如下：`数据根本不经过用户态`，直接从内核缓冲区进入到 Socket Buffer，同时，由于和用户态完全无关，就减少了一次上下文切换

&emsp;通过`sendfile()`系统调用，可以做到内核空间内部直接进行I/O传输。

![](https://img-blog.csdnimg.cn/2020072215391212.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

1. `sendfile()`系统调用也会引起用户态到内核态的切换，**与内存映射方式不同的是，用户空间此时是无法看到或修改数据内容，也就是说这是一次完全意义上的数据传输过程。**
2. 从磁盘读取到内存是DMA的方式，从**内核读缓冲区**读取到**网络发送缓冲区**，依旧需要CPU参与拷贝，而从网络发送缓冲区到协议栈中依旧是DMA方式。

<br/>

> 依旧有一次CPU进行数据拷贝，两次用户态和内核态的切换操作，相比较于内存映射的方式有了很大的进步，**但问题是程序不能对数据进行修改，而只是单纯地进行了一次数据的传输过程。**

<br/>



### 理想状态下的零拷贝I/O

![](https://img-blog.csdnimg.cn/20200722153946206.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



&emsp;依旧是系统调用**sendfile()**

```java
sendfile(socket, file, len);
```

&emsp;可以看到，这是真正意义上的零拷贝，因为其间CPU已经不参与数据的拷贝过程，也就是说完全通过其他硬件和中断的方式来实现数据的读写过程吗，**但是这样的过程需要硬件的支持才能实现。**

> 借助于硬件上的帮助，我们是可以办到的。之前我们是把页缓存的数据拷贝到socket缓存中，实际上，我们仅仅需要把缓冲区`描述符`传到socket缓冲区，再把数据长度传过去，这样DMA控制器直接将页缓存中的数据打包发送到网络中就可以了。

&emsp;**系统调用`sendfile()`发起后，磁盘数据通过DMA方式读取到内核缓冲区，内核缓冲区中的数据通过DMA聚合网络缓冲区，然后一齐发送到协议栈中。**



> **可以看到在这种模式下，是没有一次CPU进行数据拷贝的，所以就做到了真正意义上的零拷贝，虽然和前一种是同一个系统调用，但是这种模式实现起来需要硬件的支持，但对于基于操作系统的用户来讲，操作系统已经屏蔽了这种差异，它会根据不同的硬件平台来实现这个系统调用**
>
> 需要注意的一点是，这里其实有 一次 cpu 拷贝：kernel buffer -> socket buffer，但是，拷贝的信息很少，比如 lenght , offset , 消耗低，可以忽略



<br/>



### 零拷贝的再次理解

- 我们说零拷贝，是从**操作系统的角度**来说的。因为内核缓冲区之间，没有数据是重复的（只有 kernel buffer 有 

  一份数据）

- 零拷贝不仅仅带来更少的数据复制，还能带来其他的性能优势，例如更少的上下文切换，更少的 CPU 缓存伪 

  共享以及无 CPU 校验和计算。

<br/>



### mmap 和 sendFile 的区别

| mmap                                   | sendFile                               |
| -------------------------------------- | -------------------------------------- |
| 适合小数据量读写                       | 适合大文件传输                         |
| 需要 4 次上下文切换，3 次数据拷贝      | 需要 3 次上下文切换，最少 2 次数据拷贝 |
| 不能（必须从内核拷贝到 Socket 缓冲区） | 可以利用 DMA 方式，减少 CPU 拷贝       |



<br/>



### 案列

#### 客户端

```java
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
//在win下，一个transferTo只能发送2G，就需要分段传输文件，而且要指明传输的位置
long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
System.out.println("发送的总的字节数："+transferCount);

fileChannel.close();
socketChannel.close();
```





>transferTo底层使用的就是零拷贝
>在linux下，一个transferTo方法就可以全部传输
>在win下，一个transferTo只能发送2G，就需要分段传输文件，而且要指明传输的位置。transferFrom只能发送8M
>
>看源码:2147483647L/(1024\*1024\*1024)≈2G, 8388608L/(1024*1024)=8M



<br/>



#### 服务端

```java
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
```



&emsp;**运行**

```java
发送的总的字节数：672
```



<br/>





## Netty介绍

### 概述

- Netty 是由 JBOSS 提供的一个 **Java** **开源框架**，现为 **Github** **上的独立项目**。 

-  Netty 是一个**异步**的、**基于事件驱动**的网络应用框架，用以快速开发高性能、高可靠性的网络 IO 程序。

- Netty 主要针对在 TCP 协议下，面向 Clients 端的高并发应用，或者 Peer-to-Peer 场景下的大量数据持续传输的 

  应用。 

- Netty 本质是一个 NIO 框架，适用于服务器通讯相关的多种应用场景

  ![](https://img-blog.csdnimg.cn/2020072211263226.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



- 要透彻理解 Netty ， 需要先学习 NIO ， 这样我们才能阅读 Netty 的源码

![](https://img-blog.csdnimg.cn/20200722170349962.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### 原生NIO存在的问题

- NIO 的类库和 API 繁杂，使用麻烦：需要熟练掌握 Selector、ServerSocketChannel、SocketChannel、ByteBuffer 

  等

- 需要具备其他的额外技能：要熟悉 Java 多线程编程，因为 NIO 编程涉及到 Reactor 模式，你必须对多线程 

  和网络编程非常熟悉，才能编写出高质量的 NIO 程序

- 开发工作量和难度都非常大：例如客户端面临断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常流 

  的处理等等

- JDK NIO 的 Bug：例如臭名昭著的 Epoll Bug，它会导致 Selector 空轮询，最终导致 CPU 100%。直到 JDK 1.7 

  版本该问题仍旧存在，没有被根本解决



<br/>

### 优点

Netty 对 JDK 自带的 NIO 的 API 进行了封装，解决了上述问题

- 设计优雅：适用于各种传输类型的统一 API 阻塞和非阻塞 Socket；基于灵活且可扩展的事件模型，可以清晰 

  地分离关注点；高度可定制的线程模型 - 单线程，一个或多个线程池.

- 使用方便：详细记录的 Javadoc，用户指南和示例；没有其他依赖项，JDK 5（Netty 3.x）或 6（Netty 4.x）就 

  足够了

- 高性能、吞吐量更高：延迟更低；减少资源消耗；最小化不必要的内存复制。

- 安全：完整的 SSL/TLS 和 StartTLS 支持

- 社区活跃、不断更新：社区活跃，版本迭代周期短，发现的 Bug 可以被及时修复，同时，更多的新功能会被 

  加入 

<br/>





### 应用场景

#### 互联网行业

- 互联网行业：在分布式系统中，各个节点之间需要远程服务调用，高性能的 RPC 框架必不可少，Netty 作为 

  异步高性能的通信框架，往往作为基础通信组件被这些 RPC 框架使用。 

- 典型的应用有：阿里分布式服务框架 Dubbo 的 RPC 框架使用 Dubbo 协议进行节点间通信，Dubbo 协议默 

  认使用 Netty 作为基础通信组件，用于实现各进程节点之间的内部通信 

<br/>







#### 游戏

- 无论是手游服务端还是大型的网络游戏，Java 语言得到了越来越广泛的应用 

-  Netty 作为高性能的基础通信组件，提供了 TCP/UDP 和 HTTP 协议栈，方便定制和开发私有协议栈，账号登 

录服务器 

- 地图服务器之间可以方便的通过 Netty 进行高性能的通信

<br/>



#### 大数据

- 经典的 Hadoop 的高性能通信和序列化组件 Avro 的 RPC 框架，默认采用 Netty 进行跨界点通信 
- 它的 Netty Service 基于 Netty 框架二次封装实现

![](https://img-blog.csdnimg.cn/20200722113203314.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### 下载

&emsp;:point_right: [点击官网下载](https://bintray.com/netty/downloads/netty/)

<br/>



## Netty 高性能架构设计

### 线程模型基本介绍

&emsp;不同的线程模式，对程序的性能有很大影响，为了搞清 Netty 线程模式，我们来系统的讲解下 各个线程模式， 最后看看 Netty 线程模型有什么优越性.

&emsp;目前存在的线程模型有：

- 传统阻塞 I/O 服务模型
- Reactor 模式

&emsp;根据 Reactor 的数量和处理资源池线程的数量不同，有 3 种典型的实现 

- 单 Reactor 单线程
- 单 Reactor 多线程 
- 主从 Reactor 多线程

&emsp;Netty 线程模式(Netty 主要基于主从 Reactor 多线程模型做了一定的改进，其中主从 Reactor 多线程模型有多 个 Reactor) 

<br/>



### 传统阻塞 I/O 服务模型 

#### 工作原理图 

![](https://img-blog.csdnimg.cn/20200722181218142.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

- 黄色的框表示对象
- 蓝色的框表示线程 
- 白色的框表示方法(API) 

<br/>



#### 模型特点 

- 采用阻塞 IO 模式获取输入的数据
- 每个连接都需要独立的线程完成数据的输入，业务处理, 数据返回

<br/>



#### 问题分析

- 当并发数很大，就会创建大量的线程，占用很大系统资源
- 连接创建后，如果当前线程暂时没有数据可读，该线程会阻塞在 read 操作，造成线程资源浪费

<br/>



### Reactor 模式 

#### 针对传统阻塞 I/O 服务模型的 2 个缺点，解决方案

1. 基于 I/O 复用模型：多个连接共用一个阻塞对象，应用程序只需要在一个阻塞对象等待，无需阻塞等待所有连 接。当某个连接有新的数据可以处理时，操作系统通知应用程序，线程从阻塞状态返回，开始进行业务处理 Reactor 对应的叫法: 1. 反应器模式 2. 分发者模式(Dispatcher) 3. 通知者模式(notifier)

2. 基于线程池复用线程资源：不必再为每个连接创建线程，将连接完成后的业务处理任务分配给线程进行处理， 一个线程可以处理多个连接的业务

   ![](https://img-blog.csdnimg.cn/202007221817565.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



#### I/O 复用结合线程池，就是 Reactor 模式基本设计思想

![](https://img-blog.csdnimg.cn/20200722181851101.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



- Reactor 模式，通过一个或多个输入同时传递给服务处理器的模式(基于事件驱动) 

-  服务器端程序处理传入的多个请求,并将它们同步分派到相应的处理线程， 因此 Reactor 模式也叫 Dispatcher模式 

-  Reactor 模式使用 IO 复用监听事件, 收到事件后，分发给某个线程(进程), 这点就是网络服务器高并发处理关键



<br/>



#### Reactor 模式中 核心组成

- Reactor：Reactor 在一个单独的线程中运行，负责监听和分发事件，分发给适当的处理程序来对 IO 事件做出反应。 它就像公司的电话接线员，它接听来自客户的电话并将线路转移到适当的联系人

- Handlers：处理程序执行 I/O 事件要完成的实际事件，类似于客户想要与之交谈的公司中的实际官员。Reactor 通过调度适当的处理程序来响应 I/O 事件，处理程序执行非阻塞操作。



<br/>



#### Reactor 模式分类

&emsp;根据 Reactor 的数量和处理资源池线程的数量不同，有 3 种典型的实现

- 单 Reactor 单线程 
- 单 Reactor 多线程 
- 主从 Reactor 多线程

<br/>



### 单 Reactor 单线程

#### 介绍

![](https://img-blog.csdnimg.cn/20200722182303656.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



1. Select 是前面 I/O 复用模型介绍的标准网络编程 API，可以实现应用程序通过一个阻塞对象监听多路连接请求 
2. Reactor 对象通过 Select 监控客户端请求事件，收到事件后通过 Dispatch 进行分发 
3.  如果是建立连接请求事件，则由 Acceptor 通过 Accept 处理连接请求，然后创建一个 Handler 对象处理连接 完成后的后续业务处理 
4. 如果不是建立连接事件，则 Reactor 会分发调用连接对应的 Handler 来响应 
5. Handler 会完成 Read→业务处理→Send 的完整业务流程



> 结合实例：服务器端用一个线程通过多路复用搞定所有的 IO 操作（包括连接，读、写等），编码简单，清晰明了， 但是如果客户端连接数量较多，将无法支撑，前面的 NIO 案例就属于这种模型。



<br/>





#### 方案优缺点分析

- 优点：模型简单，没有多线程、进程通信、竞争的问题，全部都在一个线程中完成

- 缺点：

  - 性能问题，只有一个线程，无法完全发挥多核 CPU 的性能。Handler 在处理某个连接上的业务时，整个进程无法处理其他连接事件，很容易导致性能瓶颈 

  - 可靠性问题，线程意外终止，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障

- 使用场景：**客户端的数量有限，业务处理非常快速**，比如 Redis 在业务处理的时间复杂度 O(1) 的情况 

<br/>



#### 案例

##### 服务端

```java
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

        //5.将通道注册到选择器上,并且指定监听事件(这里是接受事件)
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        Handler handler=new Handler();

        //6.轮询式的获取选择器上已经”准备就绪“的事件
        while(selector.select()>0){
            //7.获取当前选择器中所有注册的“选择键(已就绪的监听事件)”
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while(it.hasNext()){
                //8.获取准备就绪的事件
                SelectionKey sk = it.next();

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
```



<br/>



##### 客户端

```java
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
```



&emsp;运行

![](https://img-blog.csdnimg.cn/20200722205153220.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>







### 单 Reactor 多线程

#### 介绍

![](https://img-blog.csdnimg.cn/20200722205441503.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



1. Reactor 对象通过 select 监控客户端请求 事件, 收到事件后，通过 dispatch 进行分发 
2. 如果建立连接请求, 则右 Acceptor 通过 accept 处理连接请求, 然后创建一个 Handler 对象处理完成连接后的各种事件 
3. 如果不是连接请求，则由 reactor 分发调用连接对应的 handler 来处理
4. handler 只负责响应事件，不做具体的业务处理, 通过 read 读取数据后，会分发给后面的 worker 线程池的某个 线程处理业务 
5. worker 线程池会分配独立线程完成真正的业务，并将结果返回给 handler
6. **handler 收到响应后，通过 send 将结果返回给 client**

<br/>



#### 方案优缺点分析

1. 优点：可以充分的利用多核 cpu 的处理能力

2. 缺点：多线程数据共享和访问比较复杂， reactor 处理所有的事件的监听和响应，在单线程运行， 在高并发场 

   景容易出现性能瓶颈

<br/>



### 主从 Reactor 多线程

#### 介绍

&emsp;针对单 Reactor 多线程模型中，Reactor 在单线程中运行，高并发场景下容易成为性能瓶颈，可以让 Reactor 在 多线程中运行

![](https://img-blog.csdnimg.cn/20200722221459618.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

1. Reactor 主线程 MainReactor 对象通过 select 监听连接事件, 收到事件后，通过 Acceptor 处理连接事件
2. 当 Acceptor 处理连接事件后，MainReactor 将连接分配给 SubReactor
3. subreactor 将连接加入到连接队列进行监听,并创建 handler 进行各种事件处理
4. 当有新事件发生时， subreactor 就会调用对应的 handler 处理
5. handler 收到响应后，通过 send 将结果返回给 client
6. handler 通过 read 读取数据，分发给后面的 worker 线程处理
7. worker 线程池分配独立的 worker 线程进行业务处理，并返回结果
8. handler 收到响应的结果后，再通过 send 将结果返回给 client
9. **Reactor 主线程可以对应多个 Reactor 子线程, 即 MainRecator 可以关联多个 SubReactor**

> 结合实例：这种模型在许多项目中广泛使用，包括 Nginx 主从 Reactor 多进程模型，Memcached 主从多线程，Netty 主从多线程模型的支持 



<br/>



#### 方案优缺点分析

1. 优点：
   - 父线程与子线程的数据交互简单职责明确，父线程只需要接收新连接，子线程完成后续的业务处理。
   - 父线程与子线程的数据交互简单，Reactor 主线程只需要把新连接传给子线程，子线程无需返回数据。
2. 缺点：编程复杂度较高

<br/>



#### Scalable IO in Java 对 Multiple Reactors 的原理图解

![](https://img-blog.csdnimg.cn/20200722230330793.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### Reactor 模式小结 

#### 3种模式用生活案例来理解

1. 单 Reactor 单线程，前台接待员和服务员是同一个人，全程为顾客服
2. 单 Reactor 多线程，1 个前台接待员，多个服务员，接待员只负责接待 
3. 主从 Reactor 多线程，多个前台接待员，多个服务生

<br/>



#### Reactor 模式具有如下的优点

1. 响应快，不必为单个同步时间所阻塞，虽然 Reactor 本身依然是同步的
2. 可以最大程度的避免复杂的多线程及同步问题，并且避免了多线程/进程的切换开销
3. 扩展性好，可以方便的通过增加 Reactor 实例个数来充分利用 CPU 资源 
4. 复用性好，Reactor 模型本身与具体事件处理逻辑无关，具有很高的复用性

<br/>



### Netty 模型 

#### 工作原理示意图-简单版

![](https://img-blog.csdnimg.cn/20200723000858397.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



- BossGroup 线程维护 Selector , 只关注 Accecpt 
-  当接收到 Accept 事件，获取到对应的 SocketChannel, 封装成 NIOScoketChannel 并注册到 Worker 线程(事件循 环), 并进行维护 

-  当 Worker 线程监听到 selector 中通道发生自己感兴趣的事件后，就进行处理(就由 handler)， 注意 handler 已 经加入到通道 

<br/>



#### 工作原理示意图-进阶版

![](https://img-blog.csdnimg.cn/20200723001236906.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



#### 工作原理示意图-详细版

![](https://img-blog.csdnimg.cn/20200723001548591.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



1. Netty 抽象出两组线程池 BossGroup 专门负责接收客户端的连接, WorkerGroup 专门负责网络的读写
2. BossGroup 和 WorkerGroup 类型都是 `NioEventLoopGroup`
3. NioEventLoopGroup 相当于一个事件循环组, 这个组中含有多个事件循环 ，即可以有多个线程，每一个事件循环是 NioEventLoop
4. NioEventLoop 表示一个不断循环的执行处理任务的线程， 每个 NioEventLoop 都有一个 selector , 用于监听绑 定在其上的 socket 的网络通讯
5. 每个 Boss NioEventLoop 循环执行的步骤有 3 步
   - 轮询 accept 事件
   - 处理 accept 事件 , 与 client 建立连接 , 生成 NioScocketChannel , 并将其注册到某个 worker NIOEventLoop 上 的 select
   - 处理任务队列的任务 ， 即 runAllTasks 

6. 每个 Worker NIOEventLoop 循环执行的步骤
   - 轮询 read, write 事件
   - 处理 i/o 事件， 即 read , write 事件，在对应 NioScocketChannel 处理 
   - 处理任务队列的任务 ， 即 runAllTasks

7. 每个Worker NIOEventLoop 处理业务时，会使用pipeline(管道), pipeline 中包含了 channel , 即通过pipeline可以获取到对应通道, 管道中维护了很多的 处理器

<br/>



### 说明

1. Netty 抽象出两组线程池，BossGroup 专门负责接收客户端连接，WorkerGroup 专门负责网络读写操作
2. NioEventLoop 表示一个不断循环执行处理任务的线程，每个 NioEventLoop 都有一个 selector，用于监听绑定 在其上的 socket 网络通道
3. NioEventLoop 内部采用串行化设计，`从消息的读取->解码->处理->编码->发送`，始终由 IO 线程 NioEventLoop 负责
4.  NioEventLoopGroup 下包含多个 NioEventLoop 
5. 每个 NioEventLoop 中包含有一个 Selector，一个 taskQueue
6. 每个 NioEventLoop 的 Selector 上可以注册监听多个 NioChannel
7. 每个 NioChannel 只会绑定在唯一的 NioEventLoop 上
8. 每个 NioChannel 都绑定有一个自己的 ChannelPipeline



<br/>







### Netty 快速入门实例-TCP 服务

#### server

```java
public class Server {
    public static void main(String[] args) throws IOException {
        //创建BossGroup和WorkerGroup
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap  bootstrap=new ServerBootstrap();

        try {
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup)  //设置两个线程
                .channel(NioServerSocketChannel.class)  //使用NioServerSocketChannel，作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列里连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true) //设置保持连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });  //给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器 is ready ...");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

```

<br/>



#### serverHandler

```java
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
    * @Description 读取数据事件(这里我们可以读取客户端发送的消息)
    * @date 2020/7/23 10:33
    * @param ctx  上下文对象，含有：管道(pipeline),通道(channel),地址
    * @param msg  客户端发送的数据
    * @return void
    */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //        System.out.println("服务器读取线程"+Thread.currentThread().getName());
        //        System.out.println("server ctx = "+ctx);
        //        System.out.println("看看channel和pipeline的关系");
        //        Channel channel = ctx.channel();
        //        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表
        //
        //        //将msg转为一个ByteBuf
        //        //ByteBuf是Netty提供的，不是NIO的ByteBuffer
        //        ByteBuf buf=(ByteBuf) msg;
        //        System.out.println("客户端发送消息："+buf.toString(CharsetUtil.UTF_8));
        //        System.out.println("客户端地址："+ctx.channel().remoteAddress());

        //比如我们有一个非常耗时的业务->异步执行->提交该channel对应的NioEventLoop的taskQueue中
        //        Thread.sleep(10000);
        //        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端",CharsetUtil.UTF_8));
        //        System.out.println("go on ...");

        //解决方案1 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端1",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端2",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        //解决方案二 用户自定义定时任务
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端3",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        },5, TimeUnit.SECONDS);

        System.out.println("go on ...");
    }
}

/**
    * @Description 数据读取完毕
    * @date 2020/7/23 10:46
    * @param ctx  
    * @return void
    */
@Override
public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    //将数据写入到缓冲并刷新
    //一般来讲，我们队发送的数据进行编码
    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端",CharsetUtil.UTF_8));
}

/**
    * @Description 处理异常，一般是需要管理通道
    * @date 2020/7/23 11:13
    * @param ctx
    * @param cause
    * @return void
    */
@Override
public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.channel().close();
}
}
```

<br/>



#### client

```java
public class Client {
    public static void main(String[] args) {
        //客户端需要一个事件循环组
        EventLoopGroup eventExecutors=new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            Bootstrap bootstrap=new Bootstrap();

            //设置相关参数
            bootstrap.group(eventExecutors) //设置线程组
                .channel(NioSocketChannel.class) //设置客户端通道的实现类（反射）
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());     //加入自己的处理器
                    }
                });
            System.out.println("客户端 ok...");

            //启动客户端去连接服务器端
            //关于ChannelFuture要分析，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 6668)).sync();

            //关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
```

<br/>



#### clientHandler

```java
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
    * @Description 当通道就绪时会触发该方法
    * @date 2020/7/23 11:27
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，server", CharsetUtil.UTF_8));
    }

    /**
    * @Description 当通道有读取事件时，会触发
    * @date 2020/7/23 11:29
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("服务器回复的消息："+((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }
}
```



<br/>



### 源码分析

&emsp;**bossGroup和workerGroup含有的子线程的个数(默认的NioEventLoop个数)，默认为cpu核数 * 2**

![](https://img-blog.csdnimg.cn/20200723114303321.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

![](https://img-blog.csdnimg.cn/2020072311443964.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



&emsp;当启动9个客户端时，看下线程的分配情况

```java
服务器 is ready ...
服务器读取线程nioEventLoopGroup-3-1
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x45510c71, L:/127.0.0.1:6668 - R:/127.0.0.1:62038])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62038
服务器读取线程nioEventLoopGroup-3-2
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x29926adb, L:/127.0.0.1:6668 - R:/127.0.0.1:62073])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62073
服务器读取线程nioEventLoopGroup-3-3
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x60a3c1f1, L:/127.0.0.1:6668 - R:/127.0.0.1:62129])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62129
服务器读取线程nioEventLoopGroup-3-4
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x2e2d621c, L:/127.0.0.1:6668 - R:/127.0.0.1:62154])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62154
服务器读取线程nioEventLoopGroup-3-5
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x172a53cc, L:/127.0.0.1:6668 - R:/127.0.0.1:62182])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62182
服务器读取线程nioEventLoopGroup-3-6
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x4cd7905b, L:/127.0.0.1:6668 - R:/127.0.0.1:62209])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62209
服务器读取线程nioEventLoopGroup-3-7
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x1cb0454b, L:/127.0.0.1:6668 - R:/127.0.0.1:62234])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62234
服务器读取线程nioEventLoopGroup-3-8
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x87c91e13, L:/127.0.0.1:6668 - R:/127.0.0.1:62261])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62261
服务器读取线程nioEventLoopGroup-3-1
server ctx = ChannelHandlerContext(ServerHandler#0, [id: 0x8709b039, L:/127.0.0.1:6668 - R:/127.0.0.1:62286])
客户端发送消息：hello，server
客户端地址：/127.0.0.1:62286
```



> 可以看到确实是采取轮流分配的方式来分配线程的

<br/>



&emsp;**我们可以自行设置**

```java
EventLoopGroup bossGroup=new NioEventLoopGroup(1);
EventLoopGroup workerGroup=new NioEventLoopGroup();
```

&emsp;此时再看

![](https://img-blog.csdnimg.cn/20200723115414838.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### 任务队列中的 Task 有 3 种典型使用场景 

1. 用户程序自定义的普通任务

2. 用户自定义定时任务

3. 非当前 Reactor 线程调用 Channel 的各种方法

   例如在推送系统的业务线程里面，根据用户的标识，找到对应的 Channel 引用，然后调用 Write 类方法向该用户推送消息，就会进入到这种场景。最终的 Write 会提交到任务队列中后被异步消费 

```java
Thread.sleep(10000);
ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端",CharsetUtil.UTF_8));
System.out.println("go on ...");
```

> 此时会一致阻塞在这里

<br/>



#### 用户程序自定义的普通任务

```java
//解决方案1 用户程序自定义的普通任务
ctx.channel().eventLoop().execute(new Runnable() {
    @Override
    public void run() {
        try {
            Thread.sleep(10*1000);
            ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端1",CharsetUtil.UTF_8));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
});

ctx.channel().eventLoop().execute(new Runnable() {
    @Override
    public void run() {
        try {
            Thread.sleep(20*1000);
            ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端2",CharsetUtil.UTF_8));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
});
System.out.println("go on ...");
```

> 此时任务队列中有两个任务(注意这两个任务事由一个线程来处理的)，第二个任务的运行时间是10+20秒

&emsp;运行

```java
客户端 ok...
clientChannelHandlerContext(ClientHandler#0, [id: 0x7028b530, L:/127.0.0.1:50197 - R:/127.0.0.1:6668])
服务器回复的消息：Thu Jul 23 15:16:08 CST 2020:hello,客户端
服务器的地址：/127.0.0.1:6668
服务器回复的消息：Thu Jul 23 15:16:18 CST 2020:hello 客户端1
服务器的地址：/127.0.0.1:6668
服务器回复的消息：Thu Jul 23 15:16:38 CST 2020:hello 客户端2
服务器的地址：/127.0.0.1:6668
```

<br/>





#### 用户自定义定时任务

```java
//解决方案二 用户自定义定时任务
ctx.channel().eventLoop().schedule(new Runnable() {
    @Override
    public void run() {
        try {
            Thread.sleep(5*1000);
            ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello 客户端3",CharsetUtil.UTF_8));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
},5, TimeUnit.SECONDS);
```

&emsp;运行

```java
客户端 ok...
clientChannelHandlerContext(ClientHandler#0, [id: 0xb33aedd3, L:/127.0.0.1:50405 - R:/127.0.0.1:6668])
服务器回复的消息：Thu Jul 23 15:25:05 CST 2020:hello,客户端
服务器的地址：/127.0.0.1:6668
服务器回复的消息：Thu Jul 23 15:25:06 CST 2020:hello 客户端1
服务器的地址：/127.0.0.1:6668
服务器回复的消息：Thu Jul 23 15:25:07 CST 2020:hello 客户端2
服务器的地址：/127.0.0.1:6668
服务器回复的消息：Thu Jul 23 15:25:15 CST 2020:hello 客户端3
服务器的地址：/127.0.0.1:6668
```

> 可以看到，定时任务和普通任务是区分开的



<br/>



#### 非当前 Reactor 线程调用 Channel 的各种方法



![](https://img-blog.csdnimg.cn/20200723153239977.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

> 我们可以在初始化处理器的时候，将channel放到一个集合中，然后再handler中可以处理相关业务。



<br/>



### 异步模型 

#### 介绍

1. 异步的概念和同步相对。当一个异步过程调用发出后，调用者不能立刻得到结果。实际处理这个调用的组件在 完成后，通过状态、通知和回调来通知调用者
2. Netty 中的 I/O 操作是异步的，包括 Bind、Write、Connect 等操作会简单的`返回一个 ChannelFuture`。
3. 调用者并不能立刻获得结果，而是通过 Future-Listener 机制，用户可以方便的主动获取或者通过通知机制获得 IO 操作结果 
4. Netty 的异步模型是建立在 future 和 callback 的之上的。callback 就是回调。重点说 Future，它的核心思想 是：假设一个方法 fun，计算过程可能非常耗时，等待 fun 返回显然不合适。那么可以在调用 fun 的时候，立 马返回一个Future，后续可以通过 Future 去监控方法 fun 的处理过程(即 ： Future-Listener 机制) 

<br/>



#### Future 说明

1. 表示**异步的执行结果**, 可以通过它提供的方法来检测执行是否完成，比如检索计算等等
2. ChannelFuture 是一个接口 ： public interface ChannelFuture extends Future<Void> 我们可以添**加监听器，当监听的事件发生时，就会通知到监听器**

<br/>



#### 工作原理示意图

![](https://img-blog.csdnimg.cn/20200723170207971.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>

1. 在使用 Netty 进行编程时，拦截操作和转换出入站数据只需要您提供 callback 或利用 future 即可。这使得链 式操作简单、高效, 并有利于编写可重用的、通用的代码。

2. Netty 框架的目标就是让你的业务逻辑从网络基础应用编码中分离出来、解脱出来

<br/>



#### Future-Listener 机制

1. 当 Future 对象刚刚创建时，处于非完成状态，调用者可以通过返回的 ChannelFuture 来获取操作执行的状态， 注册监听函数来执行完成后的操作。 
2. 常见有如下操作
   - 通过 isDone 方法来判断当前操作是否完成
   - 通过 isSuccess 方法来判断已完成的当前操作是否成功
   - 通过 getCause 方法来获取已完成的当前操作失败的原因
   - 通过 isCancelled 方法来判断已完成的当前操作是否被取消
   - 通过 addListener 方法来注册监听器，当操作已完成(isDone 方法返回完成)，将会通知指定的监听器；如果Future 对象已完成，则通知指定的监听器 

<br/>



#### 例子

```java
//绑定一个端口并且同步，生成一个ChannelFuture对象
ChannelFuture cf = bootstrap.bind(6668).sync();

//给 cf 注册监听器，监控我们关心的事件
cf.addListener(new ChannelFutureListener() {
    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(future.isSuccess()){
            System.out.println("监听端口成功");
        }else{
            System.out.println("监听端口失败");
        }
    }
});
```



&emsp;运行

```java
服务器 is ready ...
监听端口成功
```



<br/>





### 快速入门实例-HTTP 服务

&emsp;目的：Netty 可以做 Http 服务开发，并且理解 Handler 实例和客户端及其请求的关系

#### server

```java
public class Server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(40000)).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
```

<br/>



#### initializer

```java
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器


        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty提供的httpServerCodec
        //netty提供的处理http的编码解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        //增加一个自定义handler
        pipeline.addLast("HttpServerHandler",new HttpServerHandler());

    }
}
```

<br/>



#### serverHadndler

```java
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
    * @Description 读取客户端的数据
    * @date 2020/7/23 17:21
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是httpRequest请求
        if(msg instanceof HttpRequest){
            System.out.println("msg类型："+msg.getClass());
            System.out.println("客户端地址："+ctx.channel().remoteAddress());
            System.out.println("pipeline："+ctx.pipeline().hashCode()+"  channel："+ctx.channel().hashCode());

            //获取到
            HttpRequest httpRequest=(HttpRequest) msg;
            //获取uri
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 /favicon.ico ,不做响应");
                return;
            }

            //回复信息给浏览器
            ByteBuf buf= Unpooled.copiedBuffer("hello，我是服务器", CharsetUtil.UTF_8);

            //构造相应的http的相应，即httpresponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,buf.readableBytes());

            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
```

![](https://img-blog.csdnimg.cn/20200723175450191.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



&emsp;后台输出

```java
msg类型：class io.netty.handler.codec.http.DefaultHttpRequest
客户端地址：/0:0:0:0:0:0:0:1:54190
pipeline：1483512378  channel：1336530724
msg类型：class io.netty.handler.codec.http.DefaultHttpRequest
客户端地址：/0:0:0:0:0:0:0:1:54190
pipeline：1483512378  channel：1336530724
请求了 /favicon.ico ,不做响应
    
msg类型：class io.netty.handler.codec.http.DefaultHttpRequest
客户端地址：/0:0:0:0:0:0:0:1:54194
pipeline：1928055277  channel：-2011214508
msg类型：class io.netty.handler.codec.http.DefaultHttpRequest
客户端地址：/0:0:0:0:0:0:0:1:54194
pipeline：1928055277  channel：-2011214508
请求了 /favicon.ico ,不做响应
```



> **因为http是短连接，每次访问的时候会创建新的pipeline和channel**



<br/>



<br/>



## Netty 核心模块组件

### Bootstrap、ServerBootstrap

1. Bootstrap 意思是引导，一个 Netty 应用通常由一个 Bootstrap 开始，主要作用是配置整个 Netty 程序，串联 各个组件，Netty 中 Bootstrap 类是客户端程序的启动引导类，ServerBootstrap 是服务端启动引导类

2. 常见的方法有

   - public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)

     该方法用于服务器端， 用来设置两个 EventLoop 

   - public B group(EventLoopGroup group)

     该方法用于客户端，用来设置一个 EventLoop

   - public B channel(Class<? extends C> channelClass)

     该方法用来设置一个服务器端的通道实现

   - public <T> B option(ChannelOption<T> option, T value)

     用来给 ServerChannel 添加配置

   - public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value)

     用来给接收到的通道添加配置

   - public B handler(ChannelHandler handler) 

     在客户端连接前的请求进行handler处理，handler()是发生在**初始化的时候**

   - public ServerBootstrap childHandler(ChannelHandler childHandler)

     该方法用来设置业务处理类（自定义的handler），处理客户端连接之后的handler，childHandler()是发生在**客户端连接之后**

   - public ChannelFuture bind(int inetPort)

     该方法用于服务器端，用来设置占用的端口号

   - public ChannelFuture connect(String inetHost, int inetPort)

     该方法用于客户端，用来连接服务器端

<br/>



### Future、ChannelFuture

&emsp;Netty 中所有的 IO 操作都是异步的，不能立刻得知消息是否被正确处理。但是可以过一会等它执行完成或 者直接注册一个监听，具体的实现就是通过 Future 和 ChannelFutures，他们可以注册一个监听，**当操作执行成功 或失败时监听会自动触发注册的监听事件 **

&emsp;常见的方法有

- Channel channel() 返回当前正在进行 IO 操作的通道
- ChannelFuture sync()  等待异步操作执行完毕



<br/>



### Channel

1. Netty 网络通信的组件，能够用于执行网络 I/O 操作
2. 通过 Channel 可获得当前网络连接的通道的状态
3. 通过 Channel 可获得 网络连接的配置参数 （例如接收缓冲区大小）
4. Channel 提供异步的网络 I/O 操作(如建立连接，读写，绑定端口)，异步调用意味着任何 I/O 调用都将立即返 回，并且不保证在调用结束时所请求的 I/O 操作已完成 
5. 调用立即返回一个 ChannelFuture 实例，通过注册监听器到 ChannelFuture 上，可以 I/O 操作成功、失败或取 消时回调通知调用方 
6. 支持关联 I/O 操作与对应的处理程序 
7. 不同协议、不同的阻塞类型的连接都有不同的 Channel 类型与之对应，常用的 Channel 类型:
   - NioSocketChannel，异步的客户端 TCP Socket 连接
   - NioServerSocketChannel，异步的服务器端 TCP Socket 连接
   - NioDatagramChannel，异步的 UDP 连接
   - NioSctpChannel，异步的客户端 Sctp 连接
   - NioSctpServerChannel，异步的 Sctp 服务器端连接，这些通道涵盖了 UDP 和 TCP 网络 IO 以及文件 IO

<br/>



### Selector

1. Netty 基于 Selector 对象实现 I/O 多路复用，通过 Selector 一个线程可以监听多个连接的 Channel 事件
2. 当向一个 Selector 中注册 Channel 后，Selector 内部的机制就可以自动不断地查询(Select) 这些注册的 Channel 是否有已就绪的 I/O 事件（例如可读，可写，网络连接完成等），这样程序就可以很简单地使用一个 线程高效地管理多个 Channel



<br/>



### ChannelHandler 及其实现类

1. ChannelHandler 是一个接口，处理 I/O 事件或拦截 I/O 操作，并将其转发到其 ChannelPipeline(业务处理链) 中的下一个处理程序
2. ChannelHandler 本身并没有提供很多方法，因为这个接口有许多的方法需要实现，方便使用期间，可以继承它 的子类 
3. ChannelHandler 及其实现类一览图![](https://img-blog.csdnimg.cn/20200723183929246.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

4. 我们经常需要自定义一个 Handler 类去继承 ChannelInboundHandlerAdapter，然后通过重写相应方法实现业务 逻辑，我们接下来看看一般都需要重写哪些方法

   ```java
   public class ChannelInboundHandlerAdapter extends ChannelHandlerAdapter implements ChannelInboundHandler {
       public ChannelInboundHandlerAdapter() { 
       }    
       public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelRegistered();
       }
       public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelUnregistered();
       }
       //通道就绪事件
       public void channelActive(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelActive();
       }
       public void channelInactive(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelInactive();
       }
       //通道读取数据事件
       public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {              		  ctx.fireChannelRead(msg); 
       }
       //数据读取完毕事件
       public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
           ctx.fireChannelReadComplete();
       }
       public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
           ctx.fireUserEventTriggered(evt);
       }
       public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {        		    ctx.fireChannelWritabilityChanged();
       }
       //通道发生异常事件
       public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
           ctx.fireExceptionCaught(cause);
       }
   }
   ```



<br/>



### Pipeline 和 ChannelPipeline

1. ChannelPipeline 是一个 Handler 的集合，它负责处理和拦截 inbound 或者 outbound 的事件和操作，相当于一个贯穿 Netty 的链。(也可以这样理解：ChannelPipeline 是 保存 ChannelHandler 的 List，用于处理或拦截 Channel 的入站事件和出站操作)

2. ChannelPipeline 是一个 Handler 的集合，它负责处理和拦截 inbound 或者 outbound 的事件和操作，相当于一个贯穿 Netty 的链。(也可以这样理解：ChannelPipeline 是 保存 ChannelHandler 的 List，用于处理或拦截 Channel 的入站事件和出站操作)

3. 在 Netty 中每个 Channel 都有且仅有一个 ChannelPipeline 与之对应，它们的组成关系如下

   ![](https://img-blog.csdnimg.cn/20200723202459385.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

   - **一个 Channel 包含了一个 ChannelPipeline，而 ChannelPipeline 中又维护了一个由 ChannelHandlerContext 组成的双向链表，这个链表的头是HeadContext,链表的尾是TailContext，并且每个 ChannelHandlerContext 中又关联着一个 ChannelHandler**
   - 入站事件和出站事件在一个双向链表中，入站事件会从链表 head 往后传递到最后一个入站的 handler，出站事件会从链表 tail 往前传递到最前一个出站的 handler，两种类型的 handler 互不干扰

<br/>



4. 常用方法

   - ChannelPipeline addFirst(ChannelHandler... handlers)，把一个业务处理类（handler）添加到链中的第一个位置
   - ChannelPipeline addLast(ChannelHandler... handlers)，把一个业务处理类（handler）添加到链中的最后一个位置

   

   

   <br/>

   

### ChannelHandlerContext

1. 保存 Channel 相关的所有上下文信息，同时关联一个 ChannelHandler 对象
2. 即 ChannelHandlerContext 中 包 含 一 个 具 体 的 事 件 处 理 器 ChannelHandler ， 同 时ChannelHandlerContext 中也绑定了对应的 pipeline 和 Channel 的信息，方便对 ChannelHandler 进行调用
3. 常用方法
   - ChannelFuture close()，关闭通道
   - ChannelOutboundInvoker flush()，刷新
   - ChannelFuture writeAndFlush(Object msg) ， 将 数 据 写 到 ChannelPipeline 中 当 前
   - ChannelHandler 的下一个 ChannelHandler 开始处理（出站）



<br/>



### ChannelOption

1. Netty 在创建 Channel 实例后,一般都需要设置 ChannelOption 参数

2. ChannelOption 参数如下

   - ChannelOption.SO_BACKLOG

     描述：对应 TCP/IP 协议 listen 函数中的 backlog 参数，用来初始化服务器可连接队列大小。服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接。多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog 参数指定了队列的大小。

   - ChannelOption.SO_KEEPALIVE

     描述：一直保持连接活动状态
   
   - ChannelOption.TCP_NODELAY
   
     描述：没有延迟



<br/>





### EventLoopGroup 和其实现类 NioEventLoopGroup

1. EventLoopGroup 是一组 EventLoop 的抽象，Netty 为了更好的利用多核 CPU 资源，一般会有多个 EventLoop 同时工作，每个 EventLoop 维护着一个 Selector 实例。

2. EventLoopGroup 提供 next 接口，可以从组里面按照一定规则获取其中一个 EventLoop来处理任务。在 Netty 服务器端编程中，我们一般都需要提供两个 EventLoopGroup，例如：BossEventLoopGroup 和 WorkerEventLoopGroup。

3. 通常一个服务端口即一个 ServerSocketChannel对应一个Selector 和一个EventLoop线程。BossEventLoop 负责接收客户端的连接并将 SocketChannel 交给 WorkerEventLoopGroup 来进行 IO 处理，如下图所示

   ![](https://img-blog.csdnimg.cn/20200723222123356.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

- BossEventLoopGroup 通常是一个单线程的 EventLoop，EventLoop 维护着一个注册了ServerSocketChannel 的Selector 实例BossEventLoop 不断轮询 Selector 将连接事件分离出来
- 通常是 OP_ACCEPT 事件，然后将接收到的 SocketChannel 交给 WorkerEventLoopGroup
- WorkerEventLoopGroup 会由 next 选择其中一个 EventLoop来将这个 SocketChannel 注册到其维护的 Selector 并对其后续的 IO 事件进行处理

4. 常用方法
   - public NioEventLoopGroup()，构造方法
   - public Future<?> shutdownGracefully()，断开连接，关闭线程



<br/>



### Unpooled 类

1. Netty 提供一个专门用来操作缓冲区(即Netty的数据容器)的工具类

2. 常用方法如下所示

   - public static ByteBuf copiedBuffer(CharSequence string, Charset charset)

     通过给定的数据和字符编码返回一个 ByteBuf 对象（类似于 NIO 中的 ByteBuffer 但有区别）

3. 举例说明Unpooled 获取 Netty的数据容器ByteBuf 的基本使用

   ```java
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
   }
   ```

   

![](https://img-blog.csdnimg.cn/20200723222529355.png)



&emsp;debug运行一下

![](https://img-blog.csdnimg.cn/20200723221538151.gif)

> writerIndex会增加。使用getByte(i)，readerIndex不会增加，替换成
>
> ```java
> System.out.println(buffer.readByte());
> ```
>
> 每读一次readerIndex就会增加1





```java
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
```



&emsp;运行

```java
hello,world!                                                    
hello,world!
0
0
12
64
----------------
12
0：104   1：101   2：108   3：108   4：111   5：44   6：119   7：111   8：114   9：108   10：100   11：33   
从第0个开始，读取4个：hell
从第4个开始，读取6个：o,worl
```

<br/>



### 群聊系统

> 编写一个 Netty 群聊系统，实现服务器端和客户端之间的数据简单通讯（非阻塞）

#### server

```java
public class Server {
    /**
     * 监听端口
     */
    private int port;

    public Server(int port){
        this.port=port;
    }

    //编写run方法处理客户端的请求
    public void run(){
        //创建两个线程组
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //获取到pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        //向pipeline加入解码器
                        pipeline.addLast("decoder",new StringDecoder());
                        //向pipeline加入编码器
                        pipeline.addLast("encoder",new StringEncoder());
                        //加入自己的业务处理handler
                        pipeline.addLast(new ServerHandler());
                    }
                });

            System.out.println("服务器启动");
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();

            //监听关闭事件
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Server(50000).run();
    }
}
```

<br/>



#### serverHandler

```java
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 使用一个hashMap管理
     */
    public static Map<User,Channel> channels=new ConcurrentHashMap<>();

    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor.INSTANCE 是全局的时间执行器，是一个单利
     */
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private  DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
    * @Description 表示连接建立，一旦连接，第一个被执行
     *              将当前channel接入到channelGroup
    * @date 2020/7/23 23:11
    * @param ctx
    * @return void
    */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
        //将该客户端加入聊天的信息推送给其它在线的客户端
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天\n");

        //这里可以私聊
        channels.put(new User(123456L),channel);
    }

    /**
    * @Description 表示channel处于活跃状态，提示xxx上线
    * @date 2020/7/23 23:18
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线");
    }


    /**
    * @Description 读取数据
    * @date 2020/7/24 0:15
    * @param ctx  
    * @param msg  
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取当前channel
        Channel channel = ctx.channel();

        //这里我们遍历一下,自己不需要获取自己的消息
        for (Channel ch : channelGroup) {
            if(ch!=channel) {
                ch.writeAndFlush("[客户]"+ch.remoteAddress()+ LocalDateTime.now().format(formatter) +"发送消息："+msg+"\n");
            }else{
                ch.writeAndFlush(LocalDateTime.now().format(formatter)+"发送了消息："+msg+"[自己]\n");
            }
        }
    }

    /**
    * @Description 处理异常
    * @date 2020/7/23 23:29
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭
        ctx.close();
    }

    /**
    * @Description 表示channel处于非活跃状态，提示xxx离线
    * @date 2020/7/23 23:21
    * @param ctx
    * @return void
    */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+LocalDateTime.now().format(formatter) +"离线");
    }

    /**
    * @Description 断开连接，将xx客户端离开信息推送给当前在线的客户
    * @date 2020/7/23 23:22
    * @param ctx
    * @return void
    */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+LocalDateTime.now().format(formatter) +"离开聊天\n");
        System.out.println("channelGroup的大小："+channelGroup.size());
    }
}
```



#### client

```java
public class Client {
    private final String host;
    private final  int port;

    public Client(String host,int port){
        this.host=host;
        this.port=port;
    }

    public void run(){
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder",new StringDecoder());
                        pipeline.addLast("encoder",new StringEncoder());
                        pipeline.addLast("handler",new ClientHandler());
                    }
                });
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port)).sync();

            Channel channel = channelFuture.channel();
            System.out.println("--------"+channel.localAddress()+"-----------");

            //客户端需要输入信息，创建一个扫描器
            Scanner scanner=new Scanner(System.in);
            while(scanner.hasNext()){
                String str=scanner.nextLine();
                //通过channel发送到服务器
                channel.writeAndFlush(str);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventExecutors.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Client("127.0.0.1",50000).run();
    }
}
```

<br/>



#### clientHandler

```java
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    /**
    * @Description 读取信息
    * @date 2020/7/23 23:47
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg.trim());
    }
}
```

<br/>



#### 测试

![](https://img-blog.csdnimg.cn/20200724001757748.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### Netty 心跳检测机制案例

- 编写一个 Netty 心跳检测机制案例, 当服务器超过 3 秒没有读时，就提示读空闲 
- 当服务器超过 5 秒没有写操作时，就提示写空闲
- 实现当服务器超过 7 秒没有读或者写操作时，就提示读写空闲

```java
public class Server {
    public static void main(String[] args) {
        //创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //加入一个netty提供的idleStateHandler
                        /*
                                    说明
                                        1.idleStateHandler 是netty 提供的处理空闲状态的处理器
                                        2.long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包，检测是否连接
                                        3.long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包，检测是否连接
                                        4.long allIdleTime:表示多长时间既没有读也没有写，就会发送一个心跳检测包，检测是否连接
                                        5.当IdealStateEvent触发后，就会传递给管道的下一个handler去处理,
                                          通过调用(触发)下一个handler的userEventTriggered()，在该方法中去处理
                                     */
                        pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                        //加入一个对空闲检测进一步处理的handler(自定义)
                        pipeline.addLast(new ServerHandler());

                    }
                });

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(50000)).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```



```java
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
    * @Description 事件触发器
    * @date 2020/7/24 0:51
    * @param ctx
    * @param evt
    * @return void
    */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event=(IdleStateEvent)evt;

            String eventType=null;
            switch (event.state()){
                case READER_IDLE: eventType="读空闲";break;
                case WRITER_IDLE: eventType="写空闲";break;
                case  ALL_IDLE  : eventType="读写空闲";break;
            }
            System.out.println(ctx.channel().remoteAddress()+"--超时事件--"+eventType);
            System.out.println("服务器做相应处理...");
        }
    }
}
```



<br/>





>1. idleStateHandler 是netty 提供的处理空闲状态的处理器
>2. long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包，检测是否连接
>3. long writerIdleTime：表示多长时间没有写，就会发送一个心跳检测包，检测是否连接
>4. long allIdleTime:表示多长时间既没有读也没有写，就会发送一个心跳检测包，检测是否连接
>5. 当IdealStateEvent触发后，就会传递给管道的下一个handler去处理,通过调用(触发)下一个handler的userEventTriggered()，在该方法中去处理





<br/>



### WebSocket 编程实现服务器和客户端长连接

1. Http 协议是无状态的, 浏览器和服务器间的请求响应一次，下一次会重新创建连接
2. 要求：实现基于 webSocket 的长连接的全双工的交互
3. 改变 Http 协议多次请求的约束，实现长连接了， 服务器可以发送消息给浏览器
4.  客户端浏览器和服务器端会相互感知，比如服务器关闭了，浏览器会感知，同样浏览器关闭了，服务器会感知



#### server

```java
public class server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        //基于http协议，使用http的编码和解码器
                        pipeline.addLast(new HttpServerCodec());
                        //是以块的方式写，添加ChunkedWriteHandler处理器
                        pipeline.addLast(new ChunkedWriteHandler());

                        /*
                                     说明：
                                        1. HTTP数据在传输过程中分段，HttpObjectAggregator,就是可以将多个段聚合
                                        2. 这就是为什么，当浏览器发送大量数据时，就是发出多次http请求
                                     */
                        pipeline.addLast(new HttpObjectAggregator(1024*8));

                        /*
                                    说明：
                                        1. 对应websocket，它的数据是以 帧(frame) 形式传递
                                        2. 可以看成websocketFrame有下面有六个子类
                                        3. 浏览器请求时：ws://localhost:50000/xxx
                                        4. WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                                     */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                        //自定义的handler，处理业务逻辑
                        pipeline.addLast(new ServerHandler());
                    }
                });
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(50000)).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```

<br/>



> 当我们用POST方式请求服务器的时候，对应的参数信息是保存在message  body中的,如果只是单纯的用HttpServerCodec是无法完全的解析Http  POST请求的，因为HttpServerCodec只能获取uri中参数，所以需要加上HttpObjectAggregator。

<br/>



#### serverHandler

```java
/**
 * @author codekiller
 * @date 2020/7/24 13:56
 * @Description TextWebSocketFrame类型，表示一个文本帧
 */
public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * @Description 当web客户端连接后，触发方法
     * @date 2020/7/24 14:03
     * @param ctx
     * @return void
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id 表示唯一的一个值，LongText是唯一的,ShortText不是唯一的
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asShortText());

    }

    /**
    * @Description 读取数据
    * @date 2020/7/24 13:59
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务端收到消息："+msg.text());

        //回复客户端
        Channel channel = ctx.channel();
        channel.writeAndFlush(new TextWebSocketFrame("服务器时间："+ LocalDateTime.now()+" "+msg.text()));
    }

    /**
    * @Description 异常处理
    * @date 2020/7/24 14:06
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 "+cause.getMessage());
        ctx.close();
    }

    /**
    * @Description 当web客户端断开连接后，触发方法
    * @date 2020/7/24 14:05
    * @param ctx
    * @return void
    */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asShortText());
    }
}
```



<br/>



#### 界面

```html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>hello</title>
    </head>
    <body>
        <form onsubmit="return false">
            <textarea id="message" name="message" style="height: 300px;width: 300px"></textarea>
            <input type="button" value="发送消息" onclick="send(this.form.message.value)">
            <textarea id="responseText" style="height: 300px;width: 300px"></textarea>
            <input type="button" value="清空内容" onclick="document.getElementById('responseText').value = ''">
        </form>

    </body>
    <script>
        let socket;
        //判断当前浏览器是否支持webSocket编程
        if(window.WebSocket){
            socket =new WebSocket("ws://localhost:50000/hello");

            //ev接受服务端回送的消息
            socket.onmessage=(ev)=>{
                let rt = document.getElementById("responseText");
                rt.value=rt.value+"\n"+ev.data
            }

            //相当于连接开启(感知到连接开启)
            socket.onopen=(ev)=>{
                let rt = document.getElementById("responseText");
                rt.value="连接已开启..."
            }

            //感知到连接关闭
            socket.onclose=(ev)=>{
                let rt = document.getElementById("responseText");
                rt.value=rt.value+"\n"+"连接已关闭..."
            }
        }else{
            alert("当前浏览器不支持webSocket编程")
        }

        //发送消息到服务器
        function send(msg){
            // if(!window.socket){
            //     return;
            // }
            if(socket.readyState===WebSocket.OPEN){
                //通过socket发送消息
                socket.send(msg)
            }else{
                alert("连接没有开启")
            }
        }

    </script>
</html>
```



<br/>



#### 运行

&emsp;发送请求

![](https://img-blog.csdnimg.cn/20200724143431650.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>

&emsp;前台

![](https://img-blog.csdnimg.cn/20200724143740822.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



&emsp;后台

![](https://img-blog.csdnimg.cn/2020072414344833.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/><br/>



## Google Protobuf

### 编码和解码的基本介绍 

- 编写网络应用程序时，因为数据在网络中传输的都是二进制字节码数据，在发送数据时就需要编码，接收数据 时就需要解码
- codec(编解码器) 的组成部分有两个：decoder(解码器)和 encoder(编码器)。encoder 负责把业务数据转换成字节 码数据，decoder 负责把字节码数据转换成业务数据 

![](https://img-blog.csdnimg.cn/20200724164218197.png)



<br/>



### Netty 本身的编码解码的机制和问题分析

1. Netty 自身提供了一些 codec(编解码器)

2. Netty 提供的编码器 

   - StringEncoder，对字符串数据进行编码 

   - ObjectEncoder，对 Java 对象进行编码
   - ......

3. Netty 提供的解码器
   - StringDecoder, 对字符串数据进行解码
   - ObjectDecoder，对 Java 对象进行解码
   - ......
4. Netty 本身自带的 ObjectDecoder 和 ObjectEncoder 可以用来实现 POJO 对象或各种业务对象的编码和解码，尚硅谷 Netty 核心技术及源码剖析 **底层使用的仍是 Java 序列化技术** , 而 Java 序列化技术本身效率就不高，存在如下问题
   - 无法跨语言
   - 序列化后的体积太大，是二进制编码的 5 倍多
   - 序列化性能太低
5. 引出 新的解决方案 [Google 的 Protobuf] 

<br/>



### Protobuf

1. Protobuf 是 Google 发布的开源项目，全称 Google Protocol Buffers，是一种轻便高效的结构化数据存储格式，可以用于结构化数据串行化，或者说序列化。它很适合做数据存储或 RPC[远程过程调用 remote procedure call ] 数据交换格式 
2. **目前很多公司** 由**http+json** 转为了  **tcp+protobuf**
3. Protobuf 是以 message 的方式来管理数据的.
4. 支持跨平台、跨语言，即[客户端和服务器端可以是不同的语言编写的] （支持目前绝大多数语言，例如 C++、 C#、Java、python 等） 
5. 高性能，高可靠性 
6. 使用 protobuf 编译器能自动生成代码，Protobuf 是将类的定义使用.proto 文件进行描述。说明，在 idea 中编 写 .proto 文件时，会自动提示是否下载 .ptotot 编写插件. 可以让语法高亮。 
7.  然后通过 protoc.exe 编译器根据.proto 自动生成.java 文件 
8. protobuf 使用示意图

![](https://img-blog.csdnimg.cn/20200724164642249.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



> 参考文档 : https://developers.google.com/protocol-buffers/docs/proto

<br/>



### 单个pojo

1. 客户端可以发送一个 Student PoJo 对象到服务器 (通过 Protobuf 编码) 
2. 服务端能接收 Student PoJo 对象，并显示信息(通过 Protobuf 解码)

<br/>



#### server

```java
public class Server {
    public static void main(String[] args) throws IOException {
        //创建BossGroup和WorkerGroup
        //bossGroup和workerGroup含有的子线程的个数，默认为cpu核数*2
        EventLoopGroup bossGroup=new NioEventLoopGroup(5);
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap  bootstrap=new ServerBootstrap();

        try {
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup)  //设置两个线程
                .channel(NioServerSocketChannel.class)  //使用NioServerSocketChannel，作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列里连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {  //handler对应的是boosGruop，childHandler对应的是workerGroup
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        //加入解码器
                        //指定对哪种对象进行解码
                        pipeline.addLast("decoder",new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));

                        pipeline.addLast(new ServerHandler());
                    }
                });  //给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器 is ready ...");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给 cf 注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("监听端口成功");
                    }else{
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
```

<br/>



#### serverHandler

```java
public class ServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
        System.out.println("客户端发送的数据："+msg.getId()+"："+msg.getName());
    }

    /**
    * @Description 数据读取完毕
    * @date 2020/7/23 10:46
    * @param ctx  
    * @return void
    */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲并刷新
        //一般来讲，我们队发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello,客户端",CharsetUtil.UTF_8));
    }

    /**
    * @Description 处理异常，一般是需要管理通道
    * @date 2020/7/23 11:13
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}

```



<br/>



#### client

```java
public class Client {
    public static void main(String[] args) {
        //客户端需要一个事件循环组
        EventLoopGroup eventExecutors=new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            Bootstrap bootstrap=new Bootstrap();

            //设置相关参数
            bootstrap.group(eventExecutors) //设置线程组
                .channel(NioSocketChannel.class) //设置客户端通道的实现类（反射）
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        //加入编码器
                        pipeline.addLast("encoder",new ProtobufEncoder());
                        pipeline.addLast(new ClientHandler());     //加入自己的处理器
                    }
                });
            System.out.println("客户端 ok...");

            //启动客户端去连接服务器端
            //关于ChannelFuture要分析，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 6668)).sync();

            //关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
```

<br/>



#### clientHandler

```java
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
    * @Description 当通道就绪时会触发该方法
    * @date 2020/7/23 11:27
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("重庆刘德华,香港徐大虾").build();

        ctx.writeAndFlush(student);
    }

    /**
    * @Description 当通道有读取事件时，会触发
    * @date 2020/7/23 11:29
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("服务器回复的消息："+((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }
}

```

<br/>



#### Student.proto

```protobuf
syntax= "proto3";  //版本
option java_outer_classname="StudentPOJO"; //生成的外部类名
message Student{
    int32 id=1;
    string name=2;
}
```

<br/>





<span id="operate"></span>

#### 操作

![](https://img-blog.csdnimg.cn/20200724154328804.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



![](https://img-blog.csdnimg.cn/20200724154348186.png)

```java
protoc.exe --java_out=. Student.proto
```



![](https://img-blog.csdnimg.cn/20200724154426429.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



![](https://img-blog.csdnimg.cn/20200724165027204.png)

<br/>



#### 运行

![](https://img-blog.csdnimg.cn/20200724165152247.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### 多个pojo

#### server

```java
public class Server {
    public static void main(String[] args) throws IOException {
        //创建BossGroup和WorkerGroup
        //bossGroup和workerGroup含有的子线程的个数，默认为cpu核数*2
        EventLoopGroup bossGroup=new NioEventLoopGroup(5);
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap  bootstrap=new ServerBootstrap();

        try {
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup)  //设置两个线程
                .channel(NioServerSocketChannel.class)  //使用NioServerSocketChannel，作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列里连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {  //handler对应的是boosGruop，childHandler对应的是workerGroup
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        //加入解码器
                        //指定对哪种对象进行解码
                        pipeline.addLast("decoder",new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));


                        pipeline.addLast(new ServerHandler());
                    }
                });  //给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器 is ready ...");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给 cf 注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("监听端口成功");
                    }else{
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
```

<br/>



#### serverHandler

```java
public class ServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        //根据dataType，显示不同的信息
        MyDataInfo.MyMessage.DateType dateType=msg.getDateType();

        if(dateType==MyDataInfo.MyMessage.DateType.StudentType){
            MyDataInfo.Student student=msg.getStudent();
            System.out.println("学生的信息:"+student.getId()+"："+student.getName());
        }else if(dateType==MyDataInfo.MyMessage.DateType.WorkerType){
            MyDataInfo.Worker worker=msg.getWorker();
            System.out.println("工人的信息："+worker.getName()+"："+worker.getAge());
        }else{
            System.out.println("传输的类型不正确");
        }
    }

    /**
    * @Description 数据读取完毕
    * @date 2020/7/23 10:46
    * @param ctx  
    * @return void
    */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲并刷新
        //一般来讲，我们队发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello,客户端",CharsetUtil.UTF_8));
    }

    /**
    * @Description 处理异常，一般是需要管理通道
    * @date 2020/7/23 11:13
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
```

<br/>



#### client

```java
public class Client {
    public static void main(String[] args) {
        //客户端需要一个事件循环组
        EventLoopGroup eventExecutors=new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            Bootstrap bootstrap=new Bootstrap();

            //设置相关参数
            bootstrap.group(eventExecutors) //设置线程组
                .channel(NioSocketChannel.class) //设置客户端通道的实现类（反射）
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        //加入编码器
                        pipeline.addLast("encoder",new ProtobufEncoder());
                        pipeline.addLast(new ClientHandler());     //加入自己的处理器
                    }
                });
            System.out.println("客户端 ok...");

            //启动客户端去连接服务器端
            //关于ChannelFuture要分析，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 6668)).sync();

            //关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
```

<br/>



#### clientHandler

```java
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
    * @Description 当通道就绪时会触发该方法
    * @date 2020/7/23 11:27
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;
        System.out.println(random);
        if(random==0){
            myMessage=MyDataInfo.MyMessage.newBuilder().setDateType(MyDataInfo.MyMessage.DateType.StudentType)
                .setStudent(MyDataInfo.Student.newBuilder().setName("重庆刘德华").setId(2).build()).build();
        }else{
            myMessage=MyDataInfo.MyMessage.newBuilder().setDateType(MyDataInfo.MyMessage.DateType.WorkerType)
                .setWorker(MyDataInfo.Worker.newBuilder().setName("香港徐大虾").setAge(21).build()).build();
        }


        ctx.writeAndFlush(myMessage);
    }

    /**
    * @Description 当通道有读取事件时，会触发
    * @date 2020/7/23 11:29
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("服务器回复的消息："+((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }
}
```

<br/>



#### Message.proto

```protobuf
syntax = "proto3";
option optimize_for= SPEED;  //快速解析
option java_package="top.codekiller.test.nettty.codec2"; //指定生成到哪个包下
option java_outer_classname="MyDataInfo";  //外部类名称

//protobuf 可以使用message管理其它的message
message MyMessage{
    //定义一个枚举
    enum DateType{
        StudentType=0;  //在proto3 要求enum的编号从0开始
        WorkerType=1;
    }

    //用data_type 来标识传的是哪一个枚举类型
    DateType date_type=1;

    //表示每次枚举类型最多只能出现其中的一个，节省空间
    oneof dataBody{
        Student student=2;
        Worker worker=3;
    }
}

message Student{
    int32 id=1;
    string name=2;
}

message Worker{
    string name=1;
    int32 age=2;
}
```



<br/>



#### 操作

&emsp;:point_right: [查看操作](#operate)



<br/>



#### 运行

![](https://img-blog.csdnimg.cn/20200724163924361.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>





<br/>



## Netty 编解码器和 handler 的调用机制

### 基本说明

1. netty 的组件设计：Netty 的主要组件有 Channel、EventLoop、ChannelFuture、ChannelHandler、ChannelPipe 等
2. ChannelHandler 充当了处理入站和出站数据的应用程序逻辑的容器。例如，实现 ChannelInboundHandler 接口（或 ChannelInboundHandlerAdapter），你就可以接收入站事件和数据，这些数据会被业务逻辑处理。当要给客户端 发送 响 应 时 ， 也 可 以 从 ChannelInboundHandler 冲 刷 数 据 。 业 务 逻 辑 通 常 写 在 一 个 或 者 多 个 ChannelInboundHandler 中。ChannelOutboundHandler 原理一样，只不过它是用来处理出站数据的 
3. ChannelPipeline 提供了 ChannelHandler 链的容器。以客户端应用程序为例，如果事件的运动方向是从客户端到 服务端的，那么我们称这些事件为出站的，即客户端发送给服务端的数据会通过 pipeline 中的一系列 ChannelOutboundHandler，并被这些 Handler 处理，反之则称为入站的 

![](https://img-blog.csdnimg.cn/20200724204021510.png)

<br/>



### 编码解码器 

1. 当 Netty 发送或者接受一个消息的时候，就将会发生一次数据转换。入站消息会被解码：从字节转换为另一种 格式（比如 java 对象）；如果是出站消息，它会被编码成字节
2. Netty 提供一系列实用的编解码器，他们都实现了 ChannelInboundHadnler 或者 ChannelOutboundHandler 接口。 在这些类中，channelRead 方法已经被重写了。以入站为例，对于每个从入站 Channel 读取的消息，这个方法会 被调用。随后，它将调用由解码器所提供的 decode()方法进行解码，并将已经解码的字节转发给 ChannelPipeline 中的下一个 ChannelInboundHandler。
3. **不论解码器handler 还是 编码器handler 即接收的消息类型必须与待处理的消息类型一致，否则该handler不会被执行**
4. **在解码器 进行数据解码时，需要判断 缓存区(ByteBuf)的数据是否足够 ，否则接收到的结果会期望结果可能不一致**



<br/>



### 解码器-ByteToMessageDecoder

1. 关系继承图

   ![](https://img-blog.csdnimg.cn/2020072420442919.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



2. 由于不可能知道远程节点是否会一次性发送一个完整的信息，tcp 有可能出现粘包拆包的问题，这个类会对入 站数据进行缓冲，直到它准备好被处理

3. 一个关于 ByteToMessageDecoder 实例分析

   ```java
   public class ToIntegerDecoder extends ByteToMessageDecoder {
       @Override
       protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
           if (in.readableBytes() >= 4) {
               out.add(in.readInt());
           }
       }
   }
   ```

   1. 这个例子，每次入站从ByteBuf中读取4字节，将其解码为一个int，然后将它添加到下一个List中。当没有更多元素可以被添加到该List中时，它的内容将会被发送给下一个ChannelInboundHandler。int在被添加到List中时，会被自动装箱为Integer。在调用readInt()方法前必须验证所输入的ByteBuf是否具有足够的数据

   2. decode 执行分析图

      ![](https://img-blog.csdnimg.cn/20200724204920517.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



### Netty 的 handler 链的调用机制

&emsp;使用自定义的编码器和解码器来说明 Netty 的 handler 调用机制

- 客户端发送 long -> 服务器

- 服务端发送 long -> 客户端

  ![](https://img-blog.csdnimg.cn/20200724205323749.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### 案例

#### server

```java
public class Server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(50000)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
```





<br/>



#### serverInitializer

```java
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //入站的handler进行解码
        pipeline.addLast(new ByteToLongDecoder2());

        //加入一个出站的handler，对数据进行编码
        pipeline.addLast(new LongToByteEncoder());

        //自定义handler
        pipeline.addLast(new ServerHandler());
    }
}
```

<br/>



#### serverHandler

```java
public class ServerHandler extends SimpleChannelInboundHandler<Long> {
    /**
    * @Description 读取数据
    * @date 2020/7/24 17:56
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("从客户端"+ctx.channel().remoteAddress()+"读到的Long数据："+msg);

        //给客户端发送一个long
        ctx.writeAndFlush(987654L);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常"+cause.getMessage()+cause.getCause()+cause.getStackTrace());
        ctx.close();
    }
}
```

<br/>



#### client

```java
public class Client {
    public static void main(String[] args) {
        EventLoopGroup group=new NioEventLoopGroup();

        try {
            Bootstrap bootstrap=new Bootstrap();

            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 50000)).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
```



<br/>



#### ClientInitializer

```java
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();



        //加入一个出站的handler，对数据进行编码
        pipeline.addLast(new LongToByteEncoder());

        //计入一个入站的handler，对数据进行解码
        pipeline.addLast(new ByteToLongDecoder());

        //加入自定义的handler，处理业务
        pipeline.addLast(new ClientHandler());
    }
}
```

<br/>



#### clientHandler

```java
public class ClientHandler extends SimpleChannelInboundHandler<Long> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送数据...");

        //发送一个Long
        ctx.writeAndFlush(123456L);

        //        ctx.writeAndFlush(Unpooled.copiedBuffer("asdfzxcaaaaaaaaa", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("收到服务器消息："+msg);
    }


}
```



<br/>



#### LongToByteEncoder

```java
public class LongToByteEncoder extends MessageToByteEncoder<Long> {

    /**
    * @Description 编码器
    * @date 2020/7/24 18:03
    * @param ctx
    * @param msg
    * @param out
    * @return void
    */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("LongToByteEncoder 的 encode 方法被调用...");
        System.out.println("msg="+msg);
        out.writeLong(msg);
    }
}
```

<br/>



#### ByteToLongDecoder

```java
public class ByteToLongDecoder extends ByteToMessageDecoder {

    /**
    * @Description TODO
    * @date 2020/7/24 17:52
    * @param ctx  上下文对象
    * @param in  入站的ByteBuf
    * @param out  List集合，讲解码后的数据传给下一个handler
    * @return void
    */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToLongDecoder 的 decode 方法被调用...");
        //每次读取8个字节
        if(in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
```

<br/>



#### 运行

![](https://img-blog.csdnimg.cn/20200724203226369.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

![](https://img-blog.csdnimg.cn/20200724203313950.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>



### 传输字符

&emsp;使用上面的列子，客户端传输字符

```java
@Override
public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("发送数据...");

    //发送一个Long
    //ctx.writeAndFlush(123456L);

    //发送16个字节
    ctx.writeAndFlush(Unpooled.copiedBuffer("asdfzxcaaaaaaaaa", CharsetUtil.UTF_8));
}
```



&emsp;运行

![](https://img-blog.csdnimg.cn/20200724195423436.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

> &emsp;当发送的是字符串的时候，客户端不会经过编码器，服务端会将二进制解码成Long。又因为我发送的字符有16个字节，所以解码的时候回进行两次解码

- 不论解码器 handler 还是 编码器 handler 即接收的消息类型必须与待处理的消息类型一致，否则该 handler 不 会被执行 

- 在解码器 进行数据解码时，需要判断 缓存区(ByteBuf)的数据是否足够 ，否则接收到的结果会期望结果可能 不一致 

<br/>





### 解码器-ReplayingDecoder 

1. public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder
2. ReplayingDecoder 扩展了 ByteToMessageDecoder 类，使用这个类，**我们不必调用** **readableBytes()**方法。参数S 指定了用户状态管理的类型，其中 Void 代表不需要状态管理 
3. 应用实例：使用 ReplayingDecoder 编写解码器，对前面的案例进行简化

```java
public class ByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToLongDecoder2 的 decode 方法被调用...");
        out.add(in.readLong());
    }
}
```

4. ReplayingDecoder 使用方便，但它也有一些局限性：
   - 并 不 是 所 有 的 ByteBuf 操 作 都 被 支 持 ， 如 果 调 用 了 一 个 不 被 支 持 的 方 法 ， 将 会 抛 出 一 个 **UnsupportedOperationException**
   - ReplayingDecoder 在某些情况下可能**稍慢于 ByteToMessageDecoder**，例如网络缓慢并且消息格式复杂时，消息会被拆成了多个碎片，速度变慢

<br/>



### 其它编解码器

#### 解码器

1. LineBasedFrameDecoder：这个类在 Netty 内部也有使用，它使用行尾控制字符（\n 或者\r\n）作为分隔符来解 析数据
2. DelimiterBasedFrameDecoder：使用自定义的特殊字符作为消息的分隔符
3. HttpObjectDecoder：一个 HTTP 数据的解码器
4. LengthFieldBasedFrameDecoder：通过指定长度来标识整包消息，这样就可以自动的处理黏包和半包消息

![](https://img-blog.csdnimg.cn/20200724211607357.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)





<br/>



#### 编码器

![](https://img-blog.csdnimg.cn/20200724211656600.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)





<br/>



## 整合log4j

### 引入依赖

```xml
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.25</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.25</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>1.7.25</version>
    <scope>test</scope>
</dependency>
```

<br/>



### 配置

```properties
log4j.rootLogger=DEBUG,stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=[%p] %C{1} - %m%n
```

<br/>



### 运行测试

![](https://img-blog.csdnimg.cn/20200724223829520.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>

<br/>



## TCP 粘包和拆包 及解决方案

### TCP 粘包和拆包基本介绍

1. TCP 是面向连接的，面向流的，提供高可靠性服务。收发两端（客户端和服务器端）都要有一一成对的 socket， 因此，发送端为了将多个发给接收端的包，更有效的发给对方，使用了优化方法（Nagle 算法），**将多次间隔 较小且数据量小的数据，合并成一个大的数据块，然后进行封包**。这样做虽然提高了效率，**但是接收端就难于分辨出完整的数据包了，因为面向流的通信是无消息保护边界的 **

2. 由于 TCP 无消息保护边界, 需要在接收端处理消息边界问题，也就是我们所说的`粘包`、`拆包`问题, 看一张图

   ![](https://img-blog.csdnimg.cn/20200724234155392.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

   

   假设客户端分别发送了两个数据包 D1 和 D2 给服务端，由于服务端一次读取到字节数是不确定的，故可能存在以 下四种情况

   1. 服务端分两次读取到了两个独立的数据包，分别是 D1 和 D2，没有粘包和拆包
   2. 服务端一次接受到了两个数据包，D1 和 D2 粘合在一起，称之为 TCP 粘包
   3. 服务端分两次读取到了数据包，第一次读取到了完整的 D1 包和 D2 包的部分内容，第二次读取到了 D2 包的剩余内容，这称之为 TCP 拆包
   4. 服务端分两次读取到了数据包，第一次读取到了 D1 包的部分内容 D1_1，第二次读取到了 D1 包的剩余部 分内容 D1_2 和完整的 D2 包

   

   <br/>



### TCP 粘包和拆包现象实例

#### server

```java
public class Server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(50000)).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```

<br/>



#### serverInitializer

```java
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();
        pipeline.addLast(new ServerHandler());
    }
}
```

<br/>



#### serverHandler

```java
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count=0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("服务器接受到的数据："+msg.toString(CharsetUtil.UTF_8));
        System.out.println("服务器接受到的信息量："+(++this.count));

        //回送数据,返回一个UUID
        String id= UUID.randomUUID().toString();
        ctx.writeAndFlush(Unpooled.copiedBuffer(id+"\n",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
```

<br/>



#### client

```java
public class Client {
    public static void main(String[] args) {
        EventLoopGroup group=new NioEventLoopGroup();

        try {
            Bootstrap bootstrap=new Bootstrap();

            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 50000)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
```

<br/>





#### clientInitializer

```java
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ClientHandler());
    }
}
```

<br/>



#### clientHandler

```java
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count=0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端发送10条数据
        for(int i=0;i<10;++i){
            ctx.writeAndFlush(Unpooled.copiedBuffer(("hello server "+i+"\t"), CharsetUtil.UTF_8));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("客户端接受到服务器的信息："+msg.toString(CharsetUtil.UTF_8));
        System.out.println("客户端接收到的信息量："+(++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
```

<br/>



#### 运行

&emsp;服务器

![](https://img-blog.csdnimg.cn/20200724234640998.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



&emsp;客户端

![](https://img-blog.csdnimg.cn/20200724234755394.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>







<br/>



### TCP 粘包和拆包解决方案

1. 使用自定义协议 + 编解码器 来解决
2. 关键就是要解决 服务器端每次读取数据长度的问题, 这个问题解决，就不会出现服务器多读或少读数据的问 题，从而避免的 TCP 粘包、拆包



<br/>



#### messageProtocol

```java
public class MessageProtocol {
    private int len;
    private byte[] content;

    public MessageProtocol() {
    }



    public MessageProtocol(byte[] content, int len) {
        this.content=content;
        this.len=len;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
```



<br/>



#### messageEncoder

```java
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MessageEncoder encode 方法被调用\n");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
```

<br/>



#### messageDecoder

```java
public class MessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MessageDecoder decode 被调用");
        //将得到的二进制字节码->MessageProtocol数据包(对象)
        int len=in.readInt();

        byte[] content=new byte[len];
        in.readBytes(content);
        out.add(new MessageProtocol(content,len));
    }
}
```

<br/>



#### server

```java
public class Server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(50000)).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```



<br/>



#### serverInitializer

```java
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();

        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new ServerHandler());
    }
}
```



<br/>



#### serverHandler

```java
public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count=0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("服务器接受到的数据："+new String(msg.getContent(),CharsetUtil.UTF_8)
                           +"\t数据长度"+msg.getLen());

        System.out.println("服务器接受到的信息量："+(++this.count));

        //回送数据,返回一个UUID
        String id= UUID.randomUUID().toString();
        ctx.writeAndFlush(new MessageProtocol(id.getBytes(),id.getBytes().length));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
```



<br/>



#### client

```java
public class Client {
    public static void main(String[] args) {
        EventLoopGroup group=new NioEventLoopGroup();

        try {
            Bootstrap bootstrap=new Bootstrap();

            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 50000)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
```

<br/>



#### clientInitializer

```java
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new ClientHandler());
    }
}
```

<br/>



#### clientHandler

```java
public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count=0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //客户端发送10条数据

        for(int i=0;i<5;++i){
            byte[] content = ("hello server " + i + "\t").getBytes(CharsetUtil.UTF_8);
            MessageProtocol messageProtocol = new MessageProtocol(content,content.length);
            System.out.println(messageProtocol.getLen()+" "+new String(messageProtocol.getContent(),CharsetUtil.UTF_8));
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("客户端接受到服务器的信息："+new String(msg.getContent(),CharsetUtil.UTF_8)
                           +"\t数据长度"+msg.getLen());
        System.out.println("客户端接收到的信息量："+(++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
```



<br/>



## 任务加入异步线程池

### 介绍

1. 在 Netty 中做耗时的，不可预料的操作，比如数据库，网络请求，会严重影响 Netty 对 Socket 的处理速度。
2. 而解决方法就是将耗时任务添加到异步线程池中。但就添加线程池这步操作来讲，可以有2种方式，而且这2种方式实现的区别也蛮大的。
   1. 处理耗时业务的第一种方式---`handler 中加入线程池`
   2. 处理耗时业务的第二种方式---`Context 中添加线程池`
      - 当我们使用addLast方法添加线程池后，handler将优先使用这个线程池，如果不添加，将使用IO线程

&emsp;流程

![](https://img-blog.csdnimg.cn/20200725150503392.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



### 方式一

#### server

```java
public class Server {
    public static void main(String[] args) throws IOException {
        //创建BossGroup和WorkerGroup
        //bossGroup和workerGroup含有的子线程的个数，默认为cpu核数*2
        EventLoopGroup bossGroup=new NioEventLoopGroup(5);
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap  bootstrap=new ServerBootstrap();

        try {
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup)  //设置两个线程
                .channel(NioServerSocketChannel.class)  //使用NioServerSocketChannel，作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列里连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {  //handler对应的是boosGruop，childHandler对应的是workerGroup
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //可以使用一个集合管理SocketChannel，在推送消息时，可以将业务加入到各个channel对应的
                        // NioEventLoop的taskQueue或者scheduleTaskQueue中
                        System.out.println("客户端socketChannel hashcode="+socketChannel.hashCode());
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });  //给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器 is ready ...");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给 cf 注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("监听端口成功");
                    }else{
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
```

<br/>



#### serverHandler

```java
public class ServerHandler extends ChannelInboundHandlerAdapter {

    static final EventExecutorGroup group=new DefaultEventExecutorGroup(16);

    private final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    /**
    * @Description 读取数据事件(这里我们可以读取客户端发送的消息)
    * @date 2020/7/23 10:33
    * @param ctx  上下文对象，含有：管道(pipeline),通道(channel),地址
    * @param msg  客户端发送的数据
    * @return void
    */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("接收到的消息："+buf.toString(CharsetUtil.UTF_8)+"  "+ LocalDateTime.now().format(formatter) +"  当前线程----"+Thread.currentThread().getName());

        //任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("服务端发送消息1  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端1\t",CharsetUtil.UTF_8));
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("服务端发送消息2  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端2\t",CharsetUtil.UTF_8));
            }
        });

        //定时任务
        ctx.channel().eventLoop().schedule(()->{
            System.out.println("服务端发送消息3  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端4\t",CharsetUtil.UTF_8));
        },10,TimeUnit.SECONDS);


        //将任务提交到group线程池
        group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("服务端发送消息4  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端4\t",CharsetUtil.UTF_8));
                return null;
            }
        });

        System.out.println("发送消息...");
    }

    /**
    * @Description 数据读取完毕
    * @date 2020/7/23 10:46
    * @param ctx  
    * @return void
    */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲并刷新
        //一般来讲，我们队发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello,客户端",CharsetUtil.UTF_8));
    }

    /**
    * @Description 处理异常，一般是需要管理通道
    * @date 2020/7/23 11:13
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
```

<br/>



#### 核心代码

```java
//将任务提交到group线程池
group.submit(new Callable<Object>() {
    @Override
    public Object call() throws Exception {
        System.out.println("服务端发送消息4  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端4\t",CharsetUtil.UTF_8));
        return null;
    }
});
```

<br/>



#### 运行

![](https://img-blog.csdnimg.cn/20200725152838945.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



### 方式二

#### server

```java
public class Server {

    static final EventExecutorGroup group=new DefaultEventExecutorGroup(2);

    public static void main(String[] args) throws IOException {
        //创建BossGroup和WorkerGroup
        //bossGroup和workerGroup含有的子线程的个数，默认为cpu核数*2
        EventLoopGroup bossGroup=new NioEventLoopGroup(5);
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap  bootstrap=new ServerBootstrap();

        try {
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup)  //设置两个线程
                .channel(NioServerSocketChannel.class)  //使用NioServerSocketChannel，作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)  //设置线程队列里连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {  //handler对应的是boosGruop，childHandler对应的是workerGroup
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //可以使用一个集合管理SocketChannel，在推送消息时，可以将业务加入到各个channel对应的
                        // NioEventLoop的taskQueue或者scheduleTaskQueue中
                        System.out.println("客户端socketChannel hashcode="+socketChannel.hashCode());

                        //如果我们在addLast添加handler，前面有指定EventExecutorGroup,name该handler会优先加入到该线程池中
                        socketChannel.pipeline().addLast(group,new ServerHandler());
                    }
                });  //给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器 is ready ...");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给 cf 注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("监听端口成功");
                    }else{
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
```

<br/>



#### serverHandler

```java
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    /**
    * @Description 读取数据事件(这里我们可以读取客户端发送的消息)
    * @date 2020/7/23 10:33
    * @param ctx  上下文对象，含有：管道(pipeline),通道(channel),地址
    * @param msg  客户端发送的数据
    * @return void
    */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("接收到的消息："+buf.toString(CharsetUtil.UTF_8)+"  "+ LocalDateTime.now().format(formatter) +"  当前线程----"+Thread.currentThread().getName());

        //任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("服务端发送消息1  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端1\t",CharsetUtil.UTF_8));
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("服务端发送消息2  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端2\t",CharsetUtil.UTF_8));
            }
        });

        //定时任务
        ctx.channel().eventLoop().schedule(()->{
            System.out.println("服务端发送消息3  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端4\t",CharsetUtil.UTF_8));
        },10,TimeUnit.SECONDS);


        System.out.println("服务端发送消息4  "+LocalDateTime.now().format(formatter)+"  当前线程----"+Thread.currentThread().getName());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端4\t",CharsetUtil.UTF_8));

        System.out.println("发送消息...");
    }

    /**
    * @Description 数据读取完毕
    * @date 2020/7/23 10:46
    * @param ctx  
    * @return void
    */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲并刷新
        //一般来讲，我们队发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer(new Date().toString()+":"+"hello,客户端",CharsetUtil.UTF_8));
    }

    /**
    * @Description 处理异常，一般是需要管理通道
    * @date 2020/7/23 11:13
    * @param ctx
    * @param cause
    * @return void
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
```

<br/>



#### 核心代码

```java
//如果我们在addLast添加handler，前面有指定EventExecutorGroup,name该handler会优先加入到该线程池中
socketChannel.pipeline().addLast(group,new ServerHandler());
```

<br/>



#### 运行

![](https://img-blog.csdnimg.cn/20200725154022899.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>





## 用 Netty 自己 实现 RPC

### RPC 基本介绍

1. RPC（Remote Procedure Call）— 远程过程调用，是一个计算机通信协议。该协议允许运行于一台计算机的程序调用另一台计算机的子程序，而程序员无需额外地为这个交互作用编程

2. 两个或多个应用程序都分布在不同的服务器上，它们之间的调用都像是本地方法调用一样(如图)

   ![](https://img-blog.csdnimg.cn/20200725214937817.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

3. 常见的 RPC 框架有: 比较知名的如阿里的Dubbo、google的gRPC、Go语言的rpcx、Apache的thrift，Spring 旗下的 Spring Cloud



<br/>



### RPC 调用流程图

![](https://img-blog.csdnimg.cn/20200725215224420.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<BR/>

### PRC 调用流程说明

1. 服务消费方(client)以本地调用方式调用服务
2. client stub 接收到调用后负责将方法、参数等封装成能够进行网络传输的消息体
3. client stub 将消息进行编码并发送到服务端
4. server stub 收到消息后进行解码
5. server stub 根据解码结果调用本地的服务
6. 本地服务执行并将结果返回给 server stub
7. server stub 将返回导入结果进行编码并发送至消费方
8. client stub 接收到消息并进行解码
9. 服务消费方(client)得到结果

>小结：RPC 的目标就是将 **2-8** **这些步骤都封装起来**，用户无需关心这些细节，可以像调用本地方法一样即可完成远程服务调用 

<br/>

### 自己实现 dubbo RPC(基于 Netty)

#### 需求说明

1. dubbo 底层使用了 Netty 作为网络通讯框架，要求用 Netty 实现一个简单的 RPC 框架 
2. 模仿 dubbo，消费者和提供者约定接口和协议，消费者远程调用提供者的服务，提供者返回一个字符串，消费 者打印提供者返回的数据。

<br/>



#### 设计说明

1. 创建一个接口，定义抽象方法。用于消费者和提供者之间的约定

2. 创建一个提供者，该类需要监听消费者的请求，并按照约定返回数据

3. 创建一个消费者，该类需要透明的调用自己不存在的方法，内部需要使用 Netty 请求提供者返回数据

4. 开发的分析图

   ![](https://img-blog.csdnimg.cn/20200725215533954.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)

<br/>



5. 目录结构

   ![](https://img-blog.csdnimg.cn/20200725215715875.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ0NzY2ODgz,size_16,color_FFFFFF,t_70)



<br/>





### publicInterface(代码)

#### IHelloService

```java
public interface IHelloService {

    String hello(String msg);

}
```

<br/>



### provider(代码)

#### HelloServiceImpl

```java
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
```

<br/>





#### ServerBootStrap

```java
public class ServerBootStrap {
    public static void main(String[] args) {
        //启动服务
        NettyServer.startServer("127.0.0.1",50000);
    }
}
```

<br/>



### netty

#### NettyServer

```java
public class NettyServer {


    public static void startServer(String host,int port){
        startServer0(host,port);
    }


    private static  void startServer0(String host,Integer port){
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap=new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();

                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        //业务处理器
                        pipeline.addLast(new NettyServerHandler());
                    }
                });
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();


            //进行监听
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println(Thread.currentThread().getName());
                    if(future.isSuccess()){
                        System.out.println("服务提供方开始 提供服务成功");
                    }else{
                        System.out.println("服务提供方开始 提供服务失败");
                    }
                }
            });

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
```

<br/>





#### NettyServerHandler

```java
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用我们的服务
        System.out.println(Thread.currentThread().getName()+"  msg="+msg);

        //客户端在调用服务器的api时，我们需要定义一个协议
        //比如我们要求 每次发送消息都是必须以某个字符串开头"HelloService#hello#"
        if(msg.toString().startsWith("HelloService#hello#")){
            String  result= new HelloServerImpl().hello(msg.toString().substring(msg.toString().lastIndexOf('#')+1));
            ctx.writeAndFlush(result);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常关闭"+cause.getMessage());
        ctx.close();
    }
}
```





<br/>



#### NettyClient

```java
public class NettyClient {

    /**
     * 创建线程池
     */
    //不推荐
    //    private static ExecutorService executor=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private  static ThreadFactory threadFactory=new DefaultThreadFactory("netty-client");

    private static  ExecutorService executor=new ThreadPoolExecutor(10,10,
                                                                    60L, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10),threadFactory);

    //使用guava提供的ThreadFactoryBuilder来创建线程池(推荐)
    //    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
    //            .setNameFormat("demo-pool-%d").build();

    private static NettyClientHandler clientHandler;

    public Object getBean(final Class<?> serviceClass,final  String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{serviceClass},
                                      ((proxy, method, args) -> {
                                          if(clientHandler==null){
                                              initClient();
                                          }
                                          //设置要发给服务器端的信息
                                          //协议头+调用远程api的参数
                                          clientHandler.setParam(providerName+args[0]);
                                          return executor.submit(clientHandler).get();
                                      }));
    }

    private static void initClient(){
        clientHandler=new NettyClientHandler();
        //创建EventLoopGroup
        EventLoopGroup group=new NioEventLoopGroup();

        try {
            Bootstrap bootstrap=new Bootstrap();

            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true) //不延迟
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(clientHandler);
                    }
                });

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 50000)).sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

<br/>



#### NettyClientHandler

```java
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    /**
     * 上下文
     */
    private ChannelHandlerContext context;
    /**
     * 返回的结果
     */
    private String result;
    /**
     * 客户端调用方法时，传入的参数
     */
    private String param;

    /**
    * @Description 与服务器的连接创建后，方法会被调用(1)
    * @date 2020/7/25 17:07
    * @param ctx
    * @return void
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //我在其他方法会用到ctx
        this.context=ctx;
    }


    /**
    * @Description 收到服务器的数据后，方法会被调用(4)
    * @date 2020/7/25 17:08
    * @param ctx
    * @param msg
    * @return void
    */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("从服务端获取消息  "+Thread.currentThread().getName());
        this.result=msg.toString();
        //唤醒等待的线程
        notify();
    }

    /**
    * @Description 被代理对象调用，真正发送数据给服务器-->wait-->等待被唤醒，获取结果(3)-->(5)
    * @date 2020/7/25 17:12
    * @return java.lang.Object
    */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("代理发送消息  "+Thread.currentThread().getName());

        context.writeAndFlush(param);
        //进行wait,等待channelRead方法获取服务器的结果后，唤醒
        wait();
        //返回服务方返回的结果
        return this.result;
    }

    @Override
    public  void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
    * @Description 设置参数(2)
    * @date 2020/7/25 17:20
    * @param param  
    * @return void
    */
    public void setParam(String param){
        this.param=param;
    }

}

```

<br/>



### consumer

#### ClientBootStrap

```java
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
```

