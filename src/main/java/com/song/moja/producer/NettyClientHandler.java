package com.song.moja.producer;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler<T> extends SimpleChannelInboundHandler {
	private Logger log = Logger.getLogger(NettyClientHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Log log =
		// LogProbuf.Log.newBuilder().setLogId(1).setLogMsg("这是条日志测试信息!!!").build();

		for (int i = 0; i < 100; i++) {
			ctx.channel().writeAndFlush(
					"{\"key\":\"--123--\"}\r\n"/* log.toByteArray() */);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info((T) msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info((T) msg);
	}
}