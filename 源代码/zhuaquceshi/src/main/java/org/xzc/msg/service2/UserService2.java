package org.xzc.msg.service2;

import org.springframework.stereotype.Service;
import org.xzc.msg.domain.User;
import org.xzc.msg.exception.WrongUserFormatException;

public class UserService2 {
	/**
	 * 测试用
	 * 保证有 一个 (name,password) 的账号存在
	 * @param name
	 * @param password
	 * @return
	 * @throws WrongUserFormatException 
	 */
	public User _create(String name, String password) throws WrongUserFormatException {
		checkName( name );
		checkPassword( password );
		userDao.delete( name );
		User u = new User();
		u.setName( name );
		u.setPassword( password );
		userDao.publishMessage( u );
		return u;
	}
	
	public User delete(int userId) {
		return userDao.delete( userId );
	}

	public User delete(String name) throws WrongUserFormatException {
		checkName( name );
		return userDao.delete( name );
	}
	
}
