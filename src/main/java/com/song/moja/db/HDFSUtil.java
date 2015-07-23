package com.song.moja.db;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import com.song.moja.util.PropertyUtil;



public class HDFSUtil {
	private HDFSUtil() {

	}

	private static Logger LOG = Logger.getLogger(HDFSUtil.class);
	private static Configuration conf = new Configuration();

	static {
		Configuration conf = new Configuration();
		// 端口为HDFS默认的，建议不要修改
		conf.set("mapred.job.tracker", PropertyUtil.get("mapred.job.tracker"));
		conf.set("fs.default.name", PropertyUtil.get("fs.default.name"));
		conf.setBoolean("dfs.support.append",
				Boolean.parseBoolean(PropertyUtil.get("dfs.support.append")));
		// conf.set("fs.file.impl",
		// org.apache.hadoop.fs.LocalFileSystem.class.getName() );
		conf.set("fs.hdfs.impl",
				org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		HDFSUtil.conf = conf;
		// 在win上面用eclipse开发需要设置
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			System.setProperty("hadoop.home.dir",
					PropertyUtil.get("hadoop.home.dir"));
		}

	}

	/**
	 * 
	 * @author：
	 * @date：2015年6月17日
	 * @Description：判断文件夹是否存在
	 * @param fileName
	 * @return
	 * @throws Exception
	 *             : 返回结果描述
	 * @return boolean: 返回值类型
	 * @throws
	 */
	public static boolean isDirExist(String fileName) throws Exception {
		FileSystem fs = FileSystem.get(URI.create("/"), conf);
		Path p = new Path(fileName);
		try {
			// 存在并且是文件夹
			if (fs.exists(p) && fs.isDirectory(p)) {
				return true;
			} else {
				return false;
			}
		} finally {
			fs.close();
		}
	}

	// 判断文件是否存在
	public static boolean isFileExist(String fileName) throws Exception {
		FileSystem fs = FileSystem.get(URI.create("/"), conf);
		Path p = new Path(fileName);
		try {
			// 存在并且不是文件夹
			if (fs.exists(p) && !fs.isDirectory(p)) {
				return true;
			} else {
				return false;
			}
		} finally {
			fs.close();
		}
	}

	public static boolean createFile(String file, String content,
			boolean overwrite) throws Exception {
		boolean flag = false;
		FileSystem fs = null;
		FSDataOutputStream os = null;
		try {
			fs = FileSystem.get(URI.create("/"), conf);
			byte[] buff = content.getBytes();
			os = fs.create(new Path(file), overwrite);
			os.write(buff, 0, buff.length);

			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return flag;
	}

	public void traverse(FileSystem fs, String file, PathFilter pathFilter,
			HDFSFileHandler handler) throws Exception {
		Path path = new Path(file);
		_traverse(fs, path, pathFilter, handler);
		fs.close();
	}

	private void _traverse(FileSystem fs, Path path, PathFilter pathFilter,
			HDFSFileHandler handler) throws IOException {
		if (fs.isFile(path)) {
			handler.handler(path);
		} else {
			FileStatus[] list = fs.listStatus(path, pathFilter);
			for (FileStatus stat : list) {
				_traverse(fs, stat.getPath(), pathFilter, handler);
			}
		}
	}

	public static boolean mkdirs(String dir) throws Exception {
		boolean flag = false;
		FileSystem fs = null;
		Path path = null;
		try {
			fs = FileSystem.get(URI.create("/"), conf);
			path = new Path(dir);
			if (!fs.exists(path)) {
				if (!fs.mkdirs(path)) {
					LOG.error("新建文件夹 " + path.toString() + "失败!");
					return flag;
				}
			} else {
				LOG.info("文件夹" + dir + "已经存在");
				return flag;
			}

			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("新建文件夹 " + path.toString() + "失败!");
			throw new Exception("新建文件夹 " + path.toString() + "失败!", e);
		} finally {
			fs.close();
		}
		return flag;
	}

	public static boolean rm(String file, boolean recursive) throws Exception {
		boolean flag = false;
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create("/"), conf);
			Path path = new Path(file);
			if (!fs.delete(path, recursive)) {
				LOG.error("删除文件 " + file + " 失败!");
				return flag;
			}
			flag = true;

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("删除文件 " + file + " 失败!", e);
			throw new Exception("删除文件 " + file + " 失败!", e);
		} finally {
			fs.close();
		}
		return flag;
	}

	public static String cat(String file) throws Exception {
		FileSystem fs = FileSystem.get(URI.create("/"), conf);
		Path path = new Path(file);
		if (!fs.exists(path)) {
			LOG.error(file + " 不存在!");
			return null;
		}
		if (!fs.isFile(path)) {
			LOG.error(file + " 不是一个文件!");
			return null;
		}
		FSDataInputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			is = fs.open(path);
			IOUtils.copyBytes(is, os, 4096);
		} finally {
			IOUtils.closeStream(is);
			IOUtils.closeStream(os);
		}
		fs.close();
		return os.toString();
	}

	public static boolean copyFromLocal(String src, String dst, boolean delSrc,
			boolean overwrite) throws Exception {
		boolean flag = false;
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create("/"), conf);
			File file = new File(src);
			if (!file.exists()) {
				LOG.error(src + "源文件不存在!");
				return flag;
			}
			Path dstPath = new Path(dst);
			fs.copyFromLocalFile(delSrc, overwrite, new Path(src), dstPath);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("拷贝本地文件" + src + "到HDFS出错");
			throw new Exception("拷贝本地文件" + src + "到HDFS出错", e);
		} finally {
			fs.close();
		}
		return flag;

	}

	// 将字符串添加到文件
	public static boolean appendStrToFile(String srcFile, String appendStr)
			throws Exception {
		conf.set("dfs.client.block.write.replace-datanode-on-failure.policy",
				"NEVER");
		conf.set("dfs.client.block.write.replace-datanode-on-failure.enable",
				"true");
		FileSystem fs = null;
		FSDataOutputStream out = null;
		InputStream in = null;
		try {
			fs = FileSystem.get(URI.create(srcFile), conf);

			out = fs.append(new Path(srcFile));
			appendStr = appendStr + "\r\n";
			in = new BufferedInputStream(new ByteArrayInputStream(
					appendStr.getBytes()));
			IOUtils.copyBytes(in, out, 4096);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("添加字符串" + appendStr + "到HDFS文件" + srcFile + "出错！", e);
			throw new Exception("添加字符串" + appendStr + "到HDFS文件" + srcFile
					+ "出错！", e);
		} finally {
			out.close();
			IOUtils.closeStream(in);
			fs.close();
		}
		return true;
	}

	// 将本地文件添加到hdfs
	public static boolean app2File(String srcFile, String appendFile)
			throws Exception {
		boolean flag = false;
		// 先读出文件内容，然后2个合并，然后将原来文件删除，然后创建文件
		conf.set("dfs.client.block.write.replace-datanode-on-failure.policy",
				"NEVER");
		conf.set("dfs.client.block.write.replace-datanode-on-failure.enable",
				"true");

		FileSystem fs = null;
		FSDataOutputStream out = null;
		InputStream in = null;
		try {
			fs = FileSystem.get(URI.create(srcFile), conf);
			out = fs.append(new Path(srcFile));
			in = new BufferedInputStream(new FileInputStream(appendFile));
			IOUtils.copyBytes(in, out, 4096);

			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("添加文件" + appendFile + "到HDFS文件" + srcFile + "出错！", e);
			throw new Exception("添加文件" + appendFile + "到HDFS文件" + srcFile
					+ "出错！", e);
		} finally {
			out.close();
			IOUtils.closeStream(in);
			fs.close();
		}
		return flag;
	}

	public static void main(String[] args) throws Exception {

		// rm("hdfs://vm131:9000//user/Administrator/usr/helloworld", false);
	}

	public static <T> boolean writeLogList2File(List<T> logList,
			String absolutePath) {
		conf.set("dfs.client.block.write.replace-datanode-on-failure.policy",
				"NEVER");
		conf.set("dfs.client.block.write.replace-datanode-on-failure.enable",
				"true");
		FileSystem fs = null;
		FSDataOutputStream out = null;
		InputStream in = null;
		try {
			fs = FileSystem.get(URI.create(absolutePath), conf);

			out = fs.append(new Path(absolutePath));

			// appendStr = appendStr +"\r\n" ;
			for (T commonLog : logList) {
//				in = new BufferedInputStream(new ByteArrayInputStream(
//						commonLog.toByteArray()));

				IOUtils.copyBytes(in, out, 4096);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				IOUtils.closeStream(in);
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	public static boolean saveByteArr(byte[] byteArr, String absolutePath) {
		conf.set("dfs.client.block.write.replace-datanode-on-failure.policy",
				"NEVER");
		conf.set("dfs.client.block.write.replace-datanode-on-failure.enable",
				"true");
		FileSystem fs = null;
		FSDataOutputStream out = null;
		InputStream in = null;
		try {
			fs = FileSystem.get(URI.create(absolutePath), conf);

			out = fs.append(new Path(absolutePath));

			// appendStr = appendStr +"\r\n" ;
			in = new BufferedInputStream(new ByteArrayInputStream(
					byteArr));

			IOUtils.copyBytes(in, out, 4096);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("到HDFS文件" + absolutePath + "出错！", e);
		} finally {
			try {
				out.close();
				IOUtils.closeStream(in);
				fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
}
