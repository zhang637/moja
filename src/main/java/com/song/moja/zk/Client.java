package com.song.moja.zk;

public class Client {

	public static void main(String[] args) throws Exception {
		ServiceConsumer consumer = new ServiceConsumer();
		for (int i = 0; i < 1000; i++) {
			String result = consumer.sendLog("jack" + i);
			System.out.println("接收到服务器返回消息" + result);
		}
	}
}