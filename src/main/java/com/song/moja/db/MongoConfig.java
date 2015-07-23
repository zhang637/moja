package com.song.moja.db;

import java.util.Properties;

import com.mongodb.MongoOptions;
import com.song.moja.server.Config;
import com.song.moja.util.Utils;

public class MongoConfig extends Config {


	public MongoConfig(Properties props) {
		super(props);
	}

	public String getIp() {
		return Utils.getString(this.props, "mongo.ip2", "127.0.0.1");
	}
	public int getPort() {
		return Utils.getInt(props, "mongo.port2", 27017);
	}

	public String getUsername() {
		return Utils.getString(props, "mongo.username2", "root");
	}

	public String getPassword() {
		return Utils.getString(props, "mongo.password2", "root");
	}

	public String getDbname() {
		return Utils.getString(props, "mongo.dbname2", "nysyslogs");
	}

	public MongoOptions getMongoOptions() {
		MongoOptions options = new MongoOptions();

		options.autoConnectRetry = Boolean.valueOf(props
				.getProperty("autoConnectRetry"));
		options.connectionsPerHost = Integer.valueOf(props
				.getProperty("connectionsPerHost"));
		options.maxWaitTime = Integer.valueOf(props.getProperty("maxWaitTime"));
		options.socketTimeout = Integer.valueOf(props
				.getProperty("socketTimeout"));
		options.connectTimeout = Integer.valueOf(props
				.getProperty("socketTimeout"));
		options.threadsAllowedToBlockForConnectionMultiplier = Integer
				.valueOf(props
						.getProperty("threadsAllowedToBlockForConnectionMultiplier"));

		return options;
	
	}

}
