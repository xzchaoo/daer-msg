package org.xzc.msg.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Component;
import org.xzc.msg.convert.GroupUtils;
import org.xzc.msg.domain.Group;
import org.xzc.msg.domain.GroupForByid;
import org.xzc.msg.domain.GroupForList;
import org.xzc.msg.domain.GroupFullInfo;
import org.xzc.msg.domain.IdAndName;
import org.xzc.msg.domain.Page;
import org.xzc.msg.domain.User;
import org.xzc.msg.mongodb.IDGenerator;
import org.xzc.msg.service.MongoDBService;
import org.xzc.msg.utils.Utils;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

@Component
public class GroupDao {
	public static final String _ID = "_id";

	public static final String CREATOR_ID = "creatorId";

	public static final String MEMBER_COUNT = "memberCount";

	public static final String MEMBERS = "members";

	public static final String MESSAGE_COUNT = "messageCount";
	public static final String MESSAGES = "messages";
	private static final Document GET_PROJECTION = new Document( MEMBERS, 0 );

	private static final Document PROJECT_FOR_LIST = new Document( MESSAGES, 0 ).append( MEMBERS, 0 );

	private static final Document PROJECTION_FOR_ID_AND_NAME = new Document( "_id", 1 ).append( "name", 1 );

	/**
	 * 禁止更新的属性
	 */
	private static final String[] UNUPDATABLE_PROPERTIES = { "creatorId", MEMBER_COUNT, MESSAGE_COUNT, "creatorTime" };

	/**
	 *移除禁止更新的属性
	 */
	private static void removeInvalidProperties(Document d) {
		if (d == null)
			return;
		for (String s : UNUPDATABLE_PROPERTIES) {
			d.remove( s );
		}
	}

	private static GroupForList toGroupForList(Document d) {
		if (d == null)
			return null;
		GroupForList g = new GroupForList();
		d.append( "id", d.remove( "_id" ) );
		Utils.populate( g, d );
		return g;
	}

	private MongoCollection<Document> groups;

	private IDGenerator idGenerator;

	@Resource
	private MessageDao messageDao;

	@Resource
	private MongoDBService mongoDBService;

	@Resource
	private UserDao userDao;

	/**
	 * 添加一个组
	 */
	public void add(Group g) {
		Document d = GroupUtils.toDocument( g );
		//生成id 和 空的成员
		int id = idGenerator.getNextId();
		d.append( "_id", id );

		//消息
		d.append( MEMBERS, Collections.EMPTY_SET );
		d.append( MEMBER_COUNT, 0 );

		//成员
		d.append( MESSAGES, Collections.EMPTY_SET );
		d.append( MESSAGE_COUNT, 0 );

		groups.insertOne( d );
		g.setId( id );
	}

	public void addMessageToGroup(int msgId, int groupId) {
		//这里维护了memberCount 字段
		UpdateResult ur = groups.updateOne( Filters.eq( "_id", groupId ), new Document( "$push", new Document(
				MESSAGES, msgId ) ).append( "$inc", new Document( MESSAGE_COUNT, 1 ) ) );
	}

	/**
	 * 将用户加入到组里
	 */
	public void addUserToGroup(int userId, int groupId) {
		//这里维护了memberCount 字段
		UpdateResult ur = groups.updateOne( Filters.eq( "_id", groupId ), new Document( "$push", new Document( MEMBERS,
				userId ) ).append( "$inc", new Document( "memberCount", 1 ) ) );
	}

	public Group get(int groupId) {
		Document d = groups.find( Filters.eq( "_id", groupId ) ).projection( GET_PROJECTION ).first();
		return GroupUtils.toGroup( d );
	}

	public Group get(int creatorId, String groupName) {
		Document d = groups.find( Filters.and( Filters.eq( CREATOR_ID, creatorId ), Filters.eq( "name", groupName ) ) )
				.projection( GET_PROJECTION ).first();
		return GroupUtils.toGroup( d );
	}

	public GroupForByid getForByid(int groupId) {
		Document d = groups.find( Filters.eq( _ID, groupId ) ).first();
		if (d == null)
			return null;
		GroupForByid g = new GroupForByid();
		Utils.fromDocument( d, g );

		//填充creator
		g.creator = userDao.getIdAndName( d.getInteger( "creatorId" ) );

		//填充members
		List<Integer> memberIds = (List<Integer>) d.get( MEMBERS );
		g.members = userDao.getUserNamesByIds( memberIds );

		//填充messages
		List<Integer> messageIds = (List<Integer>) d.get( MESSAGES );
		g.messages = messageDao.getMessage3ByIds( messageIds );

		return g;
	}

	public GroupFullInfo getFullInfo(int groupId) {
		Document d = groups.find( Filters.eq( "_id", groupId ) ).first();
		GroupFullInfo g = GroupUtils.toGroupFullInfo( d );

		User creator = userDao.get( g.getCreatorId() );
		g.setCreator( creator );

		return g;
	}

	public List<Integer> getMemberIds(int groupId) {
		Document d = groups.find( Filters.eq( "_id", groupId ) ).projection( new Document( MEMBERS, 1 ) ).first();
		List<Integer> ret = (List<Integer>) d.get( MEMBERS );
		if (ret != null) {
			Collections.sort( ret );
		} else {
			System.out.println( "ret==null!" );
			ret = Collections.EMPTY_LIST;
		}
		return ret;
	}

	public List<IdAndName> getNamesByIds(List<Integer> ids) {
		if (ids == null)
			return Collections.EMPTY_LIST;
		MongoCursor<Document> iter = groups.find( Filters.in( "_id", ids ) ).projection( PROJECTION_FOR_ID_AND_NAME )
				.iterator();
		List<IdAndName> list = new ArrayList<IdAndName>();
		while (iter.hasNext()) {
			Document d = iter.next();
			IdAndName ian = new IdAndName();
			Utils.fromDocument( d, ian );
			list.add( ian );
		}
		return list;
	}

	@PostConstruct
	public void init() {
		groups = mongoDBService.getGroups();
		idGenerator = mongoDBService.getIDGenerator( "group" );
	}

	public boolean isMessageInGroup(int msgId, int groupId) {
		return groups.count( Filters.and( Filters.eq( "_id", groupId ), Filters.eq( MESSAGES, msgId ) ) ) == 1;
	}

	/**
	 * 判断用户是否在组里
	 */
	public boolean isUserInGroup(int userId, int groupId) {
		return groups.count( Filters.and( Filters.eq( "_id", groupId ), Filters.eq( MEMBERS, userId ) ) ) == 1L;
	}

	/**
	 * 列出组信息 TODO 这里默认按照 id降序
	 */
	public Page<GroupForList> list(int creatorId, int offset, int size, String by, int order, String keyword) {
		Page<GroupForList> p = new Page<GroupForList>();

		Document filter = new Document();
		if (creatorId != 0) {
			filter.append( CREATOR_ID, creatorId );
		}
		if (keyword != null) {
			List<Document> orParamList = new ArrayList<Document>();
			if (creatorId == 0) {
				User user = userDao.get( keyword );
				if (user != null) {
					orParamList.add( new Document( CREATOR_ID, user.getId() ) );
				}
			}
			orParamList.add( new Document( "name", new Document( "$regex", Pattern.quote( keyword ) ) ) );
			orParamList.add( new Document( "desc", new Document( "$regex", Pattern.quote( keyword ) ) ) );
			filter.append( "$or", orParamList );
		}

		int total = (int) groups.count( filter );
		List<GroupForList> list = new ArrayList<GroupForList>( total );

		if (by == null) {
			by = "_id";
		}
		if (order != -1 && order != 1)
			order = -1;

		MongoCursor<Document> iterator = groups.find( filter ).projection( PROJECT_FOR_LIST )
				.sort( new Document( by, order ) ).iterator();

		while (iterator.hasNext()) {
			Document d = iterator.next();
			GroupForList g = toGroupForList( d );
			g.creator = userDao.getIdAndName( d.getInteger( "creatorId" ) );
			list.add( g );
		}
		p.setList( list );
		p.setOffset( offset );
		p.setSize( list.size() );
		p.setTotal( total );
		return p;
	}

	public Page<IdAndName> listGroupNames(int creatorId, int offset, int size, String by, int order) {
		Document filter = new Document();
		if (creatorId != 0) {
			filter.append( CREATOR_ID, creatorId );
		}
		int total = (int) groups.count( filter );
		FindIterable<Document> fiter = groups.find( filter ).skip( offset ).limit( size )
				.projection( PROJECTION_FOR_ID_AND_NAME );

		if (by != null) {
			fiter.sort( new Document( by, order ) );
		}

		MongoCursor<Document> iter = fiter.iterator();
		List<IdAndName> list = new ArrayList<IdAndName>();
		while (iter.hasNext()) {
			Document d = iter.next();
			IdAndName ian = new IdAndName();
			Utils.fromDocument( d, ian );
			list.add( ian );
		}

		Page<IdAndName> p = new Page<IdAndName>();
		p.list = list;
		p.offset = offset;
		p.size = list.size();
		p.total = total;
		return p;
	}

	public void removeUserFromGroup(int userId, int groupId) {
		UpdateResult ur = groups.updateOne( Filters.eq( "_id", groupId ), new Document( "$pull", new Document( MEMBERS,
				userId ) ).append( "$inc", new Document( MEMBER_COUNT, -1 ) ) );
	}

	public void update(Group g) {
		Document d = GroupUtils.toDocument( g );
		removeInvalidProperties( d );
		groups.updateOne( Filters.eq( "_id", g.getId() ), new Document( "$set", d ) );
	}
}
