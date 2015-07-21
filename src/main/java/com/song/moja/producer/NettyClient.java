package com.song.moja.producer;
 
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
 
public class NettyClient {
 
    private final String host;
    private final int port;
 
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
 
    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer());
 
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
 
    public static void main(String[] args) throws Exception {
        // Print usage if no argument is specified.
//        if (args.length != 2) {
//            System.err.println("Usage: " + ProtobufNettyClient.class.getSimpleName() + " <host> <port>");
//            return;
//        }
        // Parse options.
//        String host = args[0];
        String host = "localhost" ;
//        int port = Integer.parseInt(args[1]);
        int port = 8088;
        new NettyClient(host, port).run();
    }
}