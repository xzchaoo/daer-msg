package org.xzc.msg.event;

import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.result.ResultBase;

public class MessageByIdResultEvent extends ResultBase{
	public int id;
	public Message message;
	public UserForSimpleInfo creator;
}
