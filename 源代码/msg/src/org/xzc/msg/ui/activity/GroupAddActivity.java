package org.xzc.msg.ui.activity;

import java.util.List;

import org.xzc.msg.R;
import org.xzc.msg.event.AddGroupResultEvent;
import org.xzc.msg.service.GroupService;
import org.xzc.msg.utils.ActivityUtils;
import org.xzc.msg.utils.Callback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

/**
 * 添加Group的界面
 * @author xzchaoo
 *
 */
@ContentView(R.layout.activity_group_add)
public class GroupAddActivity extends Activity implements ValidationListener {
	@ViewInject(R.id.btn_submit)
	private Button mBTNSubmit;

	@Length(min = 5, message = "描述长度必须大于等于5.")
	@NotEmpty(message = "描述必须填写")
	@ViewInject(R.id.et_desc)
	private EditText mETDesc;

	@Length(min = 3, max = 20, message = "名称长度必须介于3-20之间.")
	@NotEmpty(message = "名称必须填写")
	@ViewInject(R.id.et_name)
	private EditText mETName;

	private Validator mValidator;

	private ProgressDialog mProgressDialog;

	@OnClick(R.id.btn_submit)
	public void onSubmit(View v) {
		mValidator.validate();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		ViewUtils.inject( this );
		mValidator = new Validator( this );
		mValidator.setValidationListener( this );
	}

	@Override
	public void onValidationSucceeded() {
		mProgressDialog = ProgressDialog.show( this, "提示", "正在创建组" );
		String name = mETName.getText().toString();
		String desc = mETDesc.getText().toString();
		GroupService.getInstance().add( name, desc, new Callback<AddGroupResultEvent>() {
			public void onResult(AddGroupResultEvent e) {
				mProgressDialog.dismiss();
				if (e.success) {
					ActivityUtils.toGroupDetailActivity( GroupAddActivity.this, e.groupId );
					finish();
				} else {
					new AlertDialog.Builder( GroupAddActivity.this ).setTitle( "提示" ).setMessage( e.msg ).show();
				}
			}
		} );
	}

	@Override
	public void onValidationFailed(List<ValidationError> errors) {
		boolean canToast = true;
		for (ValidationError ve : errors) {
			View v = ve.getView();
			if (v instanceof EditText) {
				( (EditText) v ).setError( ve.getCollatedErrorMessage( this ) );
			} else if (canToast) {
				canToast = false;
				Toast.makeText( this, ve.getCollatedErrorMessage( this ), Toast.LENGTH_SHORT ).show();
			}
		}
	}

}
