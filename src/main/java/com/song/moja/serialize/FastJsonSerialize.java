package com.song.moja.serialize;

import java.io.IOException;

import com.alibaba.fastjson.JSON;

/**
 * 
*<p>文件名称:FastJsonSerialize.java
*<p>
*<p>文件描述:本类描述
*<p>版权所有:湖南省蓝蜻蜓网络科技有限公司版权所有(C)2015</p>
*<p>内容摘要:使用fastjson来进行序列化和反序列化
</p>
*<p>其他说明:其它内容的说明
</p>
*<p>完成日期:2015年6月16日上午10:49:01</p>
*<p>
*@author
 */
public class FastJsonSerialize {

	public static byte[] serialize(Object obj) throws IOException {
		
		return JSON.toJSONString(obj).getBytes();
	}

	public static Object deserialize(byte[] data) throws IOException,
			ClassNotFoundException {
		
		return JSON.parseObject(new String(data), Object.class);  
	}
	
	public static void main(String[] args) {
		
		
		
	}
}
