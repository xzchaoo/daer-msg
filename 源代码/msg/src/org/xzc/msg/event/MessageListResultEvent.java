package org.xzc.msg.event;

import java.util.List;

import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.params.ParamsForMessageList;
import org.xzc.msg.result.ResultBase;

public class MessageListResultEvent extends ResultBase {
	public List<MessageForList> messages;
	public int total;
	public ParamsForMessageList params;
}
