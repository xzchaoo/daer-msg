package org.xzc.msg.action.json;

import org.xzc.msg.action.code.Code;

public class MyJSON {
	private Code code;
	private Object result;
	private String msg;
	
	public MyJSON code(Code code) {
		this.code = code;
		return this;
	}
	public Code code(){
		return this.code;
	}
	public MyJSON result(Object result) {
		this.result = result;
		return this;
	}

	public MyJSON msg(String msg) {
		this.msg = msg;
		return this;
	}

	public int getCode() {
		return code.value;
	}

	public Object getResult() {
		return result;
	}

	public String getMsg() {
		return msg;
	}

}
