package com.song.moja.zk;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocketTest {

	public static void main(String[] args) {
		Socket socket = null;
		
		int port = 2099;
		try {
			socket = new Socket("localhost", port);

			for (int i = 0; i < 5; i++) {
				socket.getOutputStream().write(("hello" + i).getBytes());
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
