package org.xzc.msg.ui.activity;

import org.xzc.msg.R;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

import android.app.Activity;
import android.os.Bundle;

@ContentView(R.layout.activity_help)
public class HelpActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		ViewUtils.inject( this );
	}
}
