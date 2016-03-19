package org.xzc.msg.site.haodaxue;

public class Stage {
	public String name;
	public String start;
	public String end;

	public Stage(String name, String start, String end) {
		this.name = name;
		this.start = start;
		this.end = end;
	}

	public String toString2() {
		return name + start + end;
	}

}
