package org.xzc.msg.ui.fragments;

import org.xzc.msg.R;
import org.xzc.msg.domain.SJTUJWCMessage;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.event.MessageByIdResultEvent;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.ui.activity.MessageDetailActivity;
import org.xzc.msg.utils.ActivityUtils;
import org.xzc.msg.utils.Callback;
import org.xzc.msg.utils.DateUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.shizhefei.fragment.LazyFragment;

public class SJTUJWCMessageDetailFragment extends LazyFragment {

	/**
	 * 消息的id
	 */
	private int id;

	@ViewInject(R.id.btn_view_in_sjtujwc)
	private Button mBTNViewInSjtujwc;

	/**
	 * 消息
	 */
	private SJTUJWCMessage message;

	private ProgressDialog mProgressDialog;

	@ViewInject(R.id.tv_createTime)
	private TextView mTVCreateTime;
	@ViewInject(R.id.tv_creator)
	private TextView mTVCreator;
	@ViewInject(R.id.tv_name)
	private TextView mTVName;

	@ViewInject(R.id.wv_content)
	private WebView mWVContent;

	/**
	 * 打开到 交大教务处的 网站
	 * @param v
	 */
	@OnClick(R.id.btn_view_in_sjtujwc)
	public void viewInSjtujwc(View v) {
		Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( message.link ) );
		startActivity( intent );
	}

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_sjtujwc_message_detail );
		id = getArguments().getInt( MessageDetailActivity.ARGS_MSG_ID );
		ViewUtils.inject( this, getContentView() );
		MessageService.getInstance().byid( id, new Callback<MessageByIdResultEvent>() {
			public void onResult(MessageByIdResultEvent e) {
				mProgressDialog.dismiss();
				if (e.success) {
					//设置按钮为可用
					mBTNViewInSjtujwc.setEnabled( true );

					message = (SJTUJWCMessage) e.message;
					UserForSimpleInfo creator = e.creator;

					mTVCreator.setText( "交大教务处" );
					mTVName.setText( message.name );
					mTVCreateTime.setText( DateUtils.format( message.createTime ) );
					mWVContent.loadData( message.content, "text/html;charset=utf-8", "utf-8" );
					//mWVContent.loadUrl(message.link);

				} else {
					new AlertDialog.Builder( getActivity() ).setTitle( "提示" ).setMessage( "消息已经不存在了. id=" + id ).show()
							.setOnDismissListener( new OnDismissListener() {
								public void onDismiss(DialogInterface dialog) {
									getActivity().finish();
								}
							} );
				}

			}
		} );
		mProgressDialog = ProgressDialog.show( getActivity(), "提示", "加载中...", false );
	}

	@OnClick(R.id.tv_creator)
	public void onCreatorNameClick(View v) {
		//Toast.makeText( getActivity(), "点击了" + message.creatorId, Toast.LENGTH_SHORT ).show();
		ActivityUtils.toUserDetail( getActivity(), message.creatorId );
	}
}
