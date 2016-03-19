package org.xzc.msg.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.msg.dao.impl.GroupDao;
import org.xzc.msg.dao.impl.UserDao;
import org.xzc.msg.domain.Group;
import org.xzc.msg.domain.GroupForByid;
import org.xzc.msg.domain.GroupForList;
import org.xzc.msg.domain.GroupFullInfo;
import org.xzc.msg.domain.Page;
import org.xzc.msg.domain.User;
import org.xzc.msg.exception.GroupNotExistException;
import org.xzc.msg.exception.InvalidOperationException;
import org.xzc.msg.exception.RepeatedUserGroupException;
import org.xzc.msg.exception.RepeatedUserInGroupException;
import org.xzc.msg.exception.UserNotExistException;
import org.xzc.msg.utils.ServiceUtils;

@Service
public class GroupService {

	@Resource
	private GroupDao groupDao;

	@Resource
	private UserDao userDao;

	public void add(Group g) throws UserNotExistException, RepeatedUserGroupException {
		int creatorId = g.getCreatorId();
		User u = userDao.get( creatorId );
		if (u == null)
			throw new UserNotExistException();

		Group g0 = get( u.getId(), g.getName() );
		if (g0 != null) {
			throw new RepeatedUserGroupException();
		}
		g.createTime = new Date();
		groupDao.add( g );
		userDao.addUserOwnGroup( u.id, g.id );
	}

	public Group get(int groupId) {
		return groupDao.get( groupId );
	}

	public GroupForByid getForByid(int groupId) {
		GroupForByid g = groupDao.getForByid( groupId );
		return g;
	}

	public Page<GroupForList> list(int creatorId, int offset, int size,String by,int order, String keyword) {
		return groupDao.list( creatorId, offset, size,by,order,keyword );
	}

	/**
	 * 根据userId和groupName进行查询
	 * @return
	 */
	public Group get(int userId, String groupName) {
		return groupDao.get( userId, groupName );
	}

	/**
	 * 即使指定了creatorId也不会修改
	 * @param g
	 * @throws UserNotExistException
	 */
	public void update(Group g) throws UserNotExistException {
		groupDao.update( g );
	}

	public void addUserToGroup(int userId, int groupId) throws UserNotExistException, GroupNotExistException,
			RepeatedUserInGroupException, InvalidOperationException {
		User user = serviceUtils.ensureUserExists( userId );
		Group g = serviceUtils.ensureGroupExists( groupId );

		if (user.getId() == g.getCreatorId())
			throw new InvalidOperationException( "群主已经在该群里了." );

		//用户重复在一个组里
		if (groupDao.isUserInGroup( userId, groupId ))
			throw new RepeatedUserInGroupException();
		if (userDao.isUserJoinGroup( userId, groupId ))
			throw new RepeatedUserInGroupException();

		groupDao.addUserToGroup( userId, groupId );
		userDao.addUserJoinGroup( userId, groupId );

	}

	public GroupFullInfo getFullInfo(int groupId) {
		return groupDao.getFullInfo( groupId );
	}

	@Resource
	private ServiceUtils serviceUtils;

	public void removeUserFromGroup(int userId, int groupId) throws UserNotExistException, GroupNotExistException {
		serviceUtils.ensureUserExists( userId );
		serviceUtils.ensureGroupExists( groupId );
		groupDao.removeUserFromGroup( userId, groupId );
		userDao.removeGroupFromUser( userId, groupId );
	}

	/**
	 * 查看某个用户是否加入了某个组
	 * @param userId
	 * @param groupId
	 * @return
	 * @throws UserNotExistException 
	 * @throws GroupNotExistException 
	 */
	public boolean isUserJoinGroup(int userId, int groupId) throws UserNotExistException, GroupNotExistException {
		//由于数据由冗余 因此从哪里拿都可以
		serviceUtils.ensureUserExists( userId );
		serviceUtils.ensureGroupExists( groupId );
		return userDao.isUserJoinGroup( userId, groupId );
	}

}
