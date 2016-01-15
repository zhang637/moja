package com.song.moja.producer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class NettyClientInitializer<T> extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8), new LineBasedFrameDecoder(1024),
				new StringDecoder(CharsetUtil.UTF_8));
		// pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
		// pipeline.addLast("protobufEncoder", new ProtobufEncoder());
		// pipeline.addLast("frameDecoder", new
		// ProtobufVarint32LengthFieldPrepender());
		// pipeline.addLast("protobufDecoder", new
		// ProtobufDecoder(CommonLogProbuf.CommonLog.getDefaultInstance()));
		pipeline.addLast("handler", new NettyClientHandler<T>());
	}
}