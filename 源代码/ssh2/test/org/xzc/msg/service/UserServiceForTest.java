package org.xzc.msg.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xzc.msg.dao.impl.UserDao;
import org.xzc.msg.domain.User;

@Service
public class UserServiceForTest {

	@Resource
	private UserDao userDao;

	public void ensureExists(User user) {
		User u2 = userDao.get( user.getName() );
		if (u2 == null) {
			userDao.add( user );
		} else {
			user.setId( u2.getId() );
			userDao.update( user );
		}
	}

	public User get(String name) {
		return userDao._get( name );
	}

	public void setPassword(String name, String password) {
		User u = userDao.get( name );
		userDao.setPassword( u.getId(), password );
	}
}
