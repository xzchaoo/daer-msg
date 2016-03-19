package org.xzc.msg.service.callback;

import org.xzc.msg.domain.Message;
import org.xzc.msg.exception.InvalidMessageException;

public interface IServiceCallback {
	public Message beforeAddCallback(Message d) throws InvalidMessageException;

	public Message beforeGetCallback(Message d);

	public Message beforeUpdateCallback(Message msg) throws InvalidMessageException;
}
