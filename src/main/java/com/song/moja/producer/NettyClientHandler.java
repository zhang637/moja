package com.song.moja.producer;

import com.song.moja.protobuf.LogProbuf;
import com.song.moja.protobuf.LogProbuf.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler<T> extends SimpleChannelInboundHandler<T> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Log log = LogProbuf.Log.newBuilder().setLogId(1).setLogMsg("这是条日志测试信息!!!").build();

		for (int i = 0; i < 10; i++) {
			ctx.channel().writeAndFlush(log.toByteArray());
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, T msg) throws Exception {
		System.out.println(msg);
	}
}