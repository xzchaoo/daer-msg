package org.xzc.msg.action;

import javax.annotation.Resource;

import org.bson.Document;
import org.xzc.action.model.GroupActionModel;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.domain.Group;
import org.xzc.msg.domain.GroupForByid;
import org.xzc.msg.domain.GroupForList;
import org.xzc.msg.domain.GroupFullInfo;
import org.xzc.msg.domain.Page;
import org.xzc.msg.domain.User;
import org.xzc.msg.exception.GroupNotExistException;
import org.xzc.msg.exception.InvalidOperationException;
import org.xzc.msg.exception.KnownException;
import org.xzc.msg.exception.RepeatedUserGroupException;
import org.xzc.msg.exception.RepeatedUserInGroupException;
import org.xzc.msg.exception.UserNotExistException;
import org.xzc.msg.service.GroupService;
import org.xzc.msg.service.UserService;

import com.opensymphony.xwork2.ModelDriven;

public class GroupAction extends ActionBase implements ModelDriven<GroupActionModel> {
	private GroupActionModel model = new GroupActionModel();

	public GroupActionModel getModel() {
		return model;
	}

	@Resource
	private GroupService groupService;

	public void add() throws KnownException {
		if (!checkLogin())
			return;
		User user = getCurrentUser();
		Group g = new Group();
		g.setCreatorId( user.getId() );
		g.setName( model.getName() );
		g.setDesc( model.getDesc() );
		groupService.add( g );
		success( g );
	}

	@Resource
	private UserService userService;

	/**
	 * 通过id查询组
	 */
	public void byid() {
		GroupForByid g = groupService.getForByid( model.id );
		if (g != null) {
			success( g );
		} else {
			code( Code.NO_GROUP );
		}
	}

	/**
	 * 列出组
	 */
	public void list() {
		Page<GroupForList> p = groupService.list( model.creatorId, model.offset, model.size, model.by, model.order,
				model.keyword );
		success( p );
	}

	/**
	 * 将一个用户加入到组里
	 */
	public void addUserToGroup() throws KnownException {
		//需要用到model里的userId和groupId

		if (!checkLogin())
			return;
		//能进行此操作的只有userId本人
		User cu = getCurrentUser();
		if (model.userId != cu.getId()) {
			code( Code.NO_AUTHORITY );
			return;
		}
		groupService.addUserToGroup( model.userId, model.groupId );
		success();
	}

	/**
	 * 显示一个组的全部信息 包含它的成员
	 */
	public void fullInfo() {
		GroupFullInfo gfi = groupService.getFullInfo( model.groupId );
		if (gfi == null) {
			code( Code.NO_GROUP );
		} else {
			success( gfi );
		}
	}

	/**
	 * TODO
	 * 能执行这个操作的只有本人  (和群主??? 在考虑一下)
	 */
	public void removeUserFromGroup() throws KnownException {
		if (!checkLogin())
			return;
		User cu = getCurrentUser();
		if (cu.getId() != model.userId) {
			code( Code.NO_AUTHORITY );
			return;
		}
		groupService.removeUserFromGroup( model.userId, model.groupId );
		success();
	}

	/**
	 * 查看某个用户是否加入了某个组
	 */
	public void isUserJoinGroup() throws KnownException {
		boolean ret = groupService.isUserJoinGroup( model.userId, model.groupId );
		success( new Document( "isUserJoinGroup", ret ) );
	}
}
