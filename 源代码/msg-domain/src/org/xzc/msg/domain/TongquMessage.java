package org.xzc.msg.domain;

import java.util.Date;

public class TongquMessage extends Message {
	public int actid;
	public int maxMember;
	public int memberCount;
	public String photolink;
	public Date signEndTime;
	public Date signStartTime;
	public int type2;
	public String source;
	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getActid() {
		return actid;
	}
	public int getMaxMember() {
		return maxMember;
	}
	public int getMemberCount() {
		return memberCount;
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

	public int getType2() {
		return type2;
	}

	public void setActid(int actid) {
		this.actid = actid;
	}

	public void setMaxMember(int maxMember) {
		this.maxMember = maxMember;
	}

	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
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

	public void setType2(int type2) {
		this.type2 = type2;
	}

}
