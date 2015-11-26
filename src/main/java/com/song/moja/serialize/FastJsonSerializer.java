package com.song.moja.serialize;

import java.io.IOException;

import com.alibaba.fastjson.JSON;

public class FastJsonSerializer {

	public static byte[] serialize(Object obj) throws IOException {
		return JSON.toJSONString(obj).getBytes();
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		return JSON.parseObject(new String(data), Object.class);
	}

	public static void main(String[] args) {
	}
}
