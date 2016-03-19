package org.xzc.msg.event;

/**
 * 查看某个用户发布的消息
 * @author xzchaoo
 *
 */
public class ListMessagesByCreatorNameEvent {
	
	public String creatorName;

	public ListMessagesByCreatorNameEvent(String creatorName) {
		super();
		this.creatorName = creatorName;
	}
}
