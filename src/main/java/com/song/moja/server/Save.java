package com.song.moja.server;

import java.util.List;

public interface Save {

	<T> int save(T t);
	<T> int save(List<T> list);
	
}
