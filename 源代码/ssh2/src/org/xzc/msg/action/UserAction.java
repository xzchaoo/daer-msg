package org.xzc.msg.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xzc.action.model.UserActionModel;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.domain.User;
import org.xzc.msg.domain.UserForDetail;
import org.xzc.msg.domain.UserForRegisterAndLogin;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.exception.KnownException;
import org.xzc.msg.service.UserService;

import com.opensymphony.xwork2.ModelDriven;

@Scope("prototype")
@Controller
public class UserAction extends ActionBase implements ModelDriven<UserActionModel> {
	private static final Log LOG = LogFactory.getLog( UserAction.class );

	private UserActionModel model = new UserActionModel();

	@Resource
	private UserService userService;

	public void detail() {
		UserForDetail u;
		if (model.id != 0)
			u = userService.getDetail( model.id );
		else
			u = userService.getDetail( model.name );
		if (u == null) {
			code( Code.NO_USER );
		} else {
			success( u );
		}
	}

	public UserActionModel getModel() {
		return model;
	}

	/**
	 * 列出所有账号和密码 测试用
	 * @return
	 */
	public void listUsers() {
		List<User> userList = userService.listUsers();
		success( userList );
	}

	/**
	 * 登陆
	 * @return
	 */
	public void login() throws KnownException {
		User user = getCurrentUser();
		if (user != null) {
			code( Code.LOGINED ).result( user );
		} else {
			user = userService.get( model.name, model.password );
			if (user == null) {
				code( Code.WRONG_PASSWORD );
			} else {
				if (LOG.isInfoEnabled())
					LOG.info( user.getName() + "进行了登陆" );
				success( user );
				userSessionService.setCurrentUser( session, user );
			}
		}
	}

	/**
	 * 注销
	 * @return
	 */
	public void logout() {
		if (!checkLogin())
			return;
		if (LOG.isInfoEnabled())
			LOG.info( getCurrentUser().getName() + "进行了注销" );
		userSessionService.logout( session );
		session.clear();
		success();
	}

	/**
	 * 注册
	 * @return
	 */
	public void register() throws KnownException {
		if (checkLogin()) {
			code( Code.LOGINED );
			return;
		}
		UserForRegisterAndLogin user = new UserForRegisterAndLogin();
		user.name = model.name;
		user.password = model.password;
		userService.add( user );
		success( user );
	}

	/**
	 * 	获得一个用户的简单信息
	*	这个简单信息是任何人都可以访问的公开信息
	*	支持byid或byname
	 * @return
	 */
	public void simpleInfo() throws KnownException {
		UserForSimpleInfo su = null;
		if (model.id != 0) {
			su = userService.getSimpleInfo( model.id );
			//byid
		} else {
			//byname
			su = userService.getSimpleInfo( model.name );
		}
		if (su == null) {
			code( Code.NO_USER );
		} else {
			success( su );
		}
	}

	/**
	 * 显示当前用户的状态
	 * @return
	 */
	public void state() {
		User user = getCurrentUser();
		if (user != null) {
			user = userService.get( user.getId() );
			userSessionService.setCurrentUser( session, user );
			success(user);
		} else {
			code(Code.NOT_LOGIN);
		}
	}

	/**
	 * 更新用户信息
	 */
	public void update() throws KnownException {
		if (!checkLogin())
			return;

		User user = getCurrentUser();

		//先判断是否需要修改密码
		if (!StringUtils.isEmpty( model.oldPassword ) && !StringUtils.isEmpty( model.newPassword )) {

			//去修改密码
			user = userService.changePassword( user.getId(), model.oldPassword, model.newPassword );
			//如果为null 说明密码不正确
			if (user == null) {
				code( Code.WRONG_PASSWORD );
				return;
			}
			//否则就是修改成功了
			userSessionService.setCurrentUser( session, user );
		}
		//做其他字段的检验
		user.setPhone( model.phone );
		user.setQq( model.qq );
		user.setWeixin( model.weixin );
		user.setDesc( model.desc );
		userService.update( user );

		success( userService.getSimpleInfo( user.getId() ) );
	}
}
