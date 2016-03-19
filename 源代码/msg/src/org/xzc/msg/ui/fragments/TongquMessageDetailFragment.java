package org.xzc.msg.ui.fragments;

import org.xzc.msg.R;
import org.xzc.msg.domain.TongquMessage;
import org.xzc.msg.event.MessageByIdResultEvent;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.ui.activity.MessageDetailActivity;
import org.xzc.msg.utils.Callback;
import org.xzc.msg.utils.DateUtils;
import org.xzc.msg.utils.MyHttpUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.shizhefei.fragment.LazyFragment;

public class TongquMessageDetailFragment extends LazyFragment {
	private int actid;

	@ViewInject(R.id.tv_baoming)
	private TextView baoming;

	@ViewInject(R.id.wv_content)
	private WebView content;

	@ViewInject(R.id.tv_createTime)
	private TextView createTime;

	@ViewInject(R.id.tv_huodong)
	private TextView huodong;

	private int id;

	@ViewInject(R.id.tv_location)
	private TextView location;

	@ViewInject(R.id.tv_name)
	private TextView name;

	@ViewInject(R.id.tv_renshu)
	private TextView renshu;

	@ViewInject(R.id.tv_source)
	private TextView source;

	@OnClick(R.id.btn_view_in_tongqu)
	public void gotoTongquDetail(View v) {
		Intent intent = new Intent( Intent.ACTION_VIEW );
		intent.setData( Uri.parse( "http://www.tongqu.me/index.php/act/detail/view/" + actid ) );
		startActivity( intent );
	}

	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_tongqu_message_detail );
		ViewUtils.inject( this, this.getContentView() );

		//保存id
		id = getArguments().getInt( MessageDetailActivity.ARGS_MSG_ID );

		//请求消息
		MessageService.getInstance().byid( id,new Callback<MessageByIdResultEvent>() {
			public void onResult(MessageByIdResultEvent e) {
				if (e.success) {
					TongquMessage m = (TongquMessage) e.message;
					name.setText( m.name );
					createTime.setText( DateUtils.format( m.createTime ) );
					content.loadData( MyHttpUtils.format( m.content ), "text/html;charset=utf-8", "utf-8" );
					huodong.setText( DateUtils.format( m.startTime ) + " 至 " + DateUtils.format( m.endTime ) );
					baoming.setText( DateUtils.format( m.signStartTime ) + " 至 " + DateUtils.format( m.signEndTime ) );
					location.setText( m.location );
					renshu.setText( m.memberCount + "/" + ( m.maxMember == 0 ? "人数不限" : m.maxMember ) );
					source.setText( m.source );
					actid = m.actid;
				} else {
					new AlertDialog.Builder( getActivity() ).setTitle( "提示" ).setMessage( "无法获取该消息" )
							.setOnCancelListener( new OnCancelListener() {
								public void onCancel(DialogInterface dialog) {
									getActivity().finish();
								}
							} ).show();
				}
			}
		} );
	}
}
