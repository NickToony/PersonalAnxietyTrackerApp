package com.team5.pat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Handles the preferences and settings
 * @author Milton
 *
 */
public class SettingsActivity extends Activity implements
		OnSharedPreferenceChangeListener {
	private SharedPreferences preference;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		showSettingsFragment();

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getResources().getString(
				R.string.title_activity_settings));

		preference = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		preference.registerOnSharedPreferenceChangeListener(this);
	}

	private void showSettingsFragment() {
		SettingsFragment settingsFragment = new SettingsFragment();
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(android.R.id.content, settingsFragment).commit();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		editor = sharedPreferences.edit();
		if (key.equals("pref_key_update")) {
			boolean choice = preference.getBoolean("pref_key_update", false);
			editor.putBoolean("pref_key_update", choice).commit();
		} else if (key.equals("pref_key_pin")) {
			String pin = preference.getString("pref_key_pin", "1234");
			editor.putString("pref_key_pin", pin).commit();
		} else if (key.equals("pref_language")) {
			// Set the language selected by user
			String choice = preference.getString("pref_language", "en");
			editor.putString("pref_language", choice).commit();
		} else if (key.equals("pref_breath_exercise")) {
			// Set the breath exercise timing type
			String countDownChoice = preference.getString(
					"pref_breath_exercise", "seconds");
			editor.putString("pref_breath_exercise", countDownChoice);
		}
	}
}

class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}
}
