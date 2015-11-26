package com.song.moja.zk;

public class Server {

	public static void main(String[] args) throws Exception {
		System.out.println("hello!!!");

		String host = "localhost";
		int port = 2099;

		ServiceProvider provider = new ServiceProvider();
		provider.publish(host, port);

		Thread.sleep(Long.MAX_VALUE);
	}
}