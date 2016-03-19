package org.xzc.action.model;

/**
 * 用于UserMessageAction的model
 * @author xzchaoo
 *
 */
public class UserMessageModel {
	public int groupId;
	public int msgId;
	public String by;
	public int order;
	public int type;
	
	public final int getType() {
		return type;
	}

	public final void setType(int type) {
		this.type = type;
	}

	public final String getBy() {
		return by;
	}

	public final void setBy(String by) {
		this.by = by;
	}

	public final int getOrder() {
		return order;
	}

	public final void setOrder(int order) {
		this.order = order;
	}

	public String keyword;
	public final String getKeyword() {
		return keyword;
	}

	public final void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * 用于分页
	 */
	public int offset = 0;

	public int size = 10;

	/**
	 * User Message ID
	 */
	public int umId;

	public int getGroupId() {
		return groupId;
	}

	public int getMsgId() {
		return msgId;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}

	public int getUmId() {
		return umId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setUmId(int umId) {
		this.umId = umId;
	}
}
