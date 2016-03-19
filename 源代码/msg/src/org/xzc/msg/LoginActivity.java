package org.xzc.msg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xzc.msg.result.ResultBase;
import org.xzc.msg.service.UserService;
import org.xzc.msg.ui.activity.OptionsSelectActivity;
import org.xzc.msg.utils.ActivityUtils;
import org.xzc.msg.utils.Callback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestHandle;

@ContentView(R.layout.activity_login)
public class LoginActivity extends Activity {

	private ProgressDialog mProgressDialog;

	@ViewInject(R.id.et_password)
	private EditText mETPassword;

	@ViewInject(R.id.et_name)
	private EditText mETName;

	private static final int HANDLE_FLAG_DISMISS_PROGRESS_DIALOG = 1;
	private static final int HANDLE_FLAG_LOGIN_SUCCESS = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_FLAG_DISMISS_PROGRESS_DIALOG:
				mAlertDialog.dismiss();
				mProgressDialog.dismiss();
				break;
			case HANDLE_FLAG_LOGIN_SUCCESS:
				mAlertDialog.dismiss();
				mProgressDialog.dismiss();
				ActivityUtils.toMainActivity( LoginActivity.this );
				finish();
				break;
			}
		}
	};

	private AlertDialog mAlertDialog;

	private Pattern VALID_USER_NAME_PATTERN = Pattern.compile( "^[a-zA-Z0-9_]{3,16}$" );
	private Pattern VALID_USER_PASSWORD_PATTERN = Pattern.compile( "^[a-zA-Z0-9_]{6,16}$" );

	private static boolean matches(String text, Pattern pattern) {
		Matcher matcher = pattern.matcher( text );
		return matcher.find();
	}

	private boolean checkNameAndPassword() {
		String name = mETName.getText().toString();
		if (!matches( name, VALID_USER_NAME_PATTERN )) {
			mToast.setText( R.string.invalid_user_name_prompt );
			mToast.show();
			return false;
		}
		String password = mETPassword.getText().toString();
		if (password.length() < 6 || password.length() > 16) {
			mToast.setText( R.string.invalid_user_password_prompt );
			mToast.show();
			return false;
		}
		return true;

	}

	@OnClick(R.id.btn_login)
	public void onLogin(View v) {
		//先检查该填写的是否填写
		if (!checkNameAndPassword())
			return;
		String name = mETName.getText().toString();
		String password = mETPassword.getText().toString();
		mProgressDialog.setMessage( getString( R.string.loginning_prompt ) );
		mProgressDialog.show();
		mLoginHandle = UserService.getInstance().login( name, password, new Callback<ResultBase>() {
			public void onResult(ResultBase e) {
				mProgressDialog.dismiss();
				if (e.success) {
					mAlertDialog.setMessage( getString( R.string.login_success_prompt ) );
					mAlertDialog.show();
					mHandler.sendEmptyMessageDelayed( HANDLE_FLAG_LOGIN_SUCCESS, 800 );
				} else {
					mAlertDialog.setMessage( e.msg );
					mAlertDialog.setCanceledOnTouchOutside( true );
					mAlertDialog.setCancelable( true );
					mAlertDialog.show();
				}
			}
		} );
	}

	private Toast mToast;

	private RequestHandle mLoginHandle;

	@OnClick(R.id.btn_register)
	public void onRegister(View v) {
		if (!checkNameAndPassword())
			return;
		String name = mETName.getText().toString();
		String password = mETPassword.getText().toString();
		mProgressDialog.setMessage( "正在注册..." );
		mProgressDialog.show();
		UserService.getInstance().register( name, password, new Callback<ResultBase>() {
			public void onResult(ResultBase e) {
				mProgressDialog.dismiss();
				if (e.success) {
					mAlertDialog.setMessage( "注册成功." );
					mAlertDialog.show();
				} else {
					mAlertDialog.setMessage( e.msg );
					mAlertDialog.show();
				}
			}
		} );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate( R.menu.login, menu );
		return super.onCreateOptionsMenu( menu );
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.options) {
			Intent intent=new Intent(this,OptionsSelectActivity.class);
			startActivity( intent );
			return true;
		}
		return super.onOptionsItemSelected( item );
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		ViewUtils.inject( this );
		mETName.setText( UserService.getInstance().getLastLoginUserName() );

		setUpDialog();
		mToast = Toast.makeText( this, "", Toast.LENGTH_SHORT );

		if (UserService.getInstance().canAutoLogin()) {
			mProgressDialog.setMessage( "尝试自动登录..." );
			mProgressDialog.show();
			UserService.getInstance().tryAutoLogin( new Callback<ResultBase>() {
				public void onResult(ResultBase e) {
					if (e.success) {
						mProgressDialog.setMessage( "自动登录成功..." );
						mHandler.sendEmptyMessageDelayed( HANDLE_FLAG_LOGIN_SUCCESS, 800 );
					} else {
						mProgressDialog.setMessage( "自动登录失败,请您重新登陆" );
						mHandler.sendEmptyMessageDelayed( HANDLE_FLAG_DISMISS_PROGRESS_DIALOG, 800 );
					}
				}
			} );
		}

	}

	private void setUpDialog() {
		mProgressDialog = new ProgressDialog( this );
		mProgressDialog.setTitle( "提示" );
		mProgressDialog.setMessage( "正在登陆..." );
		mProgressDialog.setCanceledOnTouchOutside( false );
		mProgressDialog.setIndeterminate( true );
		mProgressDialog.setCancelable( true );
		mProgressDialog.setOnCancelListener( new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				if (mLoginHandle != null) {
					mLoginHandle.cancel( true );
				}
			}
		} );

		mAlertDialog = new AlertDialog.Builder( this ).setTitle( "提示" ).setCancelable( true ).create();
	}
}
