package org.xzc.action.model;

import java.util.Date;

public class MessageModel {
	public int id;
	public int type;
	public int creatorId;
	public String name;
	public String from;
	public String source;
	public String content;
	public Date createTime;
	public String keyword;
	
	public int _state;
	public Date lastUpdateTime;
}
