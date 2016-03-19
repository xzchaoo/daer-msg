package org.xzc.msg.domain;

import java.util.Date;

public class GroupForList {
	public IdAndName creator;
	public String desc;
	public int id;
	public int memberCount;
	public int messageCount;
	public String name;
	public Date createTime;
	
	public final Date getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public IdAndName getCreator() {
		return creator;
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

	public int getMessageCount() {
		return messageCount;
	}

	public String getName() {
		return name;
	}

	public void setCreator(IdAndName creator) {
		this.creator = creator;
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

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public void setName(String name) {
		this.name = name;
	}
}
