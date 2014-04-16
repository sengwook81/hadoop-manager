package org.zero.hadoop.manager.util;

import java.util.List;

public class ListWrap<T> {
	List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ListWrap [list=" + list + "]";
	}
}