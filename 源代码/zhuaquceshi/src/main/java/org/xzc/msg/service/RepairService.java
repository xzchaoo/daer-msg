package org.xzc.msg.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Service;
import org.xzc.msg.dao.impl.UserDao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

@Service
public class RepairService {
	@Resource
	private MongoDBService mongoDBService;

	private MongoCollection<Document> groups;
	private MongoCollection<Document> ums;
	private MongoCollection<Document> users;
	private MongoCollection<Document> msgs;

	@PostConstruct
	public void init() {
		msgs = mongoDBService.getMsgs();
		groups = mongoDBService.getGroups();
		ums = mongoDBService.getUserMessageCollections();
		users = mongoDBService.getUsers();
	}

	public void repair() {
		System.out.println( "service" );
	}

	/**
	 * 清除所有的 usermessages
	 */
	public void clearUMS() {
		msgs.updateMany( new Document(),
				new Document( "$set", new Document( "groupCount", 0 ).append( "groups", Collections.EMPTY_LIST ) ) );
		groups.updateMany( new Document(),
				new Document( "$set", new Document( "messageCount", 0 ).append( "messages", Collections.EMPTY_LIST ) ) );
		ums.drop();
	}

	public void repairSimpleMessageCreateTime() {
		msgs.updateMany( new Document( "type", 1 ), new Document( "$set", new Document( "createTime", new Date() ) ) );
	}

	public void resetAll() {
		mongoDBService.getDatabase().drop();
	}

	public void repairCreatorId() {
		msgs.updateMany( new Document(), new Document( "$set", new Document( "creatorId", "$userId" ) ).append(
				"$unset", new Document( "userId", 1 ) ) );
	}

	public void repairGroupCreateTime() {
		groups.updateMany( new Document(), new Document( "$set", new Document( "createTime", new Date() ) ) );
	}

	public void repairUserPublishMessageCount() {
		MongoCursor<Document> iter = users.find().iterator();
		while (iter.hasNext()) {
			Document d = iter.next();
			List<Integer> msgs = (List<Integer>) d.get( UserDao.PUBLISH_MESSAGES );
			if (msgs == null)
				msgs = Collections.EMPTY_LIST;
			users.updateOne( Filters.eq( "_id", d.getInteger( "_id" ) ), new Document( "$set", new Document(
					UserDao.PUBLISH_MESSAGE_COUNT, msgs.size() ) ) );
		}
	}
}
