package org.xzc.msg.action;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xzc.action.model.UserMessageModel;
import org.xzc.msg.domain.MessageForList;
import org.xzc.msg.domain.Page;
import org.xzc.msg.domain.User;
import org.xzc.msg.exception.KnownException;
import org.xzc.msg.service.UserMessageService;

import com.opensymphony.xwork2.ModelDriven;

public class UserMessageAction extends ActionBase implements ModelDriven<UserMessageModel> {
	private static final Log LOG = LogFactory.getLog( UserMessageAction.class );

	private UserMessageModel model = new UserMessageModel();

	@Resource
	private UserMessageService userMessageService;

	public UserMessageModel getModel() {
		return model;
	}

	/**
	 * 列出跟当前登录的用户相关的um,
	 * offset和size用于分页
	 * @return
	 */
	public void list() throws KnownException {
		if (!checkLogin())
			return;
		Page<MessageForList> p = userMessageService.list( getCurrentUser().getId(),model.offset,model.size,model.by,model.order );
		success( p );
	}

	/**
	 * 以当前用户的名义发布一个消息到组里 这样组里的人都可以收到这个消息
	 * 需要接收msgId 和 groupId
	 */
	public void publishMessageToGroup() throws KnownException {
		if (!checkLogin())
			return;
		User user = getCurrentUser();
		userMessageService.publishMessageToGroup( user.getId(), model.msgId, model.groupId );
		success();
	}
}