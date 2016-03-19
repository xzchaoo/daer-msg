package org.xzc.msg.ui.fragments;

import java.text.SimpleDateFormat;

import org.xzc.msg.R;
import org.xzc.msg.domain.SimpleMessage;
import org.xzc.msg.domain.UserForSimpleInfo;
import org.xzc.msg.event.MessageByIdResultEvent;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.ui.activity.MessageDetailActivity;
import org.xzc.msg.utils.ActivityUtils;
import org.xzc.msg.utils.Callback;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.shizhefei.fragment.LazyFragment;

public class SimpleMessageDetailFragment extends LazyFragment {

	public static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	public static final String TAG = "MessageDetailFragment";

	private UserForSimpleInfo mCreator;
	private ProgressDialog mProgressDialog;
	private SimpleMessage mSimpleMessage;
	@ViewInject(R.id.tv_content)
	private TextView mTVContent;
	@ViewInject(R.id.tv_createTime)
	private TextView mTVCreateTime;
	@ViewInject(R.id.tv_creator)
	private TextView mTVCreator;
	@ViewInject(R.id.tv_endTime)
	private TextView mTVEndTime;

	@ViewInject(R.id.tv_location)
	private TextView mTVLocation;

	@ViewInject(R.id.tv_name)
	private TextView mTVName;

	@ViewInject(R.id.tv_startTime)
	private TextView mTVStartTime;

	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_simple_message_detail );

		ViewUtils.inject( this, getContentView() );
		int id = getArguments().getInt( MessageDetailActivity.ARGS_MSG_ID );

		mProgressDialog = ProgressDialog.show( getActivity(), "提示", "加载中...", true, true, new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				getActivity().finish();
			}
		} );
		MessageService.getInstance().byid( id, new Callback<MessageByIdResultEvent>() {
			public void onResult(MessageByIdResultEvent e) {
				mProgressDialog.dismiss();
				if (e.success) {
					mSimpleMessage = (SimpleMessage) e.message;
					mCreator = e.creator;

					mTVCreator.setText( Html.fromHtml( "<u>" + mCreator.name + "</u>" ) );
					mTVCreator.setEnabled( true );

					mTVName.setText( mSimpleMessage.name );
					mTVCreateTime.setText( SDF.format( mSimpleMessage.createTime ) );
					if (mSimpleMessage.location != null)
						mTVLocation.setText( mSimpleMessage.location );
					else {
						( (View) mTVLocation.getParent() ).setVisibility( View.GONE );
					}

					if (mSimpleMessage.startTime != null)
						mTVStartTime.setText( SDF.format( mSimpleMessage.startTime ) );
					else {
						( (View) mTVStartTime.getParent() ).setVisibility( View.GONE );
					}

					if (mSimpleMessage.endTime != null)
						mTVEndTime.setText( SDF.format( mSimpleMessage.endTime ) );
					else {
						( (View) mTVEndTime.getParent() ).setVisibility( View.GONE );
					}

					mTVContent.setText( mSimpleMessage.content );

				} else {
					new AlertDialog.Builder( getActivity() ).setTitle( "提示" ).setMessage( "消息已经不存在了. id=" + e.id )
							.show().setOnDismissListener( new OnDismissListener() {
								public void onDismiss(DialogInterface dialog) {
									getActivity().finish();
								}
							} );
				}
			}
		} );
	}

	@OnClick(R.id.tv_creator)
	public void onCreatorNameClick(View v) {
		ActivityUtils.toUserDetail( getActivity(), mCreator.id );
	}
}
