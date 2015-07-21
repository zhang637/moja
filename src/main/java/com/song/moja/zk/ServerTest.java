package com.song.moja.zk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.song.moja.util.SocketUtil;


public class ServerTest {

	public static void main(String[] args) {

		ExecutorService servicePool = Executors.newFixedThreadPool(Runtime
				.getRuntime().availableProcessors());

		ServerSocket ss = null;
		Socket client = null;
		
		int port = 2099;
		try {
			ss = new ServerSocket(port);
			System.out.println("启动服务器端成功!!!当前服务端口号是"+port);
			while (true) {
				client = ss.accept();
				servicePool.execute(new SocketHandler(client,port));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

class SocketHandler implements Runnable {

	private Socket client;
	
	private int port;

	public SocketHandler(Socket client,int port) {
		this.client = client;
		this.port = port;
	}

	public void run() {
		OutputStream out = null;
		InputStream in = null;
		try {
			client.setSoTimeout(4000);
			while (true) {
				out = client.getOutputStream();
				in = client.getInputStream();

				byte[] temp = SocketUtil.readBytesFromStream(in);

				if (null != temp && temp.length > 0) {
					String proTypeStr = new String(temp);
					System.out.println("接收到客户端消息" + proTypeStr+"当前服务器端口是"+port);
					
					out.write(("success"+port).getBytes());
				}
			}
		} catch (SocketTimeoutException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}

				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
