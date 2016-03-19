package org.xzc.msg.dao.impl;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.stereotype.Component;
import org.xzc.msg.convert.UserUtils;
import org.xzc.msg.domain.IdAndName;
import org.xzc.msg.domain.User;
import org.xzc.msg.domain.UserForRegisterAndLogin;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.mongodb.IDGenerator;
import org.xzc.msg.service.MongoDBService;
import org.xzc.msg.utils.Utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

@Component
public class UserDao {

	public static final String _ID = "_id";

	public static final String JOIN_GROUP_COUNT = "joinGroupCount";

	public static final String JOIN_GROUPS = "joinGroups";
	public static final String OWN_GROUP_COUNT = "ownGroupCount";

	public static final String OWN_GROUPS = "ownGroups";

	public static final String PUBLISH_MESSAGE_COUNT = "publishMessageCount";

	public static final String PUBLISH_MESSAGES = "publishMessages";

	public static final String RECEIVE_MESSAGES = "receiveMessages";

	public static final String RECEIVE_MESSAGE_COUNT = "receiveMessageCount";

	private static final Document USER_PROJECTION = new Document( "password", 0 );

	private IDGenerator idGenerator;

	@Resource
	private MongoDBService mongoDBService;

	private MongoCollection<Document> users;

	public void add(UserForRegisterAndLogin user) {
		Document d = Utils.toDocument( user );
		int id = idGenerator.getNextId();
		user.setId( id );
		d.append( "_id", id );
		d.append( "phone", "" );
		d.append( "qq", "" );
		d.append( "weixin", "" );
		d.append( "desc", "" );
		d.append( OWN_GROUPS, Collections.EMPTY_LIST );
		d.append( OWN_GROUP_COUNT, 0 );
		d.append( JOIN_GROUPS, Collections.EMPTY_LIST );
		d.append( JOIN_GROUP_COUNT, 0 );
		d.append( PUBLISH_MESSAGES, Collections.EMPTY_LIST );
		d.append( PUBLISH_MESSAGE_COUNT, 0 );
		d.append( RECEIVE_MESSAGES, Collections.EMPTY_LIST );
		d.append( RECEIVE_MESSAGE_COUNT, 0 );

		users.insertOne( d );
	}

	/**
	 * 增加说用户拥有这个组
	 * @param id
	 * @param id2
	 */
	public void addUserOwnGroup(int userId, int groupId) {
		users.updateOne( eq( _ID, userId ), new Document( "$push", new Document( OWN_GROUPS, groupId ) ).append(
				"$inc", new Document( OWN_GROUP_COUNT, 1 ) ) );
	}

	/**
	 * 增加说用户发布了这条消息
	 * @param id
	 */
	public void addUserPublishMessage(int userId, int msgId) {
		users.updateOne( eq( "_id", userId ), new Document( "$push", new Document( PUBLISH_MESSAGES, msgId ) ).append(
				"$inc", new Document( PUBLISH_MESSAGE_COUNT, 1 ) ) );
	}

	public void addUserJoinGroup(int userId, int groupId) {
		users.updateOne( eq( _ID, userId ), new Document( "$push", new Document( JOIN_GROUPS, groupId ) ).append(
				"$inc", new Document( JOIN_GROUP_COUNT, 1 ) ) );
	}

	public User get(int userId) {
		Document d = users.find( eq( "_id", userId ) ).projection( USER_PROJECTION ).first();
		return UserUtils.toUser( d );
	}

	public User get(int userId, String password) {
		Document d = users.find( and( eq( "_id", userId ), eq( "password", password ) ) ).projection( USER_PROJECTION )
				.first();
		return UserUtils.toUser( d );
	}

	public User get(String name) {
		Document d = users.find( eq( "name", name ) ).projection( USER_PROJECTION ).first();
		return UserUtils.toUser( d );
	}

	public User get(String name, String password) {
		Document d = users.find( and( eq( "name", name ), eq( "password", password ) ) ).projection( USER_PROJECTION )
				.first();
		return UserUtils.toUser( d );
	}

	public IdAndName getIdAndName(int userId) {
		IdAndName ian = new IdAndName();
		ian.id = userId;
		ian.name = getName( userId );
		return ian;
	}

	public String getName(int userId) {
		return users.find( eq( "_id", userId ) ).projection( new Document( "name", 1 ) ).first().getString( "name" );
	}

	public UserForSimpleInfo getSimpleInfo(int userId) {
		Document d = users.find( eq( "_id", userId ) ).projection( USER_PROJECTION ).first();
		return UserUtils.toSimpleUser( d );
	}

	public UserForSimpleInfo getSimpleInfo(String name) {
		Document d = users.find( eq( "name", name ) ).projection( USER_PROJECTION ).first();
		return UserUtils.toSimpleUser( d );
	}

	/**
	 * 获得用户参加的组的id
	 * @param userId
	 * @return
	 */
	public List<Integer> getUserJoinGroupIds(int userId) {
		Document d = users.find( Filters.eq( _ID, userId ) ).projection( new Document( "joinGroups", 1 ) ).first();
		return (List<Integer>) d.get( "joinGroups" );
	}

	public List<IdAndName> getUserNamesByIds(List<Integer> ids) {
		if (ids == null)
			return Collections.EMPTY_LIST;
		List<IdAndName> ret = new ArrayList<IdAndName>();
		MongoCursor<Document> it = users.find( Filters.in( "_id", ids ) )
				.projection( new Document( "_id", 1 ).append( "name", 1 ) ).iterator();
		while (it.hasNext()) {
			Document d = it.next();
			IdAndName ian = Utils.toIdAndName( d );
			ret.add( ian );
		}
		return ret;
	}

	/**
	 * 获得用户拥有的组的id
	 * @param userId
	 * @return
	 */
	public List<Integer> getUserOwnGroupIds(int userId) {
		Document d = users.find( Filters.eq( _ID, userId ) ).projection( new Document( OWN_GROUPS, 1 ) ).first();
		return (List<Integer>) d.get( OWN_GROUPS );
	}

	/**
	 * 获得用户发布的消息的id们
	 * @param userId
	 * @return
	 */
	public List<Integer> getUserPublishMessageIds(int userId) {
		Document d = users.find( Filters.eq( _ID, userId ) ).first();
		return (List<Integer>) d.get( PUBLISH_MESSAGES );
	}

	@PostConstruct
	public void init() {
		this.users = mongoDBService.getUsers();
		idGenerator = mongoDBService.getIDGenerator( "user_id" );
	}

	public boolean isUserJoinGroup(int userId, int groupId) {
		return users.count( new Document( "_id", userId ).append( JOIN_GROUPS, groupId ) ) == 1;
	}

	public List<User> list() {
		List<Document> documents = new ArrayList<Document>();
		users.find().projection( USER_PROJECTION ).into( documents );
		return UserUtils.toUserList( documents );
	}

	public void setPassword(int userId, String newPassword) {
		users.updateOne( eq( "_id", userId ), new Document( "$set", new Document( "password", newPassword ) ) );
	}

	public void update(User user) {
		Document d = UserUtils.toDocument( user );
		d.remove( "password" );
		d.remove( "name" );
		users.updateOne( eq( "_id", user.getId() ), new Document( "$set", d ) );
	}

	public void removeGroupFromUser(int userId, int groupId) {
		users.updateOne( eq( _ID, userId ), new Document( "$pull", new Document( JOIN_GROUPS, groupId ) ).append(
				"$inc", new Document( JOIN_GROUP_COUNT, -1 ) ) );
	}

	/**
	 * 用户收到了该消息
	 * @param userId
	 * @param msgId
	 */
	public void addUserReceiveMessage(int userId, int msgId) {
		users.updateOne( eq( _ID, userId ), new Document( "$push", new Document( RECEIVE_MESSAGES, msgId ) ).append(
				"$inc", new Document( RECEIVE_MESSAGE_COUNT, 1 ) ) );
	}

	/**
	 * 用户是否已经有该消息了
	 * @param userId
	 * @param msgId
	 * @return
	 */
	public boolean isUserReceiveMessage(int userId, int msgId) {
		return users.count( new Document( _ID, userId ).append( RECEIVE_MESSAGES, msgId ) ) == 1;
	}

	/**
	 * 列出一个用户收到的消息的id
	 * @param userId
	 * @return
	 */
	public List<Integer> listUserReceivedMessageIds(int userId) {
		List<Integer> ret = (List<Integer>) users.find( eq( _ID, userId ) ).first().get( RECEIVE_MESSAGES );
		return ret == null ? Collections.EMPTY_LIST : ret;
	}

	//分段版本
	public List<Integer> listUserReceivedMessageIds(int userId, int offset, int size) {
		Document d = users.find( eq( _ID, userId ) ).projection( new Document( RECEIVE_MESSAGES, 1 ) ).first();
		if (d == null)
			return Collections.EMPTY_LIST;
		List<Integer> msgIds = (List<Integer>) d.get( RECEIVE_MESSAGES );
		if (msgIds == null)
			return Collections.EMPTY_LIST;
		int endIndex = offset + size;
		if (endIndex > msgIds.size())
			endIndex = msgIds.size();
		return msgIds.subList( offset, endIndex );
	}

	public int getUserReceivedMessagesCount(int userId) {
		return users.find( eq( _ID, userId ) ).projection( new Document( RECEIVE_MESSAGE_COUNT, 1 ) ).first()
				.getInteger( RECEIVE_MESSAGE_COUNT, 0 );
	}
}
