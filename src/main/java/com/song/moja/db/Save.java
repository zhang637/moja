package com.song.moja.db;

import java.util.List;

public interface Save {
	<T> void save(T t);

	<T> int save(List<T> list);
}
