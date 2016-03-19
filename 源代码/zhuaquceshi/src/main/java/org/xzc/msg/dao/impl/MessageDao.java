package org.xzc.msg.dao.impl;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Component;
import org.xzc.msg.convert.MessageUtils;
import org.xzc.msg.domain.IdAndName;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.Message3;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.domain.Page;
import org.xzc.msg.mongodb.IDGenerator;
import org.xzc.msg.service.MongoDBService;
import org.xzc.msg.utils.Utils;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

@Component
public class MessageDao {

	public static final String _ID = "_id";

	private static final Document PROJECT_FOR_LIST3 = new Document( "_id", 1 ).append( "name", 1 ).append( "type", 1 );

	private IDGenerator idGenerator;

	@Resource
	private MongoDBService mongoDBService;

	private MongoCollection<Document> msgs;

	@Resource
	private UserDao userDao;

	public void add(Message message) {
		int id = idGenerator.getNextId();
		Document d = Utils.toDocument( message );
		d.append( "_id", id );
		message.setId( id );
		msgs.insertOne( d );
	}

	public static final String GROUPS = "groups";
	public static final String GROUP_COUNT = "group_count";

	public void addGroupToMessage(int groupId, int msgId) {
		msgs.updateOne( eq( "_id", msgId ), new Document( "$push", new Document( GROUPS, groupId ) ).append( "$inc",
				new Document( GROUP_COUNT, 1 ) ) );
	}

	public Message delete(int msgId) {
		Document d = msgs.findOneAndDelete( eq( _ID, msgId ) );
		return MessageUtils.toMessage( d );
	}

	public Message get(int msgId) {
		Document d = msgs.find( eq( _ID, msgId ) ).first();
		return MessageUtils.toMessage( d );
	}

	/**
	 * 通过ids获得一堆简单的消息描述
	 * @param ids
	 * @return
	 */
	public List<Message3> getMessage3ByIds(List<Integer> ids) {
		if (ids == null)
			return Collections.EMPTY_LIST;
		MongoCursor<Document> iter = msgs.find( Filters.in( _ID, ids ) ).iterator();
		List<Message3> list = new ArrayList<Message3>();
		while (iter.hasNext()) {
			Document d = iter.next();
			Message3 m3 = new Message3();
			Utils.fromDocument( d, m3 );
			list.add( m3 );
		}
		return list;
	}

	public List<IdAndName> getMessageNamesByIds(List<Integer> messageIds) {
		List<IdAndName> ret = new ArrayList<IdAndName>();
		MongoCursor<Document> it = msgs.find( Filters.in( "_id", messageIds ) )
				.projection( new Document( "_id", 1 ).append( "name", 1 ) ).iterator();
		while (it.hasNext()) {
			Document d = it.next();
			IdAndName ian = Utils.toIdAndName( d );
			ret.add( ian );
		}
		return ret;
	}

	@PostConstruct
	public void init() {
		msgs = mongoDBService.getMsgs();
		idGenerator = mongoDBService.getIDGenerator( "msg_id" );
	}

	public boolean isGroupInMessage(int groupId, int msgId) {
		return msgs.count( Filters.and( eq( "_id", msgId ), eq( "groups", groupId ) ) ) == 1;
	}

	/**
	 * 枚举出type类型(如果为0表示全部类型)
	 * 分页offset size
	 * 排序 by(by=creaetTime 表示按照创建时间进行排序) order(order=-1表示降序 1表示升序)
	 */
	public Page<MessageForList> list(int creatorId, int type, int offset, int size, String by, int order, String keyword) {
		Page<MessageForList> page = new Page<MessageForList>();

		List<Document> documentList = new ArrayList<Document>();
		if (order != -1 && order != 1) {
			order = -1;
		}
		int total = 0;
		Document findFilter = new Document();
		if (creatorId != 0) {
			findFilter.append( "creatorId", creatorId );
		}
		if (type != 0) {
			findFilter.append( "type", new Document( "$eq", type ) );
		}
		if (keyword != null && !keyword.isEmpty()) {
			findFilter.append( "name", new Document( "$regex", Pattern.quote( keyword ) ) );
		}

		FindIterable<Document> find = msgs.find( findFilter );

		total = (int) msgs.count( findFilter );

		find.skip( offset ).limit( size );

		if ("id".equals(by))
			by = _ID;
		
		if (by == null) {
			find.sort( new Document( _ID, -1 ) );
		} else {

			find.sort( new Document( by, order ) );
		}
		List<MessageForList> msgList = _list( find.iterator() );
		page.setOffset( offset );
		page.setSize( msgList.size() );
		page.setTotal( total );
		page.setList( msgList );
		return page;
	}

	private List<MessageForList> _list(MongoCursor<Document> documents) {
		List<MessageForList> msgList = new ArrayList<MessageForList>();
		while (documents.hasNext()) {
			Document d = documents.next();
			MessageForList m = new MessageForList();
			Utils.fromDocument( d, m );
			m.creator = userDao.getIdAndName( d.getInteger( CREATOR_ID ) );
			msgList.add( m );
		}
		return msgList;
	}

	public List<MessageForList> listByIds(List<Integer> ids, String by, int order) {
		if (by == null) {
			by = _ID;
		}
		if (order != -1 && order != 1) {
			order = -1;
		}
		return _list( msgs.find( Filters.in( _ID, ids ) ).sort( new Document( by, order ) ).iterator() );
	}

	public static final String CREATOR_ID = "creatorId";

	public Page<Message3> list3(int creatorId, int offset, int size, String by, int order) {
		Document filter = new Document();
		if (creatorId != 0) {
			filter.append( CREATOR_ID, creatorId );
		}
		FindIterable<Document> fiter = msgs.find( filter ).skip( offset ).limit( size ).projection( PROJECT_FOR_LIST3 );
		if (by != null) {
			fiter.sort( new Document( by, order ) );
		}
		MongoCursor<Document> iter = fiter.iterator();
		List<Message3> list = new ArrayList<Message3>();
		while (iter.hasNext()) {
			Document d = iter.next();
			Message3 m3 = new Message3();
			Utils.fromDocument( d, m3 );
			list.add( m3 );
		}
		Page<Message3> p = new Page<Message3>();
		return p;
	}

	public void setMessageGroup(int msgId, int groupId) {
		msgs.updateOne( eq( "_id", msgId ), new Document( "$set", new Document( "groupId", groupId ) ) );
	}

	public void update(Message msg) {
		Document d = Utils.toDocument( msg );
		UpdateResult ur = msgs.updateOne( eq( _ID, msg.getId() ), new Document( "$set", d ) );
	}

}
