package org.xzc.msg.ui.activity;

import org.xzc.msg.R;
import org.xzc.msg.ui.view.MyTimeRangeSelector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_options_select)
public class OptionsSelectActivity extends FragmentActivity {
	@ViewInject(R.id.mtrs_createTime)
	private MyTimeRangeSelector mMTRSCreateTime;
	@ViewInject(R.id.mtrs_startTime)
	private MyTimeRangeSelector mMTRSStartTime;
	@ViewInject(R.id.mtrs_endTime)
	private MyTimeRangeSelector mMTRSEndTime;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		ViewUtils.inject( this );
		mMTRSCreateTime.setSupportFragmentManager( getSupportFragmentManager() );
		mMTRSStartTime.setSupportFragmentManager( getSupportFragmentManager() );
		mMTRSEndTime.setSupportFragmentManager( getSupportFragmentManager() );
	}
}
