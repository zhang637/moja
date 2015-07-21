package com.song.moja.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.log4j.Logger;

import com.song.moja.server.ServerConfig;
import com.song.moja.server.ThreadManager;
import com.song.moja.zk.ServiceProvider;
import com.sun.xml.internal.ws.Closeable;

public class NettyServer extends Thread implements Closeable {
	private static Logger LOG = Logger.getLogger(NettyServer.class);
	
	private final int port;
    final EventLoopGroup bossGroup = new NioEventLoopGroup();
    final EventLoopGroup workerGroup = new NioEventLoopGroup(10);
	
    private ServerConfig config;
    private ThreadManager threadManager;
    
    private String logTransferType;
    
	public NettyServer(int port) {
		this.port = port;
	}

	public NettyServer(ThreadManager threadManager, ServerConfig config) {
		this.threadManager = threadManager;
		this.config = config;
		this.port = config.getPort();
		this.logTransferType = config.getLogTransferType();
	}

	public void run(){
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			
			ChannelHandler childHandler = null;
			if(logTransferType.equalsIgnoreCase("protobuf")){
				childHandler = new ProtobufNettyServerInitializer(threadManager,config);
				
			}else if(logTransferType.equalsIgnoreCase("json")){
				childHandler = new NettyServerInitializer(threadManager,config);
				
			}else{
				throw new IllegalArgumentException("不支持的日志传输格式"+logTransferType);
			}
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(childHandler);
			
			Channel ch = null;
			try {
				ch = b.bind(port).sync().channel();
				LOG.error("NettyServer start at port {}"+port,null);
				ch.closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//启动服务器之后去zookeeper注册ServerSocket地址
			ServiceProvider provider = new ServiceProvider();
			provider.publish("localhost", port);
			
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		LOG.warn("PHP日志服务器挂掉!!!");
	}


    public void close() {
        LOG.info("PHP日志服务器ProtobufNettyServer关闭，释放占用端口"+port);
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        //这里需要通知zookeeper吗，不通知，zookeeper自己等下知道，通知了更加人性化
    }
	
}