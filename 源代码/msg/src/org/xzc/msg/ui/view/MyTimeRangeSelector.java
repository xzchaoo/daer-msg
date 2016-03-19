package org.xzc.msg.ui.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.xzc.msg.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentManager;
import android.text.method.DateTimeKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

public class MyTimeRangeSelector extends LinearLayout {
	public interface OnDateTimeSetListener {
		public void onDateTimeSet(Date date);
	}

	private OnDateTimeSetListener mOnDateTimeSetListener;
	@ViewInject(R.id.cb_time)
	private CheckBox mCBTime;
	private FragmentManager mFragmentManager;
	@ViewInject(R.id.ll_time)
	private LinearLayout mLLTime;
	private DatePickerDialog mStartTimeDatePicker;
	private TimePickerDialog mStartTimeTimePicker;
	private DatePickerDialog mEndTimeDatePicker;
	private TimePickerDialog mEndTimeTimePicker;
	@ViewInject(R.id.tv_endTime)
	private TextView mTVEndTime;
	@ViewInject(R.id.tv_startTime)
	private TextView mTVStartTime;

	public MyTimeRangeSelector(Context context) {
		super( context, null );
	}

	public MyTimeRangeSelector(Context context, AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	private SimpleDateFormat sdf;

	public MyTimeRangeSelector(Context context, AttributeSet attrs, int defStyleAttr) {
		super( context, attrs, defStyleAttr );
		TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.MyTimeRangeSelector );
		String pattern = a.getString( R.styleable.MyTimeRangeSelector_mtrs_format );
		if (pattern == null)
			pattern = "yyyy-MM-dd HH:mm:ss";
		sdf = new SimpleDateFormat( pattern );
		a.recycle();
		initViews();
	}

	public void setSupportFragmentManager(FragmentManager fm) {
		mFragmentManager = fm;
	}

	private void initViews() {
		View.inflate( getContext(), R.layout.view_time_range_selector, this );
		ViewUtils.inject( this, this );
		//当CheckBox改变的时候对LinearLayout进行隐藏或显示
		mCBTime.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mLLTime.setVisibility( View.VISIBLE );
				} else {
					mLLTime.setVisibility( View.GONE );
				}
			}
		} );
		Calendar c = Calendar.getInstance();
		int year = c.get( Calendar.YEAR );
		int month = c.get( Calendar.MONTH );
		int day = c.get( Calendar.DAY_OF_MONTH );
		int hour = c.get( Calendar.HOUR_OF_DAY );
		int minute = c.get( Calendar.MINUTE );

		mStartTimeDatePicker = DatePickerDialog.newInstance( new OnDateSetListener() {
			public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
				mStartTimeTemp.year = year;
				//注意这里范围是0-11
				mStartTimeTemp.month = month;
				mStartTimeTemp.day = day;
				mStartTimeTimePicker.show( mFragmentManager, "start_time" );
			}
		}, year, month, day );
		mStartTimeTimePicker = TimePickerDialog.newInstance( new OnTimeSetListener() {
			public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
				mStartTimeTemp.hour = hourOfDay;
				mStartTimeTemp.minute = minute;
				mStartTime = mStartTimeTemp.toDate();
				mTVStartTime.setText( sdf.format( mStartTime ) );
			}
		}, hour, minute, true );

		mEndTimeDatePicker = DatePickerDialog.newInstance( new OnDateSetListener() {
			public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
				mEndTimeTemp.year = year;
				//注意这里范围是0-11
				mEndTimeTemp.month = month;
				mEndTimeTemp.day = day;
				mEndTimeTimePicker.show( mFragmentManager, "end_time" );
			}
		}, year, month, day );
		mEndTimeTimePicker = TimePickerDialog.newInstance( new OnTimeSetListener() {
			public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
				mEndTimeTemp.hour = hourOfDay;
				mEndTimeTemp.minute = minute;
				mEndTime = mEndTimeTemp.toDate();
				mTVEndTime.setText( sdf.format( mEndTime ) );
			}
		}, hour, minute, true );
		mTVStartTime.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				mStartTimeTemp = new Model();
				mStartTimeDatePicker.show( mFragmentManager, "start_date" );
			}
		} );

		mTVEndTime.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				mEndTimeTemp = new Model();
				mEndTimeDatePicker.show( mFragmentManager, "end_date" );
			}
		} );
	}

	public Date getStartTime() {
		return mStartTime;
	}

	public Date getEndTime() {
		return mEndTime;
	}

	private static class Model {
		public int year;
		public int month;
		public int day;
		public int hour;
		public int minute;

		public Date toDate() {
			Calendar c = Calendar.getInstance();
			c.set( Calendar.YEAR, year );
			c.set( Calendar.MONTH, month );
			c.set( Calendar.DAY_OF_MONTH, day );
			c.set( Calendar.HOUR_OF_DAY, hour );
			c.set( Calendar.MINUTE, minute );
			c.set( Calendar.SECOND, 0 );
			return c.getTime();
		}
	}

	private Date mStartTime;
	private Model mStartTimeTemp;
	private Model mEndTimeTemp;
	private Date mEndTime;
}
