package org.xzc.action.model;

import java.util.Date;

/**
 * 用于AddMsgAction的model
 * 应该尽可能多的包含各式各样的属性
 * @author xzchaoo
 *
 */
public class MessageActionModel {
	public int actid;
	public String by;
	public String content;
	public Date endTime;
	public int id;
	public String keyword;
	public String link;
	public String location;
	public int maxMember;
	public int memberCount;
	public String name;
	public int offset = 0;
	public int order;
	public String creator;
	public int groupId;
	
	public final int getGroupId() {
		return groupId;
	}

	public final void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String photolink;

	public Date signEndTime;
	public Date signStartTime;
	public int size = 10;
	public String source;
	public Date startTime;
	public int type = 0;
	public int type2;
	public int creatorId;
	
	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public int getActid() {
		return actid;
	}
	
	public String getBy() {
		return by;
	}

	public String getContent() {
		return content;
	}

	public Date getEndTime() {
		return endTime;
	}
	
	public int getId() {
		return id;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getLink() {
		return link;
	}

	public String getLocation() {
		return location;
	}

	public int getMaxMember() {
		return maxMember;
	}

	public int getMemberCount() {
		return memberCount;
	}

	public String getName() {
		return name;
	}

	public int getOffset() {
		return offset;
	}

	public int getOrder() {
		return order;
	}

	public String getPhotolink() {
		return photolink;
	}

	public Date getSignEndTime() {
		return signEndTime;
	}

	public Date getSignStartTime() {
		return signStartTime;
	}

	public int getSize() {
		return size;
	}

	public String getSource() {
		return source;
	}

	public Date getStartTime() {
		return startTime;
	}

	public int getType() {
		return type;
	}

	public int getType2() {
		return type2;
	}

	public void setActid(int actid) {
		this.actid = actid;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setMaxMember(int maxMember) {
		this.maxMember = maxMember;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setPhotolink(String photolink) {
		this.photolink = photolink;
	}

	public void setSignEndTime(Date signEndTime) {
		this.signEndTime = signEndTime;
	}

	public void setSignStartTime(Date signStartTime) {
		this.signStartTime = signStartTime;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setType2(int type2) {
		this.type2 = type2;
	}

}
