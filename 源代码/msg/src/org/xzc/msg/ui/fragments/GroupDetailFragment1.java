package org.xzc.msg.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import org.xzc.msg.R;
import org.xzc.msg.domain.GroupForByid;
import org.xzc.msg.domain.IdAndName;
import org.xzc.msg.domain.User;
import org.xzc.msg.event.GroupByidResultEvent;
import org.xzc.msg.event.UserJoinGroupResultEvent;
import org.xzc.msg.params.ParamsForRemoveUserFromGroup;
import org.xzc.msg.result.ResultBase;
import org.xzc.msg.service.GroupService;
import org.xzc.msg.service.UserService;
import org.xzc.msg.ui.activity.GroupDetailActivity;
import org.xzc.msg.ui.fragments.GroupDetailTabsFragment.IGroupProvider;
import org.xzc.msg.utils.ActivityUtils;
import org.xzc.msg.utils.Callback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.shizhefei.fragment.LazyFragment;

public class GroupDetailFragment1 extends LazyFragment {
	private IGroupProvider gp;

	public void onAttach(Activity activity) {
		super.onAttach( activity );
		mActivity = (GroupDetailActivity) activity;
	}

	public GroupDetailFragment1(IGroupProvider gp) {
		this.gp = gp;
		mGroup = gp.getGroup();
	}

	public static final int HANDLER_FLAG_REFRESH = 1;

	@ViewInject(R.id.btn_submit)
	private Button mBTNSubmit;

	private GroupForByid mGroup;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_FLAG_REFRESH:
				mProgressDialog.dismiss();
				break;
			}
		}
	};

	private List<IdAndName> mMembers = new ArrayList<IdAndName>();

	private ProgressDialog mProgressDialog;

	@ViewInject(R.id.tv_creator)
	private TextView mTVCreator;

	@ViewInject(R.id.tv_desc)
	private TextView mTVDesc;

	@ViewInject(R.id.tv_memberCount)
	private TextView mTVMemberCount;

	@ViewInject(R.id.tv_name)
	private TextView mTVName;

	@OnClick(R.id.tv_creator)
	public void onCreatorNameClick(View v) {
		ActivityUtils.toUserDetail( getActivity(), mGroup.creator.id );
	}

	/**
	 * 用于表示 "加入" "退出" 按钮该如何显示
	 * 0的话 隐藏该按钮
	 * 1的话显示加入
	 * 2的话显示退出
	 */
	private int mMode = 0;

	private GroupDetailActivity mActivity;

	private void updateGroup() {
		//加入组成功 更新组信息
		GroupService.getInstance().byid( mGroup.id, new Callback<GroupByidResultEvent>() {
			public void onResult(GroupByidResultEvent e) {
				if (e.success) {
					gp.setGroup( e.group );
					mGroup = e.group;
					setGroupInfo();
				} else {
					Toast.makeText( getActivity(), e.msg != null ? e.msg : "获取群组信息失败,请检查网络情况.", Toast.LENGTH_SHORT )
							.show();
				}
			}
		} );
	}

	/**
	 * 当点击了 加入
	 */
	@OnClick(R.id.btn_submit)
	public void onSubmit(View v) {
		if (mMode == 1) {
			User cu = UserService.getInstance().getCurrentUserLocally();
			mProgressDialog.show();
			GroupService.getInstance().addUserToGroup( cu.id, mGroup.id, new Callback<ResultBase>() {
				public void onResult(ResultBase e) {
					if (e.success) {
						mMode = 2;
						updateGroup();
					} else {
						mProgressDialog.setMessage( "失败:" + e.msg );
					}
					mHandler.sendEmptyMessageDelayed( HANDLER_FLAG_REFRESH, 400 );
				}
			} );
		} else if (mMode == 2) {
			User cu = UserService.getInstance().getCurrentUserLocally();
			mProgressDialog.show();
			ParamsForRemoveUserFromGroup p = new ParamsForRemoveUserFromGroup();
			p.userId = cu.id;
			p.groupId = mGroup.id;
			GroupService.getInstance().removeUserFromGroup( p, new Callback<ResultBase>() {
				public void onResult(ResultBase e) {
					if (e.success) {
						mMode = 1;
						updateGroup();
					} else {
						mProgressDialog.setMessage( "失败:" + e.msg );
					}
					mHandler.sendEmptyMessageDelayed( HANDLER_FLAG_REFRESH, 400 );
				}
			} );
		}
	}

	private void setGroupInfo() {
		mMembers.addAll( mGroup.members );
		mTVName.setText( mGroup.name );
		mTVDesc.setText( mGroup.desc );
		mTVCreator.setText( mGroup.creator.name );
		mTVMemberCount.setText( "" + mGroup.memberCount );
		//当当前用户不是创建者的时候 允许其加入
		User cu = UserService.getInstance().getCurrentUserLocally();
		if (cu.id != mGroup.creator.id) {
			UserService.getInstance().isUserJoinGroup( cu.id, mGroup.id, new Callback<UserJoinGroupResultEvent>() {
				public void onResult(UserJoinGroupResultEvent e) {
					if (e.success) {
						if (e.isUserJoinGroup) {
							mMode = 2;
							mBTNSubmit.setText( "退出" );
						} else {
							mMode = 1;
							mBTNSubmit.setText( "加入" );
						}
						mBTNSubmit.setVisibility( View.VISIBLE );
					}
				}
			} );
		}
	}

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_group_detail_1 );
		ViewUtils.inject( this, getContentView() );

		mProgressDialog = new ProgressDialog( getActivity() );
		mProgressDialog.setTitle( "提示" );
		mProgressDialog.setMessage( "正在发送请求" );
		mProgressDialog.setCancelable( true );
		mProgressDialog.setOnCancelListener( new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				getActivity().finish();
			}
		} );
		mProgressDialog.setCanceledOnTouchOutside( false );
		mProgressDialog.setIndeterminate( true );

		setGroupInfo();

	}

}
