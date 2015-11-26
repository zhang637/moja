package com.song.moja.netty;

import java.util.Properties;

import com.song.moja.server.Config;
import com.song.moja.util.Utils;

public class ResultMsgConfig extends Config{

	public ResultMsgConfig(Properties props) {
		super(props);
	}
	//这里应该是有个结果code的Enum
	public String getSuccMsg(){
		return Utils.getString(props, "success.msg","OK!");
	}
	
	public String getErrMsg(){
		return Utils.getString(props, "params.err.msg","FAILED!");
	}
	public int getSuccCode() {
		return Utils.getInt(props, "success.code",0);
	}
}
