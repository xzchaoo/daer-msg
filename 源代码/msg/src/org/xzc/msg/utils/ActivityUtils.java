package org.xzc.msg.utils;

import org.xzc.msg.LoginActivity;
import org.xzc.msg.MainActivity;
import org.xzc.msg.ui.activity.GroupAddActivity;
import org.xzc.msg.ui.activity.GroupDetailActivity;
import org.xzc.msg.ui.activity.MessageDetailActivity;
import org.xzc.msg.ui.activity.PreferencesSettingActivity;
import org.xzc.msg.ui.activity.PublishMessageActivity;
import org.xzc.msg.ui.activity.UserDetailActivity;
import org.xzc.msg.ui.activity.UserInfoUpdateActivity;

import android.content.Context;
import android.content.Intent;

public class ActivityUtils {

	public static void toLoginActivity(Context ctx) {
		ctx.startActivity( new Intent( ctx, LoginActivity.class ) );
	}

	public static void toMainActivity(Context ctx) {
		ctx.startActivity( new Intent( ctx, MainActivity.class ) );
	}

	public static void toPublishMessageActivity(Context ctx) {
		ctx.startActivity( new Intent( ctx, PublishMessageActivity.class ) );
	}

	public static void toMessageDetailActivity(Context ctx, int id, int type) {
		Intent intent = new Intent( ctx, MessageDetailActivity.class );
		intent.putExtra( MessageDetailActivity.ARGS_MSG_ID, id );
		intent.putExtra( MessageDetailActivity.ARGS_MSG_TYPE, type );
		ctx.startActivity( intent );
	}

	public static void toUserDetail(Context ctx, int userId) {
		Intent intent = new Intent( ctx, UserDetailActivity.class );
		intent.putExtra( UserDetailActivity.ARGS_USER_ID, userId );
		ctx.startActivity( intent );
	}

	public static void toUserInfoUpdateActivity(Context ctx) {
		Intent intent = new Intent( ctx, UserInfoUpdateActivity.class );
		ctx.startActivity( intent );
	}

	public static void toGroupDetailActivity(Context ctx, int groupId) {
		Intent intent = new Intent( ctx, GroupDetailActivity.class );
		intent.putExtra( GroupDetailActivity.ARGS_GROUP_ID, groupId );
		ctx.startActivity( intent );
	}

	public static void toGroupAddActivity(Context ctx) {
		Intent intent = new Intent( ctx, GroupAddActivity.class );
		ctx.startActivity( intent );
	}

	public static void toPreferencesSettingActivity(Context ctx) {
		Intent intent = new Intent( ctx, PreferencesSettingActivity.class );
		ctx.startActivity( intent );
	}
}
