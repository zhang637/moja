package com.song.moja.netty;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.song.moja.protobuf.LogProbuf.Log;
import com.song.moja.server.ServerConfig;
import com.song.moja.server.ThreadManager;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

//得到log之后的业务逻辑
public class ProtobufNettyServerHandler<T> extends
		SimpleChannelInboundHandler<T> {

	final ThreadManager<T> threadManager;
	final ServerConfig config;
	final long enqueueTimeoutMs;

	private HttpRequest request;

	final int persistBatchSize;

	final List<Log> tempList;

	public ProtobufNettyServerHandler(ThreadManager<T> threadManager,
			ServerConfig serverConfig) {
		this.threadManager = threadManager;
		this.config = serverConfig;

		enqueueTimeoutMs = config.getEnqueueTimeoutMs();
		persistBatchSize = config.getPersistBatchSize();

		tempList = new ArrayList<Log>(persistBatchSize);

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
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
					added = queue.offer(data, enqueueTimeoutMs,
							TimeUnit.MILLISECONDS);
				}
			} catch (InterruptedException e) {
				throw new InterruptedException();
			}
		}
		
		
		
		// 返回结果
		ctx.channel().writeAndFlush("ok".getBytes());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, Object arg1)
			throws Exception {
		System.out.println("messageReceived" + arg1);
	}
}