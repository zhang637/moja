package com.song.moja.netty;

import com.song.moja.server.ServerConfig;
import com.song.moja.server.ThreadManager;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

//处理protobuf的编解码
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
	final ThreadManager threadManager;
	final ServerConfig config;

	public NettyServerInitializer(ThreadManager threadManager,
			ServerConfig config) {
		this.threadManager = threadManager;
		this.config = config;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		
		// 业务逻辑处理类
		pipeline.addLast("handler", new NettyServerHandler(threadManager,
				config));

	}
}