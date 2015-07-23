package com.song.moja.netty;

import com.song.moja.server.ServerConfig;
import com.song.moja.server.ThreadManager;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 负责JSON格式日志的编解码
 * 
 * @author 3gods.com
 * 
 */
public class NettyServerInitializer<T> extends ChannelInitializer<SocketChannel> {
	final ThreadManager<T> threadManager;
	final ServerConfig config;

	public NettyServerInitializer(ThreadManager<T> threadManager,
			ServerConfig config) {
		this.threadManager = threadManager;
		this.config = config;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
				1048576, 0, 4, 0, 4));

		// 业务逻辑处理类
		pipeline.addLast("handler", new NettyServerHandler<T>(threadManager,
				config));

	}
}