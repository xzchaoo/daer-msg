package org.xzc.msg.ui.activity;

import org.xzc.msg.R;
import org.xzc.msg.app.MsgApplication;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;

public class PreferencesSettingActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private SharedPreferences sp;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		getActionBar().setDisplayHomeAsUpEnabled( true );
		addPreferencesFromResource( R.xml.preferences );
		sp = PreferenceManager.getDefaultSharedPreferences( this );
		init();
	}

	private static String[] INIT_KEYS = { "retry", "timeout" };

	private void init() {
		for (String key : INIT_KEYS) {
			Preference p = findPreference( key );
			if (p instanceof ListPreference) {
				ListPreference lp = (ListPreference) p;
				lp.setSummary( lp.getEntry() );
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		sp.unregisterOnSharedPreferenceChangeListener( this );
	}

	@Override
	protected void onResume() {
		super.onResume();
		sp.registerOnSharedPreferenceChangeListener( this );
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference p = findPreference( key );
		if (p instanceof ListPreference) {
			ListPreference lp = (ListPreference) p;
			p.setSummary( lp.getEntry() );
		}
	}

	@Override
	protected void onDestroy() {
		//重新获取配置
		MsgApplication.app.config();
		super.onDestroy();
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		if (preference instanceof PreferenceScreen) {
			final PreferenceScreen ps = (PreferenceScreen) preference;
			ps.getDialog().getActionBar().setDisplayHomeAsUpEnabled( true );
			ps.getDialog().findViewById( android.R.id.home ).setOnClickListener( new OnClickListener() {
				public void onClick(View v) {
					ps.getDialog().dismiss();
				}
			} );
		}
		return false;
	}
}
