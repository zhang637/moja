package com.song.moja.zk;

import java.util.Properties;

import com.song.moja.server.Config;

public class ZkConfig extends Config{

	public ZkConfig(Properties props) {
		super(props);
	}
	
	public String getZkConnect(){
		return null;
	}
	
	public int getZkSessionTimeOut(){
		return getZkSessionTimeOut(props,"zk.session.timeout",5000);
	}


	private int getZkSessionTimeOut(Properties props, String string, int i) {
		return 1;
	}
}
