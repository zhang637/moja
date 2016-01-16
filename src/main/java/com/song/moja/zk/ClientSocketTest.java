package com.song.moja.zk;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.song.moja.util.SocketUtil;

public class ClientSocketTest {

	public static void main(String[] args) {
		Socket socket = null;

		int port = 2099;
		try {
			socket = new Socket("localhost", port);

			for (int i = 0; i < 100; i++) {
				socket.getOutputStream().write(("hello" + i).getBytes());
				socket.getOutputStream().flush();
				String readStrFromStream = SocketUtil.readStrFromStream(socket.getInputStream());
				System.out.println(readStrFromStream);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
