package org.xzc.msg.domain;

import java.io.Serializable;

public class User implements Serializable {
	public String desc;
	public int id;
	public String name;
	public String password;
	public String phone;
	public int publishMessageCount;
	public String qq;
	public String weixin;
	
	public String getDesc() {
		return desc;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public int getPublishMessageCount() {
		return publishMessageCount;
	}

	public String getQq() {
		return qq;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPublishMessageCount(int publishMessageCount) {
		this.publishMessageCount = publishMessageCount;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

}
