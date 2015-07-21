package com.song.moja.http;

import java.io.File;

public class HttpConstants {
	public static final String REQUEST_GET = "GET";

	public static final String REQUEST_POST = "POST";

	public static final String WEB_ROOT = System.getProperty("user.dir")
			+ File.separator + "Webroot";

	public static void main(String[] args) {
		System.out.println(HttpConstants.WEB_ROOT);
	}
}