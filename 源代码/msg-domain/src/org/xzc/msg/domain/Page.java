package org.xzc.msg.domain;

import java.util.List;

/**
 * 分页
 * @author xzchaoo
 *
 */
public class Page<T> {
	public List<T> list;
	public int offset;
	public int size;
	public int total;

	public List<T> getList() {
		return list;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}

	public int getTotal() {
		return total;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
