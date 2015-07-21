package com.song.moja.producer;
 

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
 
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("protobufEncoder", new ProtobufEncoder());
//        pipeline.addLast("frameDecoder", new ProtobufVarint32LengthFieldPrepender());
//        pipeline.addLast("protobufDecoder", new ProtobufDecoder(
//                CommonLogProbuf.CommonLog.getDefaultInstance()));
        //pipeline.addLast("protobufHandler", new NettyClientHandler());
    }
}