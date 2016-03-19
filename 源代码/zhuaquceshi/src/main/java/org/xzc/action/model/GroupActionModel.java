package org.xzc.action.model;

public class GroupActionModel {
	public String by;
	public String desc;
	public int id;
	public String name;
	public int offset;
	public int order;
	public int size;
	public int userId;
	public int groupId;
	public int creatorId;
	public String keyword;

	public final String getKeyword() {
		return keyword;
	}

	public final void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getDesc() {
		return desc;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
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

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
