package org.xzc.msg.dao.impl2;

import static com.mongodb.client.model.Filters.eq;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Component;
import org.xzc.msg.convert.UserUtils;
import org.xzc.msg.domain.User;
import org.xzc.msg.service.MongoDBService;

import com.mongodb.client.MongoCollection;

/**
 * 提供一些不常用功能的功能
 * @author xzchaoo
 *
 */
public class UserDao2 {
	@Resource
	private MongoDBService mongoDBService;
	private MongoCollection<Document> users;

	public User delete(int id) {
		Document d = users.findOneAndDelete( eq( "_id", id ) );
		return UserUtils.toUser( d );
	}

	public User delete(String name) {
		Document d = users.findOneAndDelete( eq( "name", name ) );
		return UserUtils.toUser( d );
	}

	@PostConstruct
	public void init() {
		users = mongoDBService.getUsers();
	}
}
