package org.xzc.msg.domain;

import java.util.Date;

public class SimpleMessageForPublish {
	public String content;
	public Date endTime;
	public int groupId;
	public String location;
	public String name;
	public Date startTime;
	
	public String getContent() {
		return content;
	}
	public Date getEndTime() {
		return endTime;
	}

	public final int getGroupId() {
		return groupId;
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

	public void setContent(String content) {
		this.content = content;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public final void setGroupId(int groupId) {
		this.groupId = groupId;
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
}
