package com.song.moja.monitor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;

import com.song.moja.http.HTTPResult;
import com.song.moja.http.HTTPUtils;
import com.song.moja.server.ServerConfig;
import com.song.moja.util.Constance;
import com.song.moja.util.SocketUtil;

/**
 * 关于此线程监听8888端口响应监控队列请求 这个后面可能使用JMX实现
 * 
 * @author 3gods.com
 *
 */
public class MonitorHandler extends Thread implements Closeable {

	final ServerConfig config;

	final int monitorPort;

	private final CountDownLatch shutdownLatch = new CountDownLatch(1);

	public MonitorHandler(ServerConfig serverConfig) {
		this.config = serverConfig;
		this.monitorPort = config.getMonitorPort();
	}

	@Override
	public void run() {
		ServerSocket ss = null;
		Socket socket = null;
		try {
			ss = new ServerSocket(monitorPort);
			while (!Thread.interrupted()) {
				socket = ss.accept();

				OutputStream out = socket.getOutputStream();
				InputStream in = socket.getInputStream();

				byte[] bys = SocketUtil.readBytesFromStream(in);
				// 获取头信息
				byte[] proType = Arrays.copyOfRange(bys, 0, 4);
				String proTypeStr = new String(proType, "UTF-8");
				if (proTypeStr.equals(Constance.REQUEST_GET) || proTypeStr.equals(Constance.REQUEST_POST)) {
					String uri = HTTPUtils.parseUri(new String(bys));

					byte[] result = null;
					if (StringUtils.isEmpty(uri)) {
						result = HTTPResult.get404Result();

						out.write(result);
					} else {
						HTTPUtils.process(uri, out);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void close() throws IOException {
		shutdownLatch.countDown();
	}
}
