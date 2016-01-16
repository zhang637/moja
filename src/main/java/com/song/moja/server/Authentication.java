package com.song.moja.server;

/**
 * 用于验证，比如在监控页面关闭系统时候加验证，连接mongodb等验证
 * 
 * @author 3gods.com
 *
 */
public class Authentication {
	private String username;

	private String password;
	// 加密类型，可以根据填入的密码，使用这个加密类型加密，然后与数据库或者配置文件中的比对，
	// 这里没有具体实现
	private String cryptoType;

	private ServerConfig config;

	public Authentication(String username, String password, String cryptoType) {
		this.username = username;
		this.password = password;
		this.cryptoType = cryptoType;
	}

	public boolean authentica(String username, String password, String cryptoType) {
		// if(!StringUtils.isEmpty(cryptoType)){
		// //对密码加密
		//
		// }
		if (username.equals(config.getUsername()) && password.equals(config.getPassword())) {
			return true;
		} else {
			return false;
		}

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

	public String getCryptoType() {
		return cryptoType;
	}

	public void setCryptoType(String cryptoType) {
		this.cryptoType = cryptoType;
	}
}
