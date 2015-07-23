package com.song.moja.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.protobuf.MessageLite;

public class FileUtils {
	private static Logger LOG = Logger.getLogger(FileUtils.class);
	
	/**
	 * @since 将文件读成消息返回
	 * @author song.x
	 * @param f
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static List<MessageLite> readLogsFromFile(File file)
			throws IOException {
		if(file==null||file.length()==0){
			file.delete();
			return null;
		}
		
		List<MessageLite> msgList = new ArrayList<MessageLite>(1024);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LOG.error("文件不存在" + file.getAbsolutePath());
			throw new FileNotFoundException("文件不存在" + file.getAbsolutePath()
					+ e.getMessage());
		}

		int count = 0;
		try {
			MessageLite commonLog = null;
			// while ((commonLog = CommonLog.parseFrom(inputStream)) != null) {
			// 只能使用这个parseDelimitedFrom写入和读出来
			//擦，这样好像就好了。
			while ((commonLog = commonLog.getParserForType().parseDelimitedFrom(inputStream)) != null) {
				msgList.add(commonLog);
				count++;
			}
		} catch (IOException e) {
			LOG.error("FileUtils调用convertFile2Msg，读取文件"
					+ file.getAbsolutePath() + "出错!!!");
			throw new IOException(e);
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOG.error("FileUtils调用convertFile2Msg，关闭br出错!!!");
				throw new IOException(e);
			}
		}
		return msgList;
	}
	/**
	 * 从文件路径读取出byte数组
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(String filePath) throws IOException {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e.toString());
		}
		return buffer;
	}


}
