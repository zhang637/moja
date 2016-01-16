package com.song.moja.db;

import java.io.Serializable;
import java.net.UnknownHostException;

import com.mongodb.Mongo;

public class MongoEntity extends Mongo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mongoIp;

	private int mongoPort;

	private String username;

	private String password;

	private String dbname;

	private boolean isAuthenticated;

	public MongoEntity() throws UnknownHostException {
		super();
	}

	public MongoEntity(String mongoIp, int mongoPort, String username, String password, String dbname,
			boolean isAuthenticated) throws UnknownHostException {
		this.mongoIp = mongoIp;
		this.mongoPort = mongoPort;
		this.username = username;
		this.password = password;
		this.dbname = dbname;
		this.isAuthenticated = isAuthenticated;
	}

	public String getMongoIp() {
		return mongoIp;
	}

	public void setMongoIp(String mongoIp) {
		this.mongoIp = mongoIp;
	}

	public int getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
}
