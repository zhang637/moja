package com.song.moja.netty;

import java.io.Closeable;

import org.apache.log4j.Logger;

import com.song.moja.server.ServerConfig;
import com.song.moja.server.ThreadManager;
import com.song.moja.util.Constance;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty服务器类，负责监听请求
 * 
 * @author 3gods.com
 * @param <T>
 */
public class NettyServer<T> extends Thread implements Closeable {
	private static Logger LOG = Logger.getLogger(NettyServer.class);

	private final int port;
	final EventLoopGroup bossGroup = new NioEventLoopGroup();
	final EventLoopGroup workerGroup = new NioEventLoopGroup(10);
	private ServerConfig config;
	private ThreadManager<T> threadManager;
	private String logTransferType;

	public NettyServer(int port) {
		this.port = port;
	}

	public NettyServer(ThreadManager<T> threadManager, ServerConfig config) {
		this.threadManager = threadManager;
		this.config = config;
		this.port = config.getPort();
		this.logTransferType = config.getLogTransferType();
	}

	public void run() {
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);

			ChannelHandler childHandler = null;
			if (logTransferType.equalsIgnoreCase(Constance.LOG_TYPE_PB)) {
				childHandler = new ProtobufNettyServerInitializer<T>(threadManager, config);
			} else if (logTransferType.equalsIgnoreCase(Constance.LOG_TYPE_JSON)) {
				childHandler = new NettyServerInitializer<T>(threadManager, config);
			} else {
				throw new IllegalArgumentException("不支持的日志传输格式" + logTransferType);
			}
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(childHandler);

			Channel ch = null;
			try {
				ch = b.bind(port).sync().channel();
				LOG.info("NettyServer启动" + port);
				ch.closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		LOG.warn("PHP日志服务器挂掉!!!");
	}

	public void close() {
		LOG.info("日志服务器关闭，释放占用端口" + port);
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
}