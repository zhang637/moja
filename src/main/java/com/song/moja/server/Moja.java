package com.song.moja.server;

import java.io.Closeable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.song.moja.util.Constance;
import com.song.moja.util.Utils;

/**
 * 程序入口类，主要是加载配置文件，添加hook线程
 * 
 * @author zhangyoulei
 *
 */
public class Moja implements Closeable {
	private final Logger LOG = Logger.getLogger(Moja.class);

	private volatile Thread shutdownHook;
	private int port = -1;
	private Server<String> server;

	public static void main(String[] args) {
		String mojaConfigPath = null;
		Moja moja = new Moja();

		mojaConfigPath = Constance.MOJA_CONFIG_PATH;
		moja.start(mojaConfigPath);

		moja.awaitShutdown();
		moja.close();
	}

	// 使用CountDownLatch优雅关闭系统，等待相应的线程分别执行完毕;没有使用while死循环监听，使用awaitShutdown方法阻塞
	private void awaitShutdown() {
		try {
			server.awaitShutdown();
		} catch (InterruptedException e) {
			LOG.warn(e.getMessage(), e);
		}
	}

	private void start(String mojaConfigPath) {
		Properties mojaProp = Utils.loadProps(mojaConfigPath);
		// 加载日志服务配置
		final ServerConfig config = new ServerConfig(mojaProp);

		server = new Server<String>(config);
		// 日志系统关闭时调用的hook线程
		shutdownHook = new Thread() {
			@Override
			public void run() {
				server.close();
				try {
					server.awaitShutdown();
				} catch (InterruptedException e) {
					LOG.error(e);
				}
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		port = config.getPort();
		server.startup();
	}

	public synchronized void close() {
		if (shutdownHook != null) {
			try {
				Runtime.getRuntime().removeShutdownHook(shutdownHook);
			} catch (IllegalStateException ex) {
				LOG.warn(ex.getMessage(), ex);
			}
			shutdownHook.run();
			shutdownHook = null;
			port = -1;
		}
	}

	public int getPort() {
		return this.port;
	}

	// 将所有队列中的日志写入到磁盘
	void flush() {
		if (server != null) {
			LOG.info("force flush all messages to disk");
			this.server.getThreadManager().flushAllLogs();
		}
	}
}
