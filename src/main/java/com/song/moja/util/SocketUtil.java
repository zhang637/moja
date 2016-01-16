package com.song.moja.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * <p>
 * 文件名称:SocketUtil.java
 * <p>
 * <p>
 * 文件描述:socket流工具类
 * <p>
 * 内容摘要:简要描述本文件的内容，包括主要模块、函数及能的说明
 * </p>
 * <p>
 * 其他说明:其它内容的说明
 * </p>
 * <p>
 */
public class SocketUtil {
	private static Logger LOG = Logger.getLogger(SocketUtil.class);

	// 从流中读取byte[]
	public static byte[] readBytesFromStream(InputStream in) throws IOException {
		byte len[] = new byte[1024];// 1024的缓冲刘
		int tempCount = 0;
		int count = 0;
		try {
			count = (tempCount = in.read(len)) > 0 ? tempCount : 0;
		} catch (IOException e) {
			// 捕获这个异常却不做任何处理是因为客户端发送过来available，send(0xFF)这种指令的时候就不去管它
			e.printStackTrace();
			throw new IOException(e);
		}
		byte[] temp = new byte[count];
		for (int i = 0; i < count; i++) {
			temp[i] = len[i];
		}
		return temp;
	}

	// 从流中读取byte[]，这个方法是阻塞的。也就是像死机了一下在这里不动，但是设置了socketTimeOut就行了，会抛出异常。
	public static byte[] readBytesFromStream2(InputStream in) throws IOException {
		byte len[] = new byte[1024];
		int count = 0;
		// while((len=object.read())!=-1){
		if ((count = in.read(len)) != -1) {
			// count = in.read(len);
			byte[] temp = new byte[count];
			for (int i = 0; i < count; i++) {
				temp[i] = len[i];
			}
			return temp;
		} else {
			return null;
		}
	}

	public static void writeStr2Stream(String str, OutputStream out) throws IOException {
		try {
			BufferedOutputStream writer = new BufferedOutputStream(out);
			if (str != null && str.getBytes().length > 0) {
				writer.write(str.getBytes());
				writer.flush();
			}
		} catch (IOException ex) {
			LOG.error("字符串写输出流异常", ex);
			throw ex;
		}
	}

	public static String readStrFromStream(InputStream in) throws IOException {
		StringBuffer result = new StringBuffer("");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		char[] chars = new char[2048];
		int len;
		try {
			while ((len = reader.read(chars)) != -1) {
				if (2048 == len) {
					result.append(chars);
				} else {
					for (int i = 0; i < len; i++) {
						result.append(chars[i]);
					}
					break;
				}
			}

		} catch (IOException e) {
			LOG.error("从输入流中读字符串异常", e);
			throw e;
		}
		return result.toString();
	}
}
