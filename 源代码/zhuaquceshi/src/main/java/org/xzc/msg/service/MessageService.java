package org.xzc.msg.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.xzc.msg.dao.impl.MessageDao;
import org.xzc.msg.dao.impl.UserDao;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.domain.Page;
import org.xzc.msg.exception.InvalidMessageException;
import org.xzc.msg.exception.UserNotExistException;

@Service
public class MessageService {
	public static final String _ID = "_id";
	public static final String COUNT = "count";
	public static final String MSG_ID = "msg_id";
	public static final String NAME = "name";
	public static final String OFFSET = "offset";
	public static final String SIZE = "size";
	public static final String TOTAL = "total";
	public static final String TYPE = "type";
	private static final Log LOG = LogFactory.getLog( MessageService.class );

	@Resource
	private MessageDao messageDao;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserDao userDao;

	public void publishMessage(Message msg) throws InvalidMessageException, UserNotExistException {
		ensureUserExists( msg.creatorId );
		messageDao.add( msg );
		userDao.addUserPublishMessage( msg.creatorId, msg.id );
	}

	public Message delete(int msgId) {
		return messageDao.delete( msgId );
	}

	public Message get(int msgId) {
		return messageDao.get( msgId );
	}

	@PostConstruct
	public void init() {
	}

	/**
	 * 这里需要返回 total msgs offset size 
	 * code和msg由action负责添加
	 * @param offset
	 * @param size
	 * @return
	*/
	public Page<MessageForList> list(int userId, int type, int offset, int size, String by, int order,String keyword) {
		if (offset < 0)
			offset = 0;
		//纠正by
		if (by != null && !validBys.contains( by )) {
			by = "id";
		}
		//纠正order
		if (order != -1 && order != 1) {
			order = -1;
		}
		return messageDao.list( userId, type, offset, size, by, order, keyword );
	}
	
	private Set<String> validBys = new HashSet<String>( Arrays.asList( "id", "createTime", "type","startTime","endTime" ) );

	private void ensureUserExists(int userId) throws UserNotExistException {
		if (userService.get( userId ) == null)
			throw new UserNotExistException();
	}

}