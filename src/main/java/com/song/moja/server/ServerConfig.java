package com.song.moja.server;

import java.util.Properties;

import com.song.moja.util.Utils;

/**
 * 关于服务器Server类的一些配置
 * 
 * @author 3gods.com
 *
 */
public class ServerConfig extends Config {

	public ServerConfig(Properties props) {
		super(props);
	}

	public int getPort() {
		return Utils.getInt(props, "server.port", 8088);
	}

	public int getMaxConnections() {
		return Utils.getInt(props, "max.connections", 10000);
	}

	public int getMaxMessageSize() {
		return Utils.getIntInRange(props, "max.message.size", 1024 * 1024, 0, Integer.MAX_VALUE);
	}

	public int getDefaultFlushIntervalMs() {
		return Utils.getInt(props, "log.default.flush.interval.ms", getFlushSchedulerThreadRate());
	}

	public int getFlushSchedulerThreadRate() {
		return Utils.getInt(props, "log.default.flush.scheduler.interval.ms", 3000);
	}

	public boolean getEnableZookeeper() {
		return Utils.getBoolean(props, "enable.zookeeper", false);
	}

	public String getLogDir() {
		return Utils.getString(props, "log.dir");
	}

	public int getMQInitSize() {
		return Utils.getInt(props, "mq.init.size", 5000);
	}

	public int getEnqueueTimeoutMs() {

		return Utils.getInt(props, "enqueue.timeout", -1);
	}

	/**
	 * 监控队列界面的端口
	 */
	public int getMonitorPort() {

		return Utils.getInt(props, "monitor.port", 8888);
	}

	public String getUsername() {
		return Utils.getString(props, "db.username", "root");
	}

	public String getPassword() {
		return Utils.getString(props, "db.password", "root");
	}

	public String getPBLogClassName() {
		return Utils.getString(props, "pb.log.class", null);

	}

	// 获取日志传输格式，支持protobuf,json，默认支持JSON
	public String getLogTransferType() {
		return Utils.getString(props, "log.transfer.type", "json");
	}

	public int getPersistBatchSize() {
		return Utils.getInt(props, "persist.batch.size", 10000);
	}

	public String getProtoFileDir() {
		return Utils.getString(props, "proto.file.dir", "proto");
	}

}
