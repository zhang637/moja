package com.song.moja.netty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.song.moja.protobuf.LogProbuf.Log;
import com.song.moja.server.ServerConfig;
import com.song.moja.server.ThreadManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

//得到log之后的业务逻辑
public class ProtobufNettyServerHandler<T> extends SimpleChannelInboundHandler<T> {

	final ThreadManager<T> threadManager;
	final ServerConfig config;
	final long enqueueTimeoutMs;

	final int persistBatchSize;
	final List<Log> tempList;

	public ProtobufNettyServerHandler(ThreadManager<T> threadManager, ServerConfig serverConfig) {
		this.threadManager = threadManager;
		this.config = serverConfig;

		enqueueTimeoutMs = config.getEnqueueTimeoutMs();
		persistBatchSize = config.getPersistBatchSize();

		tempList = new ArrayList<Log>(persistBatchSize);

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		BlockingQueue<T> queue = threadManager.getMq();
		boolean added = false;

		T data = (T) msg;
		if (data != null) {
			try {
				if (enqueueTimeoutMs == 0) {
					added = queue.offer(data);
				} else if (enqueueTimeoutMs < 0) {
					queue.put(data);
					added = true;
				} else {
					added = queue.offer(data, enqueueTimeoutMs, TimeUnit.MILLISECONDS);
				}
			} catch (InterruptedException e) {
				throw new InterruptedException();
			}
		}

		// 返回结果
		ctx.channel().writeAndFlush("ok".getBytes());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, Object arg1) throws Exception {
		System.out.println("messageReceived" + arg1);
	}
}