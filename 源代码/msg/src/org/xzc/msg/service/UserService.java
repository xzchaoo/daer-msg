package org.xzc.msg.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.xzc.msg.app.MsgApplication;
import org.xzc.msg.domain.User;
import org.xzc.msg.domain.UserForDetail;
import org.xzc.msg.domain.UserForUpdate;
import org.xzc.msg.event.UserDetailResultEvent;
import org.xzc.msg.event.UserJoinGroupResultEvent;
import org.xzc.msg.http.MyJsonHandler;
import org.xzc.msg.result.ResultBase;
import org.xzc.msg.utils.API;
import org.xzc.msg.utils.Callback;
import org.xzc.msg.utils.GsonUtils;
import org.xzc.msg.utils.PrefsUtils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class UserService {
	private static AsyncHttpClient ahc;

	private static UserService sUserService;

	public static UserService getInstance() {
		if (sUserService == null) {
			synchronized (UserService.class) {
				if (sUserService == null)
					sUserService = new UserService();
			}
		}
		return sUserService;
	}

	private User mCurrentUser;

	private boolean mLogin = false;

	public UserService() {
		ahc = MsgApplication.ahc;
	}

	/**
	 * 获得上一次登陆的用户名
	 * @return
	 */
	public String getLastLoginUserName() {
		return PrefsUtils.allowRememberLastUsername() ? PrefsUtils.getSharedPreferences( SP_NAME ).getString(
				LAST_LOGIN_USER_NAME, "" ) : "";
	}

	public static final String LAST_LOGIN_USER_NAME = "lastLoginUserName";

	public void setLastLoginuserName(String name) {
		PrefsUtils.getSharedPreferences( SP_NAME ).edit().putString( LAST_LOGIN_USER_NAME, name ).commit();
	}

	public static final String SP_NAME = "user";
	public static final String LOGINED_KEY = "logined";

	public boolean canAutoLogin() {
		if (PrefsUtils.allowAutoLogin()) {
			return PrefsUtils.getSharedPreferences( SP_NAME ).getBoolean( LOGINED_KEY, false );
		} else {
			MsgApplication.pcs.clear();
			return false;
		}
	}

	public RequestHandle login(String name, String password, Callback<ResultBase> cb) {
		final ResultBase e = new ResultBase();
		RequestParams params = new RequestParams();
		params.put( "name", name );
		params.put( "password", password );
		return ahc.post( API.LOGIN_URL, params, new MyJsonHandler( e, cb ) {
			@Override
			public void onSuccess(JSONObject json) {
				mLogin = true;
				mCurrentUser = GsonUtils.fromJson( json, User.class );
				setCanAutoLogin( true );
				setLastLoginuserName( mCurrentUser.name );
			}
		} );
	}

	public void register(String name, String password, Callback<ResultBase> cb) {
		ResultBase e = new ResultBase();
		RequestParams rp = new RequestParams();
		rp.put( "name", name );
		rp.put( "password", password );
		ahc.post( API.REGISTER_URL, rp, new MyJsonHandler( e, cb ) );
	}

	public User getCurrentUserLocally() {
		return mCurrentUser;
	}

	public RequestHandle getCurrentUserInfo(Callback<ResultBase> cb) {
		final ResultBase e = new ResultBase();
		return ahc.get( API.USER_STATE_URL, new MyJsonHandler( e, cb ) {
			@Override
			public void onSuccess(JSONObject json) {
				mCurrentUser = GsonUtils.fromJson( json, User.class );
			}
		} );
	}

	/**
	 * 获得用户的简单信息
	 * @param id
	 */
	public void getUserSimpleInfo(int id, Callback<UserDetailResultEvent> cb) {
		final UserDetailResultEvent e = new UserDetailResultEvent();
		e.success = false;
		RequestParams rp = new RequestParams();
		rp.put( "id", id );
		ahc.get( API.USER_DETAIL_URL, rp, new MyJsonHandler( e, cb ) {
			public void onSuccess(JSONObject json) {
				UserForDetail user = GsonUtils.fromJson( json, UserForDetail.class );
				e.user = user;
			}
		} );
	}

	public boolean isLogin() {
		return mLogin;
	}

	/**
	 * 注销
	 * @param ctx
	 */
	public void logout() {
		mLogin = false;
		setCanAutoLogin( false );
		MsgApplication.pcs.clear();//清除cookie
	}

	/**
	 * 尝试自动登录 主要是往state地址发get请求 然后查看是否返回isLogin=true
	 */
	public void tryAutoLogin(Callback<ResultBase> cb) {
		setCanAutoLogin( false );
		ResultBase e = new ResultBase();
		ahc.get( API.USER_STATE_URL, new MyJsonHandler( e, cb ) {
			@Override
			public void onFailure(int statusCode, int code, String msg) {
				mLogin = false;
				setCanAutoLogin( false );
				MsgApplication.pcs.clear();
				super.onFailure( statusCode, code, msg );
			}

			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				mCurrentUser = GsonUtils.fromJson( json, User.class );
				mLogin = true;
				setCanAutoLogin( true );
			}

		} );

	}

	/**
	 * 设置下次可否自动登录
	 * @param b
	 */
	private static void setCanAutoLogin(boolean b) {
		PrefsUtils.getSharedPreferences( SP_NAME ).edit().putBoolean( LOGINED_KEY, b ).commit();
	}

	public RequestHandle updateCurrentUser(UserForUpdate user, Callback<ResultBase> cb) {
		final ResultBase e = new ResultBase();
		RequestParams rp = new RequestParams();
		rp.put( "id", user.id );
		rp.put( "phone", user.phone );
		rp.put( "qq", user.qq );
		rp.put( "weixin", user.weixin );
		rp.put( "desc", user.desc );
		rp.put( "oldPassword", user.oldPassword );
		rp.put( "newPassword", user.newPassword );

		return ahc.post( API.User_UPDATE_URL, rp, new MyJsonHandler( e, cb ) {
			@Override
			public void onSuccess(JSONObject json) {
				mCurrentUser = GsonUtils.fromJson( json, User.class );
			}
		} );
	}

	/**
	 * 查看某个用户是否加入了某个组
	 */
	public void isUserJoinGroup(int userId, int groupId, Callback<UserJoinGroupResultEvent> cb) {
		final UserJoinGroupResultEvent e = new UserJoinGroupResultEvent();
		RequestParams rp = new RequestParams();
		rp.put( "userId", userId );
		rp.put( "groupId", groupId );
		ahc.get( API.GROUP_IS_USER_IN_GROUP_URL, rp, new MyJsonHandler( e, cb ) {
			public void onSuccess(JSONObject json) throws JSONException {
				e.isUserJoinGroup = json.getBoolean( "isUserJoinGroup" );
			}
		} );
	}
}
