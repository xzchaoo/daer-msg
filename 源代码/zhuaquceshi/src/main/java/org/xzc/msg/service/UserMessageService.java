package org.xzc.msg.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.msg.dao.impl.GroupDao;
import org.xzc.msg.dao.impl.MessageDao;
import org.xzc.msg.dao.impl.UserDao;
import org.xzc.msg.domain.Group;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.domain.Page;
import org.xzc.msg.domain.User;
import org.xzc.msg.exception.AuthorityException;
import org.xzc.msg.exception.GroupNotExistException;
import org.xzc.msg.exception.InvalidOperationException;
import org.xzc.msg.exception.MessageNotExistException;
import org.xzc.msg.exception.RepeatedMessageInGroupException;
import org.xzc.msg.exception.UserNotExistException;
import org.xzc.msg.utils.ServiceUtils;

@Service
public class UserMessageService {

	/**
	 * 以userId的名义发布msgId到groupId里
	 * groupId下的每个成员的消息队列都会收到该消息
	 * @param id
	 * @param msgId
	 * @param groupId
	 * @throws UserNotExistException 
	 * @throws MessageNotExistException 
	 * @throws GroupNotExistException 
	 * @throws AuthorityException 
	 * @throws InvalidOperationException 
	 * @throws RepeatedMessageInGroupException 
	 */
	public void publishMessageToGroup(int userId, int msgId, int groupId) throws UserNotExistException,
			MessageNotExistException, GroupNotExistException, AuthorityException, InvalidOperationException,
			RepeatedMessageInGroupException {
		User user = serviceUtils.ensureUserExists( userId );
		Message message = serviceUtils.ensureMessageExists( msgId );
		Group group = serviceUtils.ensureGroupExists( groupId );
		if (userId != message.creatorId)
			throw new AuthorityException();
		if (userId != group.creatorId)
			throw new AuthorityException();
		if (messageDao.isGroupInMessage( groupId, msgId ))
			throw new RepeatedMessageInGroupException();
		if (groupDao.isMessageInGroup( msgId, groupId ))
			throw new RepeatedMessageInGroupException();

		messageDao.addGroupToMessage( groupId, msgId );
		groupDao.addMessageToGroup( msgId, groupId );
		//获得成员的id
		List<Integer> memberIds = groupDao.getMemberIds( groupId );
		for (int memberId : memberIds) {
			//对于每个成员 如果他还没有接受过该消息 就让他接受
			if (!userDao.isUserReceiveMessage( memberId, msgId ))
				userDao.addUserReceiveMessage( memberId, msgId );
		}
	}

	@Resource
	private UserDao userDao;
	@Resource
	private GroupDao groupDao;
	@Resource
	private MessageDao messageDao;

	@Resource
	private ServiceUtils serviceUtils;

	public Page<MessageForList> list(int userId, int offset, int size, String by, int order)
			throws UserNotExistException {
		serviceUtils.ensureUserExists( userId );
		//获得一个用户所接收到消息的id
		List<Integer> userReceivedMessageIds = userDao.listUserReceivedMessageIds( userId, offset, size );
		int total = userDao.getUserReceivedMessagesCount( userId );
		List<MessageForList> messageList = messageDao.listByIds( userReceivedMessageIds, by, order );
		Page<MessageForList> p = new Page<MessageForList>();
		p.list = messageList;
		p.offset = offset;
		p.size = messageList.size();
		p.total = total;
		return p;
	}
}
