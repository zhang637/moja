package com.song.moja.zk;

/**
 * 创建临时node
 * 
 * @author zhangyoulei
 *
 */
public class Server {
	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 2099;

		ServiceProvider provider = new ServiceProvider();
		provider.publish(host, port);

		Thread.sleep(Long.MAX_VALUE);
	}
}