package com.song.moja.http;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.song.moja.util.Constance;
import com.song.moja.util.FileUtils;

public class HTTPResult {

	public static byte[] get404Result() throws IOException {

		String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
				+ "Content-Length: 23\r\n" + "\r\n" + "<h1>Nothing Found! 404 </h1>";

		return errorMessage.getBytes();
	}

	public static byte[] getHTTPHeader() throws IOException {
		StringBuffer sb = new StringBuffer("HTTP/1.1 200 OK\r\n");
		sb.append("Content-Type:text/html;UTF-8\r\n");
		sb.append("\r\n");

		return sb.toString().getBytes();
	}

	public static byte[] getUriResult(String uri) throws IOException {
		if (StringUtils.isEmpty(uri)) {
			return get404Result();
		}

		File file = new File(Constance.WEB_ROOT, uri);
		byte[] fileBytes = null;
		try {
			fileBytes = FileUtils.getBytes(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			fileBytes = get404Result();
		}

		return fileBytes;
	}
}
