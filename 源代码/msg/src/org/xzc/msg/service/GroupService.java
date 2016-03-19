package org.xzc.msg.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xzc.msg.app.MsgApplication;
import org.xzc.msg.domain.GroupForByid;
import org.xzc.msg.domain.GroupForList;
import org.xzc.msg.event.AddGroupResultEvent;
import org.xzc.msg.event.GroupByidResultEvent;
import org.xzc.msg.event.GroupListResultEvent;
import org.xzc.msg.http.MyJsonHandler;
import org.xzc.msg.params.ParamsForGroupList;
import org.xzc.msg.params.ParamsForRemoveUserFromGroup;
import org.xzc.msg.result.ResultBase;
import org.xzc.msg.utils.API;
import org.xzc.msg.utils.Callback;
import org.xzc.msg.utils.GsonUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class GroupService {
	public static GroupService sGroupService;

	public static GroupService getInstance() {
		if (sGroupService == null) {
			synchronized (GroupService.class) {
				if (sGroupService == null)
					sGroupService = new GroupService();
			}
		}
		return sGroupService;
	}

	public GroupService() {
		ahc = MsgApplication.ahc;
	}

	private AsyncHttpClient ahc;

	public void list(ParamsForGroupList p, final Callback<GroupListResultEvent> cb) {
		final GroupListResultEvent e = new GroupListResultEvent();
		e.success = false;
		RequestParams rp = new RequestParams();
		rp.put( "creatorId", p.creatorId );
		rp.put( "offset", p.offset );
		rp.put( "size", p.size );
		rp.put( "by", p.by );
		rp.put( "order", p.order );
		rp.put( "keyword", p.keyword );
		ahc.get( API.GROUP_LIST_URL, rp, new MyJsonHandler( e, cb ) {
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				e.total = json.getInt( "total" );
				JSONArray list = json.getJSONArray( "list" );
				List<GroupForList> groups = new ArrayList<GroupForList>();
				for (int i = 0; i < list.length(); ++i) {
					JSONObject jo = list.getJSONObject( i );
					GroupForList g = GsonUtils.fromJson( jo, GroupForList.class );
					groups.add( g );
				}
				e.groups = groups;
			}
		} );
	}

	public void byid(int groupId, final Callback<GroupByidResultEvent> cb) {
		final GroupByidResultEvent e = new GroupByidResultEvent();
		e.success = false;
		RequestParams rp = new RequestParams();
		rp.put( "id", groupId );
		ahc.get( API.GROUP_BYID_URL, rp, new MyJsonHandler( e, cb ) {
			public void onSuccess(JSONObject json) throws JSONException {
				GroupForByid group = GsonUtils.fromJson( json, GroupForByid.class );
				e.group = group;
			}
		} );
	}

	/**
	 * 添加一个群组
	 * @param name
	 * @param desc
	 */
	public void add(String name, String desc, final Callback<AddGroupResultEvent> cb) {
		final AddGroupResultEvent e = new AddGroupResultEvent();
		RequestParams rp = new RequestParams();
		rp.put( "name", name );
		rp.put( "desc", desc );
		RequestHandle post = ahc.post( API.GROUP_ADD_URL, rp, new MyJsonHandler( e, cb ) {
			public void onSuccess(JSONObject json) throws JSONException {
				e.groupId = json.getInt( "id" );
			}
		} );
	}

	/**
	 * 将用户加入到组里
	 * @param userId
	 * @param groupId
	 */
	public void addUserToGroup(int userId, int groupId, final Callback<ResultBase> cb) {
		final ResultBase e = new ResultBase();
		RequestParams rp = new RequestParams();
		rp.put( "userId", userId );
		rp.put( "groupId", groupId );
		ahc.get( API.GROUP_ADD_USER_TO_GROUP_URL, rp, new MyJsonHandler( e, cb ) );
	}

	public void removeUserFromGroup(ParamsForRemoveUserFromGroup p, final Callback<ResultBase> cb) {
		final ResultBase e = new ResultBase();
		RequestParams rp = new RequestParams();
		rp.put( "userId", p.userId );
		rp.put( "groupId", p.groupId );
		ahc.get( API.GROUP_REMOVE_USER_FROM_GROUP_URL, rp, new MyJsonHandler( e, cb ) );
	}
}
