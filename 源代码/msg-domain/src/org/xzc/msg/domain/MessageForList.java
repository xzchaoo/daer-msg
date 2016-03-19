package org.xzc.msg.domain;

import java.util.Date;

/**
 * 用于list的消息 只需要一些简单的属性
 * @author xzchaoo
 *
 */
public class MessageForList {
	/**
	 * 创建的时间
	 */
	public Date createTime;
	public Date startTime;
	public Date endTime;
	public String location;
	public final Date getStartTime() {
		return startTime;
	}
	public final void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public final Date getEndTime() {
		return endTime;
	}
	public final void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public final String getLocation() {
		return location;
	}
	public final void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 创建者的简单信息
	 */
	public IdAndName creator;
	
	public int id;

	public String name;

	public int type;
	
	public Date getCreateTime() {
		return createTime;
	}
	public IdAndName getCreator() {
		return creator;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setCreator(IdAndName creator) {
		this.creator = creator;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

}
