package org.xzc.action.model;

/**
 * 用于LoginAction的model
 * @author xzchaoo
 *
 */
public class UserActionModel {
	public String desc;
	public int id;
	public String name;
	public String newPassword;
	public String oldPassword;
	public String password;
	public String phone;
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

	public String getNewPassword() {
		return newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
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

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
}
