package org.xzc.msg.domain;

import java.util.List;

/**
 * 一个组的所有信息
 * @author xzchaoo
 *
 */
public class GroupFullInfo extends Group {
	public User creator;
	public List<IdAndName> members;

	public User getCreator() {
		return creator;
	}

	public List<IdAndName> getMembers() {
		return members;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public void setMembers(List<IdAndName> members) {
		this.members = members;
	}
}
