package com.song.moja.log;

import java.util.Properties;

import com.song.moja.server.Config;
import com.song.moja.util.Utils;
/**
 * 关于日志的配置
 * @author 3gods.com
 *
 */
public class LogConfig  extends Config{

	public LogConfig(Properties props) {
		super(props);
	}
	
	public String getDir(){
		return Utils.getString(props, "log.dir", "/logs");
	}
	
	public String getPrefix(){
		return Utils.getString(props,"log.prefix","log_");
	}
	
	public int getMaxSize(){
		return Utils.getInt(props,"log.maxsize",16);
	}
}
