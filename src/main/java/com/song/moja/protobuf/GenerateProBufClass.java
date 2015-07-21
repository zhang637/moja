package com.song.moja.protobuf;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;



/**
 * 
*<p>文件名称:GenerateProBufClass.java
*<p>
*<p>文件描述:本类描述
*<p>版权所有:湖南省蓝蜻蜓网络科技有限公司版权所有(C)2015</p>
*<p>内容摘要:简要描述本文件的内容，包括主要模块、函数及能的说明
</p>
*<p>调用protoc.exe生成java数据访问类
</p>
*<p>完成日期:2015年4月23日上午9:34:39</p>
*<p>
*@author songxin
 */
public class GenerateProBufClass {
	
//	public static final String STRCMD = "protoc.exe --java_out=./src/main/java ./proto/" ;
	private static Logger LOG = Logger.getLogger(GenerateProBufClass.class);
	
	public static void main(String[] args) throws Exception {
		//1.遍历存放proto文件的文件夹
		
		//2.调用方法生成类
		String protoFile = "log.proto";
		generate(protoFile);
		/**
		//序列化
		DebugLogProbuf.DebugLog.Builder builder = DebugLogProbuf.DebugLog.newBuilder();
		
		builder.setClassName("ComInfoPlugin");
		builder.setIp("192.168.139.133");
		builder.setServiceId("comInfoPlugin");
		builder.setErrMsg("这是个文本文件");
		
		DebugLog logSrc = builder.build();
		byte[] byteArr = logSrc.toByteArray();
		ByteArrayInputStream input = new ByteArrayInputStream(byteArr);
		
		//然后将这个二进制流发送出去
		
		//反序列化
		DebugLogProbuf.DebugLog logDest = DebugLogProbuf.DebugLog.parseFrom(input);
		
		System.out.println(logDest.getClassName());
		System.out.println(logDest.getServiceId());
		System.out.println(logDest.getIp());
		**/
	}

	/**
	 * DESC 此方法用于将一个文件转换成对象
	 * @param protoFile
	 *            要生成类的数据文件
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void generate(String protoFile) throws InterruptedException,
			IOException {

		String strCmd = "protoc.exe --java_out=./src/main/java ./proto/"
				+ protoFile;
		Process p = Runtime.getRuntime().exec("cmd /c " + strCmd);// 通过执行cmd命令调用protoc.exe程序
			
		int result = p.waitFor();

		if (result != 0) {
			if (p.exitValue() == 1) {// p.exitValue()==0表示正常结束，1：非正常结束
				System.err.println("命令执行失败!");
				System.exit(1);
			}else if(p.exitValue() == 0){
				System.out.println("命令执行成功!");
			}
		}
	}
	
	
}
