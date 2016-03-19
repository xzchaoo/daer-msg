package org.xzc.msg.dao.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Component;
import org.xzc.msg.dao.IMessageDaoForTest;
import org.xzc.msg.domain.Message;
import org.xzc.msg.service.MongoDBService;

import com.mongodb.client.MongoCollection;

@Component
public class MessageDaoForTest implements IMessageDaoForTest {

	@Resource
	private MongoDBService mongoDBService;

	private MongoCollection<Document> msgs;

	@PostConstruct
	public void init() {
		msgs = mongoDBService.getMsgs();
	}

	@Override
	public void deleteAll() {
		msgs.drop();
	}

	@Override
	public void ensureExist(Message m) {
	}

}
