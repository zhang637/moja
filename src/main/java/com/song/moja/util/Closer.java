package com.song.moja.util;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;

import org.apache.log4j.Logger;
/**
 * 
 * @author 3gods.com
 *
 */
public final class Closer {
	private static final Logger LOG = Logger.getLogger(Closer.class);

	public static void close(java.io.Closeable closeable) throws IOException {
		close(closeable, LOG);
	}

	public static void close(java.io.Closeable closeable, Logger logger)
			throws IOException {
		if (closeable == null)
			return;
		try {
			closeable.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void closeQuietly(Selector selector) {
		closeQuietly(selector, LOG);
	}

	public static void closeQuietly(java.io.Closeable closeable, Logger logger) {
		if (closeable == null)
			return;
		try {
			closeable.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void closeQuietly(java.nio.channels.Selector closeable,
			Logger logger) {
		if (closeable == null)
			return;
		try {
			closeable.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void closeQuietly(Socket socket) {
		if (socket == null)
			return;
		try {
			socket.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static void closeQuietly(ServerSocket serverSocket) {
		if (serverSocket == null)
			return;
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static void closeQuietly(java.io.Closeable closeable) {
		closeQuietly(closeable, LOG);
	}
}
