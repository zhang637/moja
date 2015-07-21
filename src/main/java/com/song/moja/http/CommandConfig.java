package com.song.moja.http;

import java.util.Properties;
import com.song.moja.server.Config;
import com.song.moja.util.Utils;

public class CommandConfig extends Config{

	public CommandConfig(Properties props) {
		super(props);
	}

	public String getShutDown(){
		return Utils.getString(props, "command.shutdown","shutdown");
	}
	
	public String getPage(){
		return Utils.getString(props, "command.monitor","monitor");
	}

	public String getSnapshoot(){
		return Utils.getString(props, "command.snapshoot","snapshoot");
	}
	
	public String getProperties(){
		return Utils.getString(props, "command.properties","properties");
	}
}
