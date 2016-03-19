package org.xzc.msg.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.xzc.msg.action.code.Code;
import org.xzc.msg.action.json.MyJSON;
import org.xzc.msg.domain.User;
import org.xzc.msg.interceptor.IMyJSONSupported;
import org.xzc.msg.service.UserSessionService;

public class ActionBase implements SessionAware, IMyJSONSupported {

	public static final Map<Code, String> DESC = new HashMap<Code, String>();

	public static final Log LOG = LogFactory.getLog( ActionBase.class );

	static {
		DESC.put( Code.SUCCESS, "成功" );
		DESC.put( Code.UNKNOWN_ERROR, "未知的错误" );
		DESC.put( Code.NO_MESSAGE, "id对应的消息不存在" );
		DESC.put( Code.WRONG_PASSWORD, "密码错误" );
		DESC.put( Code.LOGINED, "您已经登陆了" );
		DESC.put( Code.WRONG_NAME_FORMAT, "账号长度必须介于3-16,只能使用大小写字母,下划线和数字." );
		DESC.put( Code.WRONG_PASSWORD_FORMAT, "密码长度必须介于6-16,只能使用大小写字母,下划线和数字." );
		DESC.put( Code.NAME_EXISTS, "用户名已经存在." );
		DESC.put( Code.NOT_LOGIN, "您还没有登陆呢." );
		//DESC.put( Code.USER_NOT_EXISTS, "用户不存在." );
		DESC.put( Code.EMPTY_MESSAGE_TO_ADD, "请输入你要添加的消息." );
		DESC.put( Code.UNSUPPORTED_TYPE, "不支持的消息类型." );
		DESC.put( Code.INVALID_MESSAGE, "添加消息发生异常,请检查你的消息内容与格式." );
		DESC.put( Code.USER_MESSAGE_REPETITION, "用户已经和该消息关联了." );
		DESC.put( Code.NO_USER_MESSAGE, "id对应的用户消息不存在." );
		DESC.put( Code.EXCEPTION, "发生异常" );
		DESC.put( Code.NO_USER, "用户不存在" );
		DESC.put( Code.NO_AUTHORITY, "没有权限." );
		DESC.put( Code.NO_GROUP, "组不存在." );
		DESC.put( Code.REPEATED_USER_GROUP, "你已经有一个同名的组了." );
		DESC.put( Code.REPEATED_MESSAGE_IN_GROUP, "该消息已经在该组里了.");
		DESC.put( Code.WRONG_USER_FORMAT, "账号长度必须介于3-16,密码长度必须介于6-16,且只能使用大小写字母,下划线和数字.");
		DESC.put( Code.REPEATED_USER_IN_GROUP, "该用户已经在该组里了.");
		DESC.put( Code.INVALID_OPERATION, "无效的操作.");
	}
	protected final MyJSON json = new MyJSON().code( Code.UNKNOWN_ERROR );
	protected Map<String, Object> session;

	@Resource
	protected UserSessionService userSessionService;

	public MyJSON getJson() {
		if (json.getMsg() == null)
			json.msg( DESC.get( json.code() ) );
		return json;
	}

	public MyJSON getMyJSON() {
		return json;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	protected boolean checkLogin() {
		User user = getCurrentUser();
		if (user == null) {
			code( Code.NOT_LOGIN );
			return false;
		}
		return true;
	}

	protected MyJSON code(Code code) {
		return json.code( code );
	}

	protected MyJSON exception(Exception e) {
		return code( Code.SUCCESS ).msg( e.getMessage() );
	}

	protected User getCurrentUser() {
		return userSessionService.getCurrentUser( session );
	}

	protected MyJSON success() {
		return code( Code.SUCCESS );
	}

	protected MyJSON success(Object result) {
		return success().result( result );
	}
}
