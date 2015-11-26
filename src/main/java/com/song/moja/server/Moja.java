package com.song.moja.server;

import java.io.Closeable;
import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.song.moja.util.Constance;
import com.song.moja.util.PropertyUtil;
import com.song.moja.util.Utils;

/**
 * 程序入口类，主要是加载配置文件，添加hook线程
 * 
 * @author 3gods.com
 *
 */
public class Moja implements Closeable {
	private final Logger LOG = Logger.getLogger(Moja.class);

	private volatile Thread shutdownHook;
	private int port = -1;
	private Server server;

	public static void main(String[] args) {
		int argsSize = args.length;
		String mojaConfigPath = null;
		String log4jConfigPath = null;
		if (argsSize <= 0) {
			System.out.println("没有配置日志系统配置文件，系统正在退出!!!" + "如要使用默认参数，请使用DEFAULT参数");
			System.exit(1);
		}

		if (argsSize == 1 && args[0].equalsIgnoreCase("DEFAULT")) {
			System.out.println("采用系统默认配置参数，在当前文件夹获取配置文件!!!");
			mojaConfigPath = Constance.MOJA_CONFIG_PATH;
			log4jConfigPath = Constance.MOJA_LOG4J_PATH;
		}

		if (argsSize == 2) {
			mojaConfigPath = args[0];
			log4jConfigPath = args[1];
			System.out.println("采用自定义配置参数文件!!!" + "mojaConfigPath的路径是:" + mojaConfigPath + "log4jConfigPath的路径是:"
					+ log4jConfigPath);
		}

		Moja moja = new Moja();
		moja.start(mojaConfigPath, log4jConfigPath);
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

	private void start(String mojaConfigPath, String log4jConfigPaht) {
		File mojaPropFile = Utils.getCanonicalFile(new File(mojaConfigPath));
		File log4jPropFile = Utils.getCanonicalFile(new File(log4jConfigPaht));

		if (!mojaPropFile.isFile() || !mojaPropFile.exists() || !log4jPropFile.isFile() || !log4jPropFile.exists()) {
			System.err.println(String.format("moja.properties或log4j.properties不存在,获取到的路径是 => '%s'",
					new Object[] { mojaPropFile.getAbsolutePath(), log4jPropFile.getAbsolutePath() }));
			System.exit(2);
		}
		PropertyUtil.init(mojaConfigPath);

		start(Utils.loadProps(mojaConfigPath), Utils.loadProps(log4jConfigPaht));
	}

	private void start(Properties mojaProp, Properties log4jProp) {
		// 加载日志服务配置
		final ServerConfig config = new ServerConfig(mojaProp);
		// 加载log4j配置
		PropertyConfigurator.configure(log4jProp);

		server = new Server(config);
		// 日志系统关闭时调用的hook线程
		shutdownHook = new Thread() {
			@Override
			public void run() {
				server.close();
				try {
					server.awaitShutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
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
				// ignore shutting down status
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
