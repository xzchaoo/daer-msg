package org.xzc.msg.domain;

import java.util.Date;

/**
 * 一个群组 用户可以订阅某些群组的信息
 * 同一个创建者不能拥有两个同名的组
 * @author xzchaoo
 *
 */
public class Group {
	public Date createTime;
	public int creatorId;
	public String desc;
	public int id;
	public int memberCount;
	public int messageCount;
	public String name;
	public Date getCreateTime() {
		return createTime;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public String getDesc() {
		return desc;
	}

	public int getId() {
		return id;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public final int getMessageCount() {
		return messageCount;
	}

	public String getName() {
		return name;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public final void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public void setName(String name) {
		this.name = name;
	}

}
