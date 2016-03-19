package org.xzc.msg.utils;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.msg.dao.impl.GroupDao;
import org.xzc.msg.dao.impl.MessageDao;
import org.xzc.msg.dao.impl.UserDao;
import org.xzc.msg.domain.Group;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.User;
import org.xzc.msg.exception.GroupNotExistException;
import org.xzc.msg.exception.MessageNotExistException;
import org.xzc.msg.exception.UserNotExistException;

@Service
public class ServiceUtils {
	@Resource
	private UserDao userDao;
	@Resource
	private GroupDao GroupDao;

	public User ensureUserExists(int userId) throws UserNotExistException {
		User u = userDao.get( userId );
		if (u == null)
			throw new UserNotExistException();
		return u;
	}
	
	public Group ensureGroupExists(int groupId) throws GroupNotExistException{
		Group g = GroupDao.get( groupId );
		if (g == null)
			throw new GroupNotExistException();
		return g;
	}

	@Resource
	private MessageDao messageDao;
	
	public Message ensureMessageExists(int msgId) throws MessageNotExistException {
		Message m = messageDao.get( msgId );
		if(m==null)
			throw new MessageNotExistException( );
		return m;
	}
}
