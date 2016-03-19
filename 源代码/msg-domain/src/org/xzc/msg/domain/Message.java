package org.xzc.msg.domain;

import java.util.Date;

public class Message {
	
	public int id;
	public Date createTime;
	
	/**
	 * 创建者的id
	 */
	public int creatorId;
	
	/**
	 * 类型 type和from是一一对应的
	 */
	public int type;
	public String from;
	
	/**
	 * 消息的名字和内容
	 */
	public String name;
	public String content;
	
	/**
	 * 消息的相关地点和时间
	 */
	public String location;
	public Date startTime;
	public Date endTime;
	
	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getFrom() {
		return from;
	}

	public int getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public int getType() {
		return type;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setType(int type) {
		this.type = type;
	}

	public final int getCreatorId() {
		return creatorId;
	}

	public final void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

}
