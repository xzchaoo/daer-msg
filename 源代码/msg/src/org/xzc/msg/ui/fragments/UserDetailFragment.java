package org.xzc.msg.ui.fragments;

import org.xzc.msg.R;
import org.xzc.msg.domain.UserForDetail;
import org.xzc.msg.event.UserDetailResultEvent;
import org.xzc.msg.service.UserService;
import org.xzc.msg.utils.Callback;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.shizhefei.fragment.LazyFragment;

/**
 * 显示用户简单信息的Fragment 比如当查看别的用户的信息的时候
 * @author xzchaoo
 *
 */
public class UserDetailFragment extends LazyFragment {
	public static final String ARGS_USER_ID = "user.id";
	/**
	 *要查看信息的用户的id
	 */
	private int id;

	@ViewInject(R.id.tv_name)
	private TextView mTVName;
	@ViewInject(R.id.tv_phone)
	private TextView mTVPhone;
	@ViewInject(R.id.tv_qq)
	private TextView mTVQQ;
	@ViewInject(R.id.tv_weixin)
	private TextView mTVWeixin;
	@ViewInject(R.id.tv_desc)
	private TextView mTVDesc;
	@ViewInject(R.id.tv_publishMessageCount)
	private TextView mTVPublishMessageCount;
	private UserForDetail mUser;

	/**
	 * 当用户点击了 "查看" 显示跟这个用户相关的消息
	 * @param v
	 */
	@OnClick(R.id.btn_view_messages)
	public void onViewMessagesClick(View v) {
		Toast.makeText( getActivity(), "还没有实现", Toast.LENGTH_SHORT ).show();
	}

	protected void onCreateViewLazy(Bundle savedInstanceState) {
		id = getArguments().getInt( ARGS_USER_ID );
		setContentView( R.layout.f_user_detail );
		ViewUtils.inject( this, getContentView() );
		UserService.getInstance().getUserSimpleInfo( id ,new Callback<UserDetailResultEvent>() {
			public void onResult(UserDetailResultEvent e) {
				if (e.success) {
					mUser = e.user;
					mTVName.setText( mUser.name );
					mTVPhone.setText( mUser.phone );
					mTVQQ.setText( mUser.qq );
					mTVWeixin.setText( mUser.weixin );
					mTVDesc.setText( mUser.desc );
					mTVPublishMessageCount.setText( "" + mUser.publishMessageCount );
				} else {
					Toast.makeText( getActivity(), "获取用户详情失败,请检查网络情况.", Toast.LENGTH_SHORT ).show();
				}
			}
		});
	}

}
