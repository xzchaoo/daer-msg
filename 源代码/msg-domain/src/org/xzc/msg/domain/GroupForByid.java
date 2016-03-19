package org.xzc.msg.domain;

import java.util.Date;
import java.util.List;

/**
 * 用于byid的 group
 * @author xzchaoo
 *
 */
public class GroupForByid {
	public Date createTime;
	public String desc;
	public int id;
	public int memberCount;
	public List<IdAndName> members;
	public List<Message3> messages;
	public IdAndName creator;
	
	public IdAndName getCreator() {
		return creator;
	}

	public void setCreator(IdAndName creator) {
		this.creator = creator;
	}

	public String name;

	public Date getCreateTime() {
		return createTime;
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

	public List<IdAndName> getMembers() {
		return members;
	}

	public List<Message3> getMessages() {
		return messages;
	}

	public String getName() {
		return name;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public void setMembers(List<IdAndName> members) {
		this.members = members;
	}

	public void setMessages(List<Message3> messages) {
		this.messages = messages;
	}

	public void setName(String name) {
		this.name = name;
	}
}
