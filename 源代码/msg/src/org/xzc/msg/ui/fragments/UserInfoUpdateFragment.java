package org.xzc.msg.ui.fragments;

import java.util.List;

import org.xzc.msg.R;
import org.xzc.msg.domain.User;
import org.xzc.msg.domain.UserForUpdate;
import org.xzc.msg.result.ResultBase;
import org.xzc.msg.service.UserService;
import org.xzc.msg.utils.Callback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestHandle;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.shizhefei.fragment.LazyFragment;

public class UserInfoUpdateFragment extends LazyFragment implements ValidationListener {
	@ViewInject(R.id.et_desc)
	private EditText mETDesc;
	@ViewInject(R.id.et_new_password)
	private EditText mETNewPassword;
	@ViewInject(R.id.et_old_password)
	private EditText mETOldPassword;
	@ViewInject(R.id.et_phone)
	private EditText mETPhone;
	
	@Length(min = 5, max = 11, message = "QQ号长度必须介于5-11")
	@ViewInject(R.id.et_qq)
	private EditText mETQQ;

	@ViewInject(R.id.et_weixin)
	private EditText mETWeixin;

	@ViewInject(R.id.tb_password)
	private ToggleButton mTBPassword;

	@ViewInject(R.id.tv_name)
	private TextView mTVName;
	private Validator mValidator;
	private User mCurrentUser;

	private ProgressDialog mProgressDialog;
	private RequestHandle mRequestHandle;

	protected void onCreateViewLazy(Bundle savedInstanceState) {
		setContentView( R.layout.f_user_info_update );
		ViewUtils.inject( this, getContentView() );

		mProgressDialog = new ProgressDialog( getActivity() );
		mProgressDialog.setTitle( "提示" );
		mProgressDialog.setMessage( "正在更新..." );
		mProgressDialog.setCancelable( true );
		mProgressDialog.setCanceledOnTouchOutside( false );
		mProgressDialog.setOnCancelListener( new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				if (mRequestHandle != null) {
					mRequestHandle.cancel( true );
					mRequestHandle.shouldBeGarbageCollected();
					mRequestHandle = null;
				}
			}
		} );

		mTBPassword.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					( (View) mETOldPassword.getParent() ).setVisibility( View.VISIBLE );
					( (View) mETNewPassword.getParent() ).setVisibility( View.VISIBLE );
				} else {
					( (View) mETOldPassword.getParent() ).setVisibility( View.GONE );
					( (View) mETNewPassword.getParent() ).setVisibility( View.GONE );
				}
			}
		} );
		mProgressDialog.setMessage( "正在获取当前用户信息..." );
		mProgressDialog.show();
		mRequestHandle = UserService.getInstance().getCurrentUserInfo( new Callback<ResultBase>() {
			public void onResult(ResultBase e) {
				sendDismissProgressDialogRequest();
				if (e.success) {
					User user = mCurrentUser = UserService.getInstance().getCurrentUserLocally();
					mTVName.setText( user.name );
					mETPhone.setText( user.phone );
					mETQQ.setText( user.qq );
					mETWeixin.setText( user.weixin );
					mETDesc.setText( user.desc );
					mBTNSubmit.setEnabled( true );
				} else {
					Toast.makeText( getActivity(), "获取当前用户信息失败,请检查登陆是否已经过时.", Toast.LENGTH_SHORT ).show();
				}
			}
		} );

		setUpValidator();
	}

	private void setUpValidator() {
		mValidator = new Validator( this );
		mValidator.setValidationListener( this );
		mValidator.put( mETOldPassword, new QuickRule<EditText>() {

			@Override
			public boolean isValid(EditText view) {
				if (!mTBPassword.isChecked())
					return true;
				int length = view.getText().toString().length();
				return length >= 6 && length <= 16;
			}

			@Override
			public String getMessage(Context context) {
				return "旧密码的长度必须介于6-16.";
			}

		} );

		mValidator.put( mETNewPassword, new QuickRule<EditText>() {
			public boolean isValid(EditText view) {
				if (!mTBPassword.isChecked())
					return true;
				int length = view.getText().toString().length();
				return length >= 6 && length <= 16;
			}

			@Override
			public String getMessage(Context context) {
				return "新密码的长度必须介于6-16.";
			}

		} );
	}

	@Override
	protected void onFragmentStartLazy() {
		getActivity().setTitle( "当前用户详情" );
	}

	//用于延时关闭 progress dialog
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mProgressDialog.dismiss();
		}
	};

	private void sendDismissProgressDialogRequest() {
		mHandler.sendEmptyMessageDelayed( 1, 400 );
	}

	@Override
	public void onValidationSucceeded() {
		UserForUpdate user = new UserForUpdate();
		user.id = mCurrentUser.id;
		user.phone = mETPhone.getText().toString();
		user.qq = mETQQ.getText().toString();
		user.weixin = mETWeixin.getText().toString();
		user.desc = mETDesc.getText().toString();
		user.oldPassword = mETOldPassword.getText().toString();
		user.newPassword = mETNewPassword.getText().toString();
		mBTNSubmit.setEnabled( false );
		mProgressDialog.setMessage( "正在更新..." );
		mProgressDialog.show();
		mRequestHandle = UserService.getInstance().updateCurrentUser( user, new Callback<ResultBase>() {
			public void onResult(ResultBase e) {
				mBTNSubmit.setEnabled( true );
				sendDismissProgressDialogRequest();
				if (e.success) {
					Toast.makeText( getActivity(), "更新成功", Toast.LENGTH_SHORT ).show();
				} else {
					Toast.makeText( getActivity(), e.msg, Toast.LENGTH_SHORT ).show();
				}
			}
		} );
	}

	@Override
	public void onValidationFailed(List<ValidationError> errors) {
		boolean canToast = true;
		for (ValidationError e : errors) {
			View view = e.getView();
			if (view instanceof EditText) {
				( (EditText) view ).setError( e.getCollatedErrorMessage( getActivity() ) );
			} else if (canToast) {
				canToast = false;
				Toast.makeText( getActivity(), e.getCollatedErrorMessage( getActivity() ), Toast.LENGTH_SHORT ).show();
			}
		}
	}

	@ViewInject(R.id.btn_submit)
	private Button mBTNSubmit;

	@OnClick(R.id.btn_submit)
	public void onSubmit(View v) {
		mValidator.validate();
	}
}
