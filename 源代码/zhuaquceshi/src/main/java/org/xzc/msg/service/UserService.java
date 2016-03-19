package org.xzc.msg.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.dao.impl.GroupDao;
import org.xzc.msg.dao.impl.MessageDao;
import org.xzc.msg.dao.impl.UserDao;
import org.xzc.msg.domain.IdAndName;
import org.xzc.msg.domain.Message3;
import org.xzc.msg.domain.Page;
import org.xzc.msg.domain.User;
import org.xzc.msg.domain.UserForDetail;
import org.xzc.msg.domain.UserForRegisterAndLogin;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.exception.WrongUserFormatException;
import org.xzc.msg.exception.RepeatedUserException;
import org.xzc.msg.utils.Assert;
import org.xzc.msg.utils.Utils;

@Service
public class UserService {
	public static final String NAME = "name";

	private static final Pattern NAME_PATTERN = Pattern.compile( "^[a-zA-Z0-9_]{3,16}$" );

	private static final Pattern PASSWORD_PATTERN = Pattern.compile( "^[a-zA-Z0-9_]{6,16}$" );

	/**
	 * 检查text是否满足正则表达式p
	 * @param text
	 * @param p
	 * @return
	 */
	private static boolean check(String text, Pattern p) {
		if (text == null)
			return false;
		Matcher matcher = p.matcher( text );
		return matcher.find();
	}

	private static void checkName(String name) throws WrongUserFormatException {
		if (!check( name, NAME_PATTERN ))
			throw new WrongUserFormatException();
	}

	private static void checkPassword(String password) throws WrongUserFormatException {
		if (!check( password, PASSWORD_PATTERN ))
			throw new WrongUserFormatException();
	}

	@Resource
	private MongoDBService mongoDBService;

	@Resource
	private UserDao userDao;

	public void add(UserForRegisterAndLogin user) throws WrongUserFormatException, RepeatedUserException {
		if (user == null)
			throw new IllegalArgumentException();

		checkName( user.getName() );
		checkPassword( user.getPassword() );

		if (userDao.get( user.getName(), user.getPassword() ) != null)
			throw new RepeatedUserException( "name=" + user.getName() + "的用户已经存在了." );

		userDao.add( user );
	}

	public User get(int userId) {
		return userDao.get( userId );
	}

	public User get(String name) {
		return userDao.get( name );
	}

	public User get(String name, String password) throws WrongUserFormatException {
		checkName( name );
		checkPassword( password );
		return userDao.get( name, password );
	}

	/**
	 * 获得id对应的用户的简单信息
	 * @param id
	 * @return
	 */
	public UserForSimpleInfo getSimpleInfo(int userId) {
		return userDao.getSimpleInfo( userId );
	}

	public UserForSimpleInfo getSimpleInfo(String name) throws WrongUserFormatException {
		checkName( name );
		return userDao.getSimpleInfo( name );
	}

	/**
	 * 测试用
	 * 列出所有用户
	 * @return
	 */
	public List<User> listUsers() {
		return userDao.list();
	}

	/**
	 * 修改密码
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @throws WrongUserFormatException 
	 */
	public User changePassword(int userId, String oldPassword, String newPassword) throws WrongUserFormatException {
		//检查格式
		checkPassword( oldPassword );
		checkPassword( newPassword );
		User u = userDao.get( userId, oldPassword );
		if (u != null) {
			userDao.setPassword( userId, newPassword );
			u.setPassword( newPassword );
		}
		return u;
	}

	public void update(User user) {
		Assert.notNull( user, "user不能为null." );
		//ensure id exists
		userDao.update( user );
	}

	@Resource
	private MessageDao messageDao;

	@Resource
	private GroupDao groupDao;

	private UserForDetail _getDetail(User user) {
		int userId = user.getId();
		UserForDetail u = new UserForDetail();
		Utils.copy( user, u );

		List<Integer> publishMessageIds = userDao.getUserPublishMessageIds( userId );
		List<Message3> publishMessages = messageDao.getMessage3ByIds( publishMessageIds );

		//获得用户拥有的组的id
		List<Integer> ownGroupIds = userDao.getUserOwnGroupIds( userId );
		List<IdAndName> ownGroups = groupDao.getNamesByIds( ownGroupIds );

		//获得用户参加的组的id
		List<Integer> joinGroupIds = userDao.getUserJoinGroupIds( userId );
		List<IdAndName> joinGroups = groupDao.getNamesByIds( joinGroupIds );

		u.publishMessages = publishMessages;
		u.ownGroups = ownGroups;
		u.joinGroups = joinGroups;
		return u;
	}

	public UserForDetail getDetail(int userId) {
		User user = userDao.get( userId );
		if (user == null)
			return null;
		return _getDetail( user );
	}

	public UserForDetail getDetail(String name) {
		User user = userDao.get( name );
		if (user == null)
			return null;
		return _getDetail( user );
	}
}
