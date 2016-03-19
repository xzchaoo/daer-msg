package org.xzc.msg.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xzc.action.model.MessageActionModel;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.domain.Group;
import org.xzc.msg.domain.Message;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.domain.Page;
import org.xzc.msg.domain.User;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.exception.KnownException;
import org.xzc.msg.pull.IMessagePullCreator;
import org.xzc.msg.pull.MessagePullService;
import org.xzc.msg.service.GroupService;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.service.UserMessageService;
import org.xzc.msg.service.UserService;

import com.opensymphony.xwork2.ModelDriven;

public class MessageAction extends ActionBase implements ModelDriven<MessageActionModel> {
	private static final Log LOG = LogFactory.getLog( MessageAction.class );
	@Resource
	private MessagePullService messagePullService;
	@Resource
	private MessageService messageService;
	private MessageActionModel model = new MessageActionModel();

	@Resource
	private GroupService groupService;
	@Resource
	private UserMessageService userMessageService;

	/**
	 * 根据model里的内容添加一条消息
	 * @return
	 */
	public void add() throws KnownException {
		if (!checkLogin())
			return;
		User user = getCurrentUser();
		//获得相应的creator
		IMessagePullCreator mpc = messagePullService.getMessagePullCreator( model.type );
		if (mpc == null) {//不支持的类型
			code( Code.UNSUPPORTED_TYPE );
			return;
		}
		if (model.groupId != 0) {
			Group g = groupService.get( model.groupId );
			if (g == null) {
				code( Code.NO_GROUP );
				return;
			} else if (user.id != g.creatorId) {
				code( Code.NO_AUTHORITY ).msg( "你不是群主." );
				return;
			}
		}
		Message msg = mpc.create( model );

		//TODO 假设消息是由当前用户创建的 可能要为其他人开一下后门
		//不然总是要先登陆
		msg.creatorId = user.id;
		messageService.publishMessage( msg );
		if(model.groupId!=0)
			userMessageService.publishMessageToGroup( user.id, msg.id, model.groupId );
		success( msg );
	}

	@Resource
	private UserService userService;

	/**
	 * 返回msg byid
	 * @return
	 */
	public void byid() {
		Message msg = messageService.get( model.id );
		if (msg == null) {
			code( Code.NO_MESSAGE );
		} else {
			UserForSimpleInfo creator = userService.getSimpleInfo( msg.creatorId );
			Map<String, Object> map = new HashMap<String, Object>();
			map.put( "msg", msg );
			map.put( "creator", creator );
			success( map );
		}
	}

	/**
	 * 删除 具有权限的人才可以用 现在测试阶段
	 * @return
	 */
	public void delete() {
		if (!checkLogin())
			return;
		Message msg = messageService.delete( model.id );
		if (msg != null) {
			success( msg );
		} else {
			code( Code.NO_MESSAGE );
		}
	}

	public MessageActionModel getModel() {
		return model;
	}

	/**
	 * 测试用 分页列出数据
	 * @return
	 */
	public void list() {
		if (model.creator != null && model.creator.length() > 0) {
			User user = userService.get( model.creator );
			if (user != null)
				model.creatorId = user.getId();
			else
				model.creatorId = -1;
		}
		Page<MessageForList> page = messageService.list( model.creatorId, model.type, model.offset, model.size,
				model.by, model.order, model.keyword );
		success( page );
	}

}
