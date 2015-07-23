package com.song.moja.util;

import java.io.File;

public class Constance {

	
	public static final String MOJA_CONFIG_PATH = "moja.properties";
	
	public static final String MOJA_LOG4J_PATH = "log4j.properties";
	
	public static final String CLEAN_SHUTDOWN_FILE = "shutdown.gracefully";
	
	public static final String LOG_TYPE_PB = "protobuf";
	
	public static final String LOG_TYPE_JSON = "json";
	
	/** http相关**/
	public static final String REQUEST_GET = "GET";

	public static final String REQUEST_POST = "POST";

	public static final String WEB_ROOT = System.getProperty("user.dir")
			+ File.separator + "Webroot";

	
}
