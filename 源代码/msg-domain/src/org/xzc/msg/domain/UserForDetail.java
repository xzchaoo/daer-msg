package org.xzc.msg.domain;

import java.util.List;

/**
 * 用于显示一个用户的详情
 * @author xzchaoo
 *
 */
public class UserForDetail {
	public String desc;
	//用户的基本信息
	public int id;
	//参加的组
	public List<IdAndName> joinGroups;

	//用户最近发过的消息
	public List<Message3> publishMessages;
	public String name;
	//拥有的组
	public List<IdAndName> ownGroups;
	public String phone;

	public String qq;

	public String weixin;

	public int publishMessageCount;
	
	public int getPublishMessageCount() {
		return publishMessageCount;
	}

	public void setPublishMessageCount(int publishMessageCount) {
		this.publishMessageCount = publishMessageCount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<IdAndName> getJoinGroups() {
		return joinGroups;
	}

	public void setJoinGroups(List<IdAndName> joinGroups) {
		this.joinGroups = joinGroups;
	}

	public List<Message3> getPublishMessages() {
		return publishMessages;
	}

	public void setPublishMessages(List<Message3> publishMessages) {
		this.publishMessages = publishMessages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IdAndName> getOwnGroups() {
		return ownGroups;
	}

	public void setOwnGroups(List<IdAndName> ownGroups) {
		this.ownGroups = ownGroups;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

}
