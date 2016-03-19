package org.xzc.msg.dao;

import org.xzc.msg.domain.Message;

public interface IMessageDaoForTest {
	public void deleteAll();
	public void ensureExist(Message m);
}
