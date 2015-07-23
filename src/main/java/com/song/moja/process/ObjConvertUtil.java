package com.song.moja.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.MessageLite;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
/**
 * 对象转换类
 * @author 3gods.com
 *
 */
public class ObjConvertUtil<T> {
	
	public static <T> DBObject t2DBObj(T t){
		
		return null;
	}

	//这个地方这个东西不对。
	public static DBObject PB2DBObj(MessageLite ml) {
//		if(null==ml){
//			return null;
//		}
//		
//		Map<FieldDescriptor, Object> map = ml.getAllFields();
//		Map<String, Object> dbMap = new HashMap<String, Object>();
//
//		for (FieldDescriptor fd : map.keySet()) {
//			dbMap.put(fd.getName(), map.get(fd));
//		}
//		return new BasicDBObject(dbMap);
//		
//		
//		
		return null;
	}
	
	public static DBObject JSON2DBObj(Object obj){
		//将obj转换成json，将json转换成map
		if(obj==null){
			return null;
		}
		String jsonStr = JSON.toJSONString(obj);
		Map<String,Object> map = (Map<String,Object>)JSON.parse(jsonStr);
		return new BasicDBObject(map);
	}
	
	
}
