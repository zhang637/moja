package com.song.moja.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.song.moja.util.Constance;
import com.song.moja.util.PropertyUtil;

public class HTTPUtils {

	private static Logger LOG = Logger.getLogger(HTTPUtils.class);

	public static void process(String request, OutputStream out) throws IOException {
		// 解析出URI
		String uri = parseUri(request);

		if (StringUtils.isEmpty(uri)) {
			send404(out);
			return;
		}

		// String servletName = null;
		// 这个地方直接写死URL了，还是根据URL得到servlet来进行处，反正这个监控队列暂时不做，就这样
		if (uri.equals("/monitor.html")) {
			// servletName = uri.substring(uri.lastIndexOf("/") + 1);
			LOG.info("客户端请求URL地址是:" + uri);

			sendStaticResource(out, uri);
		} else if (uri.equals("/test")) {
			MQMonitorInfo.send(out);
		} else {
			sendStaticResource(out, uri);
		}
	}

	public static void process(String uri, SocketChannel socketChannel) throws IOException {
		if (StringUtils.isEmpty(uri)) {
			send404(socketChannel);
			return;
		}

		String servletName = null;
		servletName = uri.substring(uri.lastIndexOf("/") + 1);
		LOG.info("客户端请求地址是:" + servletName);
		// 这个地方直接写死URL了，还是根据URL得到servlet来进行处
		// 反正这个监控队列暂时不做，就这样
		if (uri.equals("/monitor.html")) {
			sendStaticResource(socketChannel, servletName);
		} else if (uri.equals("/test")) {
			// MQMonitorInfo.send(out);
			MQMonitorInfo.send(socketChannel);
		} else if (uri.equals("/clear")) {
			String msg = "clear success";
			socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
			MQMonitorInfo.clear();
		} else if (uri.equals("/property")) {
			PropertyUtil.send(socketChannel);
		} else if (uri.equals("/shutdown")) {
			String msg = "shut down system succeee.";
			socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
			socketChannel.close();
			System.exit(0);
		} else {
			// sendStaticResource(out, servletName);
			sendStaticResource(socketChannel, servletName);
		}
	}

	private static void sendStaticResource(SocketChannel socketChannel, String uri) throws IOException {
		if (StringUtils.isEmpty(uri)) {
			send404(socketChannel);
			return;
		}

		byte[] bytes = new byte[1024];
		FileInputStream fis = null;
		try {
			StringBuffer sb = new StringBuffer("HTTP/1.1 200 OK\r\n");
			sb.append("Content-Type:text/html;UTF-8\r\n");
			sb.append("\r\n");

			byte[] httpHeader = sb.toString().getBytes();
			// out.write(httpHeader);
			socketChannel.write(ByteBuffer.wrap(httpHeader));

			File file = new File(Constance.WEB_ROOT, uri);
			System.out.println("HTTP请求HTML页面，文件路径是" + file.getAbsolutePath());
			LOG.error("HTTP请求HTML页面，文件路径是" + file.getAbsolutePath());
			fis = new FileInputStream(file);

			int ch = fis.read(bytes, 0, 1024);
			while (ch != -1) {
				// out.write(bytes, 0, ch);
				socketChannel.write(ByteBuffer.wrap(Arrays.copyOfRange(bytes, 0, ch)));
				ch = fis.read(bytes, 0, 1024);
			}
		} catch (FileNotFoundException e) {
			send404(socketChannel);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}

	}

	public static void sendStaticResource(OutputStream out, String uri) throws IOException {
		if (StringUtils.isEmpty(uri)) {
			send404(out);
			return;
		}

		File file = new File(Constance.WEB_ROOT, uri);
		FileInputStream fis = null;
		byte[] bytes = new byte[1024];

		try {
			fis = new FileInputStream(file);

			sendHTTPHeader(out);
			int ch = fis.read(bytes, 0, 1024);
			while (ch != -1) {
				out.write(bytes, 0, ch);
				ch = fis.read(bytes, 0, 1024);
			}
		} catch (FileNotFoundException e) {
			send404(out);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	public static void sendHTTPHeader(OutputStream out) throws IOException {
		if (out == null) {
			return;
		}

		StringBuffer sb = new StringBuffer("HTTP/1.1 200 OK\r\n");
		sb.append("Content-Type:text/html;UTF-8\r\n");
		sb.append("\r\n");

		byte[] httpHeader = sb.toString().getBytes();
		out.write(httpHeader);

	}

	public static void send404(OutputStream out) throws IOException {
		if (out == null) {
			return;
		}
		String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
				+ "Content-Length: 23\r\n" + "\r\n" + "<h1>Nothing Found! 404 </h1>";
		out.write(errorMessage.getBytes());

	}

	private static void send404(SocketChannel socketChannel) throws IOException {
		if (socketChannel == null) {
			return;
		}
		String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
				+ "Content-Length: 23\r\n" + "\r\n" + "<h1>Nothing Found! 404</h1>";
		socketChannel.write(ByteBuffer.wrap(errorMessage.getBytes()));

	}

	/**
	 * 
	 * @Description：解析HTTP的请求URL @param requestString @return: 返回结果描述 @return
	 *                           String: 返回值类 @throws
	 */
	public static String parseUri(String requestString) {
		if (StringUtils.isEmpty(requestString)) {
			return null;
		}

		int index1, index2;
		index1 = requestString.indexOf(' ');
		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1)
				return requestString.substring(index1 + 1, index2);
		}
		return null;
	}

}
