package com.song.moja.protobuf;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.song.moja.server.ServerConfig;

/**
 * 生成.proto文件对应的class的类
 * 
 * @author 3gods.com
 *
 */
public class GenerateProBufClass {
	// public static final String STRCMD = "protoc.exe
	// --java_out=./src/main/java ./proto/" ;
	private static Logger LOG = Logger.getLogger(GenerateProBufClass.class);

	final ServerConfig config;

	public GenerateProBufClass(ServerConfig serverConfig) {
		this.config = serverConfig;
	}

	public void start() {
		// 1.遍历存放proto文件的文件夹
		String protoDir = config.getProtoFileDir();
		File f = new File(protoDir);
		// for(f.listFiles()
		// 2.调用方法生成类
	}

	public static void main(String[] args) throws Exception {
		String protoFile = "log.proto";
		generate(protoFile);
	}

	/**
	 * DESC 此方法用于将一个文件转换成对象
	 * 
	 * @param protoFile
	 *            要生成类的数据文件
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static boolean generate(String protoFile) throws InterruptedException, IOException {
		boolean flag = false;

		String strCmd = "protoc.exe --java_out=./src/main/java ./proto/" + protoFile;
		Process p = Runtime.getRuntime().exec("cmd /c " + strCmd);// 通过执行cmd命令调用protoc.exe程序

		int result = p.waitFor();

		if (result != 0) {
			if (p.exitValue() == 1) {// p.exitValue()==0表示正常结束，1：非正常结束
				System.err.println("命令执行失败!");
				flag = false;
				// System.exit(1);
			} else if (p.exitValue() == 0) {
				System.out.println("命令执行成功!");
				flag = true;
			}
		}
		return flag;
	}

}
