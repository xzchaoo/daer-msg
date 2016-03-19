package org.xzc.msg.ui.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.xzc.msg.R;
import org.xzc.msg.domain.SimpleMessageForPublish;
import org.xzc.msg.event.MessagePublishResultEvent;
import org.xzc.msg.service.MessageService;
import org.xzc.msg.service.UserService;
import org.xzc.msg.utils.ActivityUtils;
import org.xzc.msg.utils.Callback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.Mode;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

/**
 * 发布消息用的Activity
 * @author xzchaoo
 *
 */
@ContentView(R.layout.activity_publish_message)
public class PublishMessageActivity extends FragmentActivity implements OnCheckedChangeListener, OnClickListener,
		OnTimeSetListener, OnDateSetListener, ValidationListener {
	public static final String TAG = "PublishMessageActivity";

	public static class TimeModel {
		public int day;
		public int hour;
		public int minute;
		public int month;
		public int year;

		public Date toDate() {
			Calendar c = Calendar.getInstance();
			c.set( Calendar.YEAR, year );
			c.set( Calendar.MONTH, month );
			c.set( Calendar.DAY_OF_MONTH, day );
			c.set( Calendar.HOUR, hour );
			c.set( Calendar.MINUTE, minute );
			return c.getTime();
		}

		@Override
		public String toString() {
			return String.format( "%02d-%02d-%02d %02d:%02d:00", year, month, day, hour, minute );
		}
	}

	public static final String END_TIME_DATEPICKER_TAG = "END_TIME_DATEPICKER_TAG";

	public static final String END_TIME_TIMEPICKER_TAG = "END_TIME_TIMEPICKER_TAG";
	public static final String START_TIME_DATEPICKER_TAG = "START_TIME_DATEPICKER_TAG";

	public static final String START_TIME_TIMEPICKER_TAG = "START_TIME_TIMEPICKER_TAG";

	private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	@ViewInject(R.id.cb_endTime)
	private CheckBox mCBEndTime;

	@ViewInject(R.id.cb_location)
	private CheckBox mCBLocation;

	@ViewInject(R.id.cb_startTime)
	private CheckBox mCBStartTime;

	@ViewInject(R.id.tv_group)
	private TextView mTVGroup;

	private DatePickerDialog mEndTimeDatePickerDialog;
	private TimeModel mEndTimeModel;

	private TimeModel mEndTimeModelTemp;
	private TimePickerDialog mEndTimeTimePickerDialog;

	@Length(min = 5, message = "内容长度必须大于等于5")
	@NotEmpty(trim = true, message = "内容必须填写")
	@ViewInject(R.id.et_content)
	private EditText mETContent;

	@ViewInject(R.id.et_location)
	private EditText mETLocation;

	@Length(min = 3, message = "标题长度必须大于等于3")
	@NotEmpty(message = "标题必须填写")
	@ViewInject(R.id.et_name)
	private EditText mETName;

	/**
	 * 当前是否处于选择起始时间的模式
	 * 因为在timeset里面没法判断...
	 */
	private boolean mSelectStartTimeMode = true;

	private DatePickerDialog mStartTimeDatePickerDialog;

	private TimeModel mStartTimeModel;

	private TimeModel mStartTimeModelTemp;

	private TimePickerDialog mStartTimeTimePickerDialog;

	@ViewInject(R.id.tv_endTime)
	private TextView mTVEndTime;

	@ViewInject(R.id.tv_startTime)
	private TextView mTVStartTime;

	private Validator mValidator;

	@ViewInject(R.id.cb_group)
	private CheckBox mCBGroup;

	private int mGroupId;

	/**
	 * 当按钮变化的时候 设置相应的组件的可用性
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (mCBStartTime == buttonView) {
			mTVStartTime.setEnabled( isChecked );
		} else if (mCBEndTime == buttonView) {
			mTVEndTime.setEnabled( isChecked );
		} else if (mCBLocation == buttonView) {
			mETLocation.setEnabled( isChecked );
			mETLocation.clearFocus();
		} else if (mCBGroup == buttonView) {
			mBTNSelectGroup.setEnabled( isChecked );
		}
	}

	@ViewInject(R.id.btn_select_group)
	private Button mBTNSelectGroup;

	/**
	 * 启动一个activity选择一个组
	 * @param v
	 */
	@OnClick(R.id.btn_select_group)
	public void onSelectGroup(View v) {
		Intent intent = new Intent( this, GroupListActivity.class );
		intent.setAction( GroupListActivity.ACTION_PICK );
		int creatorId = UserService.getInstance().getCurrentUserLocally().getId();
		intent.putExtra( GroupListActivity.EXTRA_CREATOR_ID, creatorId );
		startActivityForResult( intent, REQUEST_CODE_PICK_GROUP );
	}

	public static final int REQUEST_CODE_PICK_GROUP = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PICK_GROUP) {
			if (resultCode == RESULT_OK) {
				mGroupId = data.getIntExtra( "groupId", 0 );
				String groupName = data.getStringExtra( "groupName" );
				mTVGroup.setText( groupName );
				//Toast.makeText( this, "你选择了 groupId=" + groupId + " groupName=" + groupName, Toast.LENGTH_SHORT )
				//		.show();
			}
		} else {
			super.onActivityResult( requestCode, resultCode, data );
		}
	}

	/**
	 * 响应时间的选择
	 */
	@Override
	public void onClick(View v) {
		if (mTVStartTime == v) {
			mStartTimeDatePickerDialog.show( getSupportFragmentManager(), START_TIME_DATEPICKER_TAG );
		} else {
			mEndTimeDatePickerDialog.show( getSupportFragmentManager(), END_TIME_DATEPICKER_TAG );
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		ViewUtils.inject( this );

		//建立验证
		setUpCustomValidation();

		//各种事件
		mCBLocation.setOnCheckedChangeListener( this );
		mCBStartTime.setOnCheckedChangeListener( this );
		mCBEndTime.setOnCheckedChangeListener( this );
		mCBGroup.setOnCheckedChangeListener( this );
		mTVStartTime.setOnClickListener( this );
		mTVEndTime.setOnClickListener( this );

		//建立 datetimepicker
		setUpDateTimePicker( savedInstanceState );

		mProgressDialog = new ProgressDialog( this );
		mProgressDialog.setTitle( "提示" );
		mProgressDialog.setMessage( "正在提交..." );
		mProgressDialog.setCancelable( false );
		mProgressDialog.setCanceledOnTouchOutside( false );

		mToast = Toast.makeText( this, "", Toast.LENGTH_SHORT );
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mProgressDialog.dismiss();
		}
	};

	private void setUpCustomValidation() {
		mValidator = new Validator( this );
		mValidator.setValidationMode( Mode.BURST );
		mValidator.setValidationListener( this );

		//新增验证机制
		mValidator.put( mTVEndTime, new QuickRule<TextView>() {
			public boolean isValid(TextView view) {
				if (mCBEndTime.isChecked() && mCBStartTime.isChecked()) {
					Date startTime = mStartTimeModel.toDate();
					Date endTime = mEndTimeModel.toDate();
					return !startTime.after( endTime );
				}
				return true;
			}

			@Override
			public String getMessage(Context context) {
				return "结束时间不能小于起始时间";
			}

		} );

		mValidator.put( mETLocation, new QuickRule<EditText>() {
			public boolean isValid(EditText view) {
				if (!mCBLocation.isChecked())
					return true;
				return mETLocation.getText().toString().length() >= 2;
			}

			public String getMessage(Context context) {
				return "位置的长度必须大于等于2";
			}
		} );

	}

	/**
	 * 当选完date的时候 就让用户选择time
	 * 足以month是0-11
	 */
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
		if (datePickerDialog == mStartTimeDatePickerDialog) {
			mSelectStartTimeMode = true;
			mStartTimeModelTemp = new TimeModel();
			mStartTimeModelTemp.year = year;
			mStartTimeModelTemp.month = month + 1;
			mStartTimeModelTemp.day = day;
			mStartTimeTimePickerDialog.show( getSupportFragmentManager(), START_TIME_TIMEPICKER_TAG );
		} else {
			mSelectStartTimeMode = false;
			mEndTimeModelTemp = new TimeModel();
			mEndTimeModelTemp.year = year;
			mEndTimeModelTemp.month = month + 1;
			mEndTimeModelTemp.day = day;
			mEndTimeTimePickerDialog.show( getSupportFragmentManager(), END_TIME_TIMEPICKER_TAG );
		}
	}

	private Toast mToast;

	/**
	 * 进行消息的发布
	 */
	@OnClick(R.id.publish)
	public void onPublish(View v) {
		if (mCBGroup.isChecked() && mGroupId == 0) {
			mToast.setText( "必须选择一个组." );
			mToast.show();
			return;
		}
		//先验证
		mValidator.validate();
	}

	/**
	 * 选完time 之后就可以拼凑出完整的时间了
	 */
	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		if (mSelectStartTimeMode) {
			mStartTimeModelTemp.hour = hourOfDay;
			mStartTimeModelTemp.minute = minute;
			mStartTimeModel = mStartTimeModelTemp;
			mTVStartTime.setText( mStartTimeModel.toString() );
		} else {
			mEndTimeModelTemp.hour = hourOfDay;
			mEndTimeModelTemp.minute = minute;
			mEndTimeModel = mEndTimeModelTemp;
			mTVEndTime.setText( mEndTimeModel.toString() );
		}
	}

	/**
	 * 验证失败 提示一些信息
	 */
	@Override
	public void onValidationFailed(List<ValidationError> errors) {
		boolean canToast = true;
		for (ValidationError ve : errors) {
			View v = ve.getView();
			if (v instanceof EditText) {
				( (EditText) v ).setError( ve.getCollatedErrorMessage( this ) );
			} else {
				if (canToast) {
					Toast.makeText( this, ve.getCollatedErrorMessage( this ), Toast.LENGTH_SHORT ).show();
					canToast = false;
				}
			}
		}
	}

	/**
	 * 验证通过 这里进行表单的提交
	 */
	@Override
	public void onValidationSucceeded() {
		SimpleMessageForPublish m = new SimpleMessageForPublish();
		m.name = mETName.getText().toString();
		m.content = mETContent.getText().toString();
		m.groupId = mGroupId;
		if (mCBLocation.isChecked())
			m.location = mETLocation.getText().toString();
		if (mCBStartTime.isChecked())
			m.startTime = mStartTimeModel.toDate();
		if (mCBEndTime.isChecked())
			m.endTime = mEndTimeModel.toDate();
		if (mCBGroup.isChecked())
			m.groupId = mGroupId;
		MessageService.getInstance().publishMessage( m, new Callback<MessagePublishResultEvent>() {
			public void onResult(MessagePublishResultEvent e) {
				if (e.success) {
					mProgressDialog.dismiss();
					ActivityUtils.toMessageDetailActivity( PublishMessageActivity.this, e.id, e.type );
					finish();
				} else {
					mProgressDialog.setMessage( "发布失败:" + e.msg );
					mHandler.sendEmptyMessageDelayed( 1, 800 );
				}
			}
		} );

		mProgressDialog.show();
	}

	private ProgressDialog mProgressDialog;

	/**
	 * 辅助函数 创建出datepickerdialolg
	 * @param calendar
	 * @return
	 */
	private DatePickerDialog createDatePickerDialog(Calendar calendar) {
		DatePickerDialog dlg = DatePickerDialog.newInstance( this, calendar.get( Calendar.YEAR ),
				calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ), true );
		dlg.setYearRange( 2010, 2030 );
		//选择了天就关闭
		dlg.setCloseOnSingleTapDay( true );
		return dlg;
	}

	/**
	 * 辅助函数
	 * @param calendar
	 * @return
	 */
	private TimePickerDialog createTimePickerDialog(Calendar calendar) {
		TimePickerDialog dlg = TimePickerDialog.newInstance( this, calendar.get( Calendar.HOUR_OF_DAY ),
				calendar.get( Calendar.MINUTE ), true, true );
		dlg.setCloseOnSingleTapMinute( true );
		return dlg;
	}

	/**
	 * 建立起datetimepicker及其监听
	 * @param savedInstanceState
	 */
	private void setUpDateTimePicker(Bundle savedInstanceState) {
		Calendar calendar = Calendar.getInstance();
		String now = SDF.format( calendar.getTime() );
		mTVStartTime.setText( now );
		mTVEndTime.setText( now );
		mStartTimeDatePickerDialog = createDatePickerDialog( calendar );
		mStartTimeTimePickerDialog = createTimePickerDialog( calendar );

		mEndTimeDatePickerDialog = createDatePickerDialog( calendar );
		mEndTimeTimePickerDialog = createTimePickerDialog( calendar );

		mStartTimeModel = new TimeModel();
		mEndTimeModel = new TimeModel();
		mStartTimeModel.year = mEndTimeModel.year = calendar.get( Calendar.YEAR );
		mStartTimeModel.month = mEndTimeModel.month = calendar.get( Calendar.MONTH ) + 1;
		mStartTimeModel.day = mEndTimeModel.day = calendar.get( Calendar.DAY_OF_MONTH );
		mStartTimeModel.hour = mEndTimeModel.hour = calendar.get( Calendar.HOUR );
		mStartTimeModel.minute = mEndTimeModel.minute = calendar.get( Calendar.MINUTE );

		if (savedInstanceState != null) {
			DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(
					START_TIME_DATEPICKER_TAG );
			if (dpd != null) {
				dpd.setOnDateSetListener( this );
			}
			dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag( END_TIME_DATEPICKER_TAG );
			if (dpd != null) {
				dpd.setOnDateSetListener( this );
			}

			TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(
					START_TIME_TIMEPICKER_TAG );
			if (tpd != null) {
				tpd.setOnTimeSetListener( this );
			}
			tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag( END_TIME_TIMEPICKER_TAG );
			if (tpd != null) {
				tpd.setOnTimeSetListener( this );
			}
		}
	}
}
