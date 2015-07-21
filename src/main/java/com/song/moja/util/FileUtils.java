package com.song.moja.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * 
 * <p>
 * 文件名称:FileUtils.java
 * <p>
 * <p>
 * 文件描述:本类描述
 * <p>
 * 版权�?��:湖南省蓝蜻蜓网络科技有限公司版权�?��(C)2015
 * </p>
 * <p>
 * 内容摘要:本地磁盘文件工具�?
 * </p>
 * <p>
 * 其他说明:其它内容的说�?
 * </p>
 * <p>
 * 完成日期:2015�?�?7日上�?:59:43
 * </p>
 * <p>
 * 
 * @author wangwanqiang
 */
public class FileUtils {
	private static Logger LOG = Logger.getLogger(FileUtils.class);

	/**
	 * 创建单个文件夹�?
	 * 
	 * @param dir
	 * @param ignoreIfExitst
	 *            true 表示如果文件夹存在就不再创建了�?false是重新创建�?
	 * @throws IOException
	 */
	public static void createDir(String dir, boolean ignoreIfExitst)
			throws IOException {
		File file = new File(dir);
		if (ignoreIfExitst && file.exists()) {
			return;
		}
		System.out.println(file.mkdir());
		if (file.mkdir() == false) {
			throw new IOException("创建文件夹失败！");
		}

	}

	/**
	 * 删除�?��文件�?
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public static void delFile(String filename) throws IOException {
		File file = new File(filename);
		LOG.trace("Delete file = " + filename);
		if (file.isDirectory()) {
			throw new IOException(
					"IOException -> BadInputException: not a file.");
		}
		if (file.exists() == false) {
			throw new IOException(
					"IOException -> BadInputException: file is not exist.");
		}
		if (file.delete() == false) {
			throw new IOException("Cannot delete file. filename = " + filename);
		}
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与�?
	 * 
	 * @param sPath
	 *            要删除的目录或文�?
	 * @return 删除成功返回 true，否则返�?false�?
	 * @author songxin
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存�?
		if (!file.exists()) { // 不存在返�?false
			return flag;
		} else {
			// 判断是否为文�?
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件�?
	 * @return 单个文件删除成功返回true，否则返回false
	 * @author songxin
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文�?
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔�?
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则�?�?
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文�?包括子目�?
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文�?
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目�?
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		return true;
		// 删除当前目录
		// if (dirFile.delete()) {
		// return true;
		// } else {
		// return false;
		// }
	}

	public static String getPathSeparator() {
		return java.io.File.pathSeparator;
	}

	public static String getFileSeparator() {
		return java.io.File.separator;
	}

	/**
	 * 
	 * @author�?
	 * @date�?015�?�?7�?
	 * @Description：创建文�?
	 * @param fileName
	 * @param content
	 *            : 返回结果描述
	 * @return void: 返回值类�?
	 * @throws
	 */
	public static void createNewFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error("创建文件失败");
			}
		}
	}

	/**
	 * @throws IOException
	 * 
	 * @author�?
	 * @date�?015�?�?7�?
	 * @Description：向�?��文件追加内容
	 * @param filePath
	 * @param content
	 *            : 返回结果描述
	 * @return void: 返回值类�?
	 * @throws
	 */
	public static void append2File(String filePath, String content)
			throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			boolean result = file.createNewFile();
		}
		if (file.isDirectory()) {
			LOG.error("不是文件");
			return;
		}
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write("\r\n" + content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Write content to a fileName with the destEncoding 写文件�?如果此文件不存在就创建一个�?
	 * 
	 * @param content
	 *            String
	 * @param fileName
	 *            String
	 * @param destEncoding
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeFile(String content, String fileName,
			String destEncoding) throws FileNotFoundException, IOException {
		File file = null;
		try {
			file = new File(fileName);
			if (!file.exists()) {
				if (file.createNewFile() == false) {
					throw new IOException("create file '" + fileName
							+ "' failure.");
				}
			}
			if (file.isFile() == false) {
				throw new IOException("'" + fileName + "' is not a file.");
			}
			if (file.canWrite() == false) {
				throw new IOException("'" + fileName + "' is a read-only file.");
			}
		} finally {
			// we dont have to close File here
		}
		BufferedWriter out = null;
		try {
			// FileOutputStream fos = new FileOutputStream(fileName);
			out = new BufferedWriter(new FileWriter(fileName, true));
			out.write(content);
			out.flush();
		} catch (FileNotFoundException fe) {
			LOG.error("Error", fe);
			throw fe;
		} catch (IOException e) {
			LOG.error("Error", e);
			throw e;
		} finally {
			// try {
			// if (out != null)
			// out.close();
			// } catch (IOException ex) {
			// }
		}
	}

	/**
	 * 读取文件的内容，并将文件内容以字符串的形式返回�?
	 * 
	 * @param fileName
	 * @param srcEncoding
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String file2Str(String fileName, String srcEncoding)
			throws FileNotFoundException, IOException {
		File file = null;
		try {
			file = new File(fileName);
			if (file.isFile() == false) {
				throw new IOException("'" + fileName + "' is not a file.");
			}
		} finally {
			// we dont have to close File here
		}
		BufferedReader reader = null;
		try {
			StringBuffer result = new StringBuffer(1024);
			FileInputStream fis = new FileInputStream(fileName);
			reader = new BufferedReader(new InputStreamReader(fis, srcEncoding));
			char[] block = new char[512];
			while (true) {
				int readLength = reader.read(block);
				if (readLength == -1)
					break;// end of file
				result.append(block, 0, readLength);
			}
			return result.toString();
		} catch (FileNotFoundException fe) {
			LOG.error("Error", fe);
			throw fe;
		} catch (IOException e) {
			LOG.error("Error", e);
			throw e;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
			}
		}
	}

	// 将一个文件移动到另外�?��地方 songxin
	public static boolean removeFile2Dir(File srcFile, String dirPath) {
		boolean flag = false;
		File tempDir = new File(dirPath);
		// 如果文件夹不存在
		if (!tempDir.exists()) {
			boolean mktempDir = tempDir.mkdir();
			if (!mktempDir) {
				LOG.error("移动文件" + srcFile + "到文件夹" + dirPath + "新建文件夹出�?");
				return false;
			}
		}

		File fnew = new File(tempDir + "\\" + srcFile.getName());
		// System.out.println("新文件地�?��"+fnew.getAbsolutePath());
		// System.out.println("tempDir"+tempDir+"srcFileName"+new
		// File(srcFile).getName());
		flag = srcFile.renameTo(fnew);
		// System.out.println("移动文件"+srcFile+"到文件夹"+dirPath+"新建文件夹成�?");
		return flag;
	}

	// 将一个文件夹下面的文件移动到另外�?��文件夹下�?song.x
	public static void fileMove(String from, String to) throws Exception {// 移动指定文件夹内的全部文�?
		try {
			File dir = new File(from);
			File[] files = dir.listFiles();// 将文件或文件夹放入文件集
			if (files == null)// 判断文件集是否为�?
				return;
			File moveDir = new File(to);// 创建目标目录
			if (!moveDir.exists()) {// 判断目标目录是否存在
				moveDir.mkdirs();// 不存在则创建
			}
			for (int i = 0; i < files.length; i++) {// 遍历文件�?
				if (files[i].isDirectory()) {// 如果是文件夹或目�?则�?归调用fileMove方法，直到获得目录下的文�?
					fileMove(files[i].getPath(), to + "\\" + files[i].getName());// 递归移动文件
					files[i].delete();// 删除文件�?��原目�?
				}
				File moveFile = new File(moveDir.getPath() + "\\"// 将文件目录放入移动后的目�?
						+ files[i].getName());
				if (moveFile.exists()) {// 目标文件夹下存在的话，删�?
					moveFile.delete();
				}
				files[i].renameTo(moveFile);// 移动文件
				System.out.println(files[i] + " 移动成功");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// 根据当前的时间，判断这个socket应该写入的文件是否存在，不存在就创建，返回文件对象song.x
	/**
	 * @throws IOException
	 * 
	 * @author�?
	 * @date�?015�?�?6�?
	 * @Description：根据当前时间获得文件对象，天为单位
	 * @return
	 * @throws IOException
	 *             : 返回结果描述
	 * @return File: 返回值类�?
	 * @throws
	 */
	public static File getMsgInputFile() throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String currentDate = sdf.format(c.getTime());

		StringBuilder sb = new StringBuilder();
		String dir = sb.append(PropertyUtil.get("log.local.dir")).toString();

		File dirFile = new File(dir);

		String filePath = sb.append(currentDate).toString();

		System.out.println("获取持久化文件" + filePath);
		File f = new File(filePath);
		// 文件夹存�?
		if (dirFile.exists()) {
			// 文件存在
			if (f.exists()) {
				return f;
			}
			// 文件不存�?
			if (f.createNewFile()) {
				return f;
			} else {
				LOG.error("创建文件失败" + f.getAbsolutePath());
				return null;
			}
			// 文件夹不存在
		} else {
			dirFile.mkdirs();
			// 文件存在
			if (f.exists()) {
				return f;
			}
			// 文件不存�?
			if (f.createNewFile()) {
				return f;
			} else {
				LOG.error("创建文件失败" + f.getAbsolutePath());
				return null;
			}
		}

	}

	// 根据当前的时间，判断这个socket应该写入的文件是否存在，不存在就创建，返回文件对象song.x
	public static File getMsgInputFile(String type) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String currentDate = sdf.format(c.getTime());

		StringBuilder sb = new StringBuilder();
		String dir = sb.append(PropertyUtil.get("log.local.dir")).append(type)
				.append("/").toString();

		File dirFile = new File(dir);

		String filePath = sb.append(type).append(currentDate).append(".txt")
				.toString();

		System.out.println("获取持久化文件" + filePath);
		File f = new File(filePath);
		// 文件夹存�?
		if (dirFile.exists()) {
			// 文件存在
			if (f.exists()) {
				return f;
			}
			// 文件不存�?
			if (f.createNewFile()) {
				return f;
			} else {
				LOG.error("创建文件失败" + f.getAbsolutePath());
				return null;
			}
			// 文件夹不存在
		} else {
			dirFile.mkdirs();
			// 文件存在
			if (f.exists()) {
				return f;
			}
			// 文件不存�?
			if (f.createNewFile()) {
				return f;
			} else {
				LOG.error("创建文件失败" + f.getAbsolutePath());
				return null;
			}
		}
	}

	// 根据日志类型，时间间�?
	public static File getMsgInputFile(String type, long timeInterval)
			throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		Calendar c = Calendar.getInstance();
		String currentDate = sdf.format(c.getTime());

		StringBuilder sb = new StringBuilder();
		String dir = sb.append(PropertyUtil.get("log.local.dir")).append(type)
				.append("/").toString();

		File dirFile = new File(dir);

		String filePath = sb.append(type).append(currentDate).append(".txt")
				.toString();

		System.out.println("获取持久化文件" + filePath);
		File f = new File(filePath);
		// 文件夹存�?
		if (dirFile.exists()) {
			// 文件存在
			if (f.exists()) {
				return f;
			}
			// 文件不存�?
			if (f.createNewFile()) {
				return f;
			} else {
				LOG.error("创建文件失败" + f.getAbsolutePath());
				return null;
			}
			// 文件夹不存在
		} else {
			dirFile.mkdirs();
			// 文件存在
			if (f.exists()) {
				return f;
			}
			// 文件不存�?
			if (f.createNewFile()) {
				return f;
			} else {
				LOG.error("创建文件失败" + f.getAbsolutePath());
				return null;
			}
		}
	}

	/**
	 * //根据不同的类型获取不同的日志文件 public static File getMsgInputFile(String type) throws
	 * IOException { SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 * Calendar c = Calendar.getInstance(); String currentDate =
	 * sdf.format(c.getTime());
	 * 
	 * String dir = PropertyUtil.get("log.local.dir");
	 * 
	 * String fileName = new
	 * StringBuilder().append(type).append(currentDate).append
	 * (".txt").toString();
	 * 
	 * String filePath = new StringBuilder().
	 * append(PropertyUtil.get("log.local.dir"))
	 * .append(PropertyUtil.get("log.local.prefix"))
	 * .append(currentDate).append(".txt").toString();
	 * 
	 * LOG.info("获取持久化文�? + filePath); File f = new File(filePath);
	 * 
	 * if (!f.exists()) { f.createNewFile(); }
	 * 
	 * int fileSizeSJ = (int) (f.length()/ 1048576);
	 * 
	 * int fileSize = Integer.valueOf(PropertyUtil.get("debug.log.size"));
	 * if(fileSizeSJ>fileSize){ //新建�?��文件
	 * 
	 * }
	 * 
	 * File[] files = new File(dir).listFiles();
	 * 
	 * List<File> fileList = new ArrayList<File>(); for(int
	 * i=0;i<files.length;i++){ if(files[i].getName().contains(fileName)){
	 * fileList.add(files[i]); } }
	 * 
	 * //sort 以下 // 将文件按照时间排个序 Collections.sort(fileList, new Comparator<File>()
	 * { public int compare(File o1, File o2) { long diff = o1.lastModified() -
	 * o2.lastModified(); if (diff > 0) return 1; else if (diff == 0) return 0;
	 * else return -1; } }); //这样得到的结果是debuglog2015-05-21
	 * 
	 * }
	 **/

	// 获取消费失败的日志目录和文件，最好让他自己定义日志存放的目录和文件名
	public static File getErrMsgInputFile(String type) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String currentDate = sdf.format(c.getTime());

		StringBuilder sb = new StringBuilder();
		// sb.append("D:/msglogs/").append("debug_log_").append(currentDate)
		// .append(".txt");

		sb.append(PropertyUtil.get("log.err.local.dir")).append(type)
				.append("Err").append(currentDate).append(".txt");
		String filePath = sb.toString();
		System.out.println("新建临时文件，持久化消费失败的文件" + filePath);
		File f = new File(filePath);

		if (f.exists()) {

			System.out.println(f.getAbsolutePath() + "文件存在!");
			return f;
		}
		if (f.createNewFile()) {
			System.out.println(f.getAbsolutePath() + "文件不存�?新建文件");
			return f;
		} else {
			LOG.error("创建文件失败" + f.getAbsolutePath());
			return null;
		}
	}

	// 获取日志持久化目�?
	public static String getMsgInputDir() {

		return PropertyUtil.get("log.dir");
	}

	// 获取错误日志存放目录
	public static String getErrMsgDir() {

		return PropertyUtil.get("log.err.local.dir");
	}

	// 获取索引存放目录
	public static String getIndexDir() {

		return PropertyUtil.get("log.dir");
	}

//	/**
//	 * @since 将文件读成消息返�?
//	 * @author song.x
//	 * @param f
//	 * @return
//	 * @throws IOException
//	 * @throws Exception
//	 */
//	public static List<CommonLog> readLogsFromFile(File file)
//			throws IOException {
//		if(file==null||file.length()==0){
//			file.delete();
//			return null;
//		}
//		
//		List<CommonLog> msgList = new ArrayList<CommonLog>(1024);
//		FileInputStream inputStream = null;
//		try {
//			inputStream = new FileInputStream(file);
//		} catch (FileNotFoundException e) {
//			LOG.error("文件不存在!!!"+ file.getAbsolutePath());
//			throw new FileNotFoundException("文件不存在!!!" + file.getAbsolutePath()
//					+ e.getMessage());
//		}
//
//		int count = 0;
//		try {
//			CommonLog commonLog = null;
//			// while ((commonLog = CommonLog.parseFrom(inputStream)) != null) {
//			// 只能使用这个parseDelimitedFrom写入和读出来
//			while ((commonLog = CommonLog.parseDelimitedFrom(inputStream)) != null) {
//				msgList.add(commonLog);
//				count++;
//			}
//		} catch (IOException e) {
//			LOG.error("FileUtils调用convertFile2Msg，读取文件"
//					+ file.getAbsolutePath() + "出错!!!");
//			throw new IOException(e);
//		} finally {
//			try {
//				if (null != inputStream) {
//					inputStream.close();
//				}
//			} catch (IOException e) {
//				LOG.error("FileUtils调用convertFile2Msg，关闭br出错!!!");
//				throw new IOException(e);
//			}
//		}
//		return msgList;
//	}

	/**
	 * 将一个String转换成一条消息，这个依赖于写入消息的方式
	 * 
	 * @author song.x
	 * @param str
	 * @return
	 * @throws InvalidProtocolBufferException
	 * @throws Exception
	 */
//	public static CommonLog convertStr2Msg(String str) {
//		if (StringUtils.isEmpty(str)) {
//			return null;
//		}
//
//		CommonLog commonLog = null;
//		try {
//			commonLog = CommonLog.parseFrom(str.getBytes());
//		} catch (InvalidProtocolBufferException e) {
//			LOG.error("FileUtils调用convertStr2Msg，将" + str + "转换成CommonLog出错!!!");
//			// 不抛出异常，将消费失败的日志记录又不能转换成对象的放到一个map�?
//			// throw new InvalidProtocolBufferException(e.getMessage());
//		}
//		return commonLog;
//	}

//	public static void writeLogs2File(List<CommonLog> logList, File f) {
//
//		OutputStream out = null;
//		try {
//			out = new FileOutputStream(f, true);
//
//			for (int i = 0; i < logList.size(); i++) {
//				logList.get(i).writeTo(out);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (null != out) {
//				try {
//					out.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}

//	public synchronized static void writeLog2File(CommonLog commonLog, File f) {
//		File file = new File("D:/usr/local/msglogs/log/deuglog/err2.log");
//
//		OutputStream out = null;
//		try {
//			out = new FileOutputStream(file);
//			commonLog.writeTo(out);
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (null != out) {
//				try {
//					out.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	/**
	 * 
	 * @author�?
	 * @date�?015�?�?8�?
	 * @Description：获取bitCask以后会持久化到的文件的路�?
	 * @return: 返回结果描述
	 * @return String: 返回值类�?
	 * @throws
	 */
	public static String getBitCaskFilePath() {

		return new StringBuilder()
				.append(PropertyUtil.get("log.local.dir"))
				.append(PropertyUtil.get("log.local.prefix"))
				.append(new SimpleDateFormat("yyyy-MM-dd").format(Calendar
						.getInstance().getTime())).toString();

	}

	/**
	 * 获得指定文件的byte数组
	 * 
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(String filePath) throws IOException {
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

	/**
	 * 根据byte数组，生成文�?
	 */
	public static void getFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + "\\" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * @Description long转文件大小M单位方法
	 * @author temdy
	 * @param bytes
	 * @return
	 */
	public static String bytes2kb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		return returnValue + "";
	}
	
	/**
	 * @Description long转文件大小M单位方法
	 * @author temdy
	 * @param bytes
	 * @return
	 */
	public static String bytes2mb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024*1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		return returnValue + "";
	}
	
	public static void main(String[] args) {
		long l = 898999999998L;
		
		System.out.println(bytes2mb(l));
	}

	public static <T> List<T> readLogsFromFile(File file)  {
		if(file==null||file.length()==0){
			file.delete();
			return null;
		}
		
		List<T> msgList = new ArrayList<T>(1024);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LOG.error("文件不存在!!!"+ file.getAbsolutePath());
//			throw new FileNotFoundException("文件不存在!!!" + file.getAbsolutePath()
//					+ e.getMessage());
		}

		int count = 0;
		try {
			T t = null;
			//将文件读成JSON字符串，将JSON字符串转换成List
			String jsonStr = file2Str(file.getAbsolutePath(),"utf8");
			
			msgList = (List<T>) JSON.parseArray(jsonStr, t.getClass());  
			
		} catch (IOException e) {
			LOG.error("FileUtils调用convertFile2Msg，读取文件"
					+ file.getAbsolutePath() + "出错!!!");
//			throw new IOException(e);
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOG.error("FileUtils调用convertFile2Msg，关闭br出错!!!");
//				throw new IOException(e);
			}
		}
		return msgList;
	}
}
