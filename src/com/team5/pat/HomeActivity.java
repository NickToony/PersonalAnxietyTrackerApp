package com.team5.pat;

import java.io.File;
import java.util.Locale;

import com.team5.fragment.BreathExerciseFragment;
import com.team5.fragment.GraphFragment;
import com.team5.fragment.HomeFragment;
import com.team5.fragment.SeekBarFragment;
import com.team5.fragment.SocialFragment;
import com.team5.fragment.StatusFragment;
import com.team5.navigationlist.NavListAdapter;
import com.team5.pat.R;

import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HomeActivity extends Activity implements OnItemClickListener,
		SensorEventListener {
	// Constants and variables for back press and shaking censor
	private final int VIBRATE_DURATION = 200;
	private final long SHAKE_DURATION = 1000;
	private final long BACK_PRESS_DURATION = 1000;
	private final float SHAKE_THRESHOLD = 3F;
	private long previousPress = 0;
	private long previousTime = 0;

	// Components for navigation drawer list
	private DrawerLayout myDrawerLayout;
	private ListView myDrawerList;
	private ActionBarDrawerToggle myDrawerToggle;
	private ActionBar actionBar;
	private SharedPreferences preference;

	// Components for shaking the phone
	private Sensor mAccelerometer;
	private SensorManager mSensorManager;
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		((Session) getApplication()).initiate(this);
		((Session) getApplication()).getUserAccount().logIn("1234");

		preference = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		setLocale();

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.app_name);

		initialiseNavigationDrawerAndShakingSensor();
		addItemsToNavList();

		// Launch home fragment if this application is first started
		if (savedInstanceState == null) {
			changeFragment(new HomeFragment());
		}
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (myDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_settings:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;

		case R.id.action_feedback:
			Toast.makeText(getApplicationContext(), "Feedback",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_contact:
			intent = new Intent(this, ContactActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_status:
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.addToBackStack(null);
			ft.add(R.id.content_frame, new StatusFragment()).commit();
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		doNavigation((int) myDrawerList.getItemIdAtPosition(pos));
	}

	/** Show a message to confirm the user really wants to exit this application **/
	@Override
	public void onBackPressed() {
		long currentPress = System.currentTimeMillis();

		// User presses back for the first time
		if (currentPress - previousPress > BACK_PRESS_DURATION) {
			previousPress = currentPress;
			getFragmentManager().popBackStack();
			Toast.makeText(getApplicationContext(), "Press to exit",
					Toast.LENGTH_SHORT).show();
		} // Exit this application when user presses back for the second time
		else {
			super.onBackPressed();
		}
	}

	/** Sync the toggle state after onRestoreInstanceState has occurred **/
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		myDrawerToggle.syncState();
	}

	/** Pass any configuration change to the drawer toggles **/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		myDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/** React when the phone is shaken **/
	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0]; // horizontal movement
		float y = event.values[1]; // vertical movement
		float z = event.values[2]; // opaque movement
		float G = SensorManager.GRAVITY_EARTH;
		float acceleration = (x * x + y * y + z * z) / (G * G);

		if (acceleration > SHAKE_THRESHOLD) {
			long currentTime = System.currentTimeMillis();

			if ((currentTime - previousTime > SHAKE_DURATION)
					&& (isShake(x, y, z))) {
				vibrator.vibrate(VIBRATE_DURATION);
				previousTime = currentTime;
			}
		}
	}

	private void changeFragment(Fragment fragment) {
		myDrawerLayout.closeDrawer(myDrawerList);

		// Replace the frame with another fragment
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.content_frame, fragment).commit();
	}

	public void doNavigation(int theItem) {
		switch (theItem) {
		case NavListAdapter.navigationHome:
			changeFragment(new HomeFragment());
			break;
		case NavListAdapter.navigationLog:
			changeFragment(new SeekBarFragment());
			break;
		case NavListAdapter.navigationTracker:
			changeFragment(new GraphFragment());
			break;
		case NavListAdapter.navigationExercises:
			changeFragment(new BreathExerciseFragment());
			break;
		case NavListAdapter.navigationDiscussion:
			changeFragment(new SocialFragment());
			break;
		case NavListAdapter.navigationContact:
			startActivity(new Intent(this, ContactActivity.class));
			break;
		case NavListAdapter.navigationLogOff:
			finish();
			break;
		}
	}

	private void addItemsToNavList() {
		NavListAdapter adapter = (NavListAdapter) myDrawerList.getAdapter();
		/*
		 * adapter.addItem(R.drawable.ic_log_off, "Home"); // 0
		 * adapter.addItem(R.drawable.ic_log, "Log"); // 1
		 * adapter.addItem(R.drawable.ic_tracker, "Tracker"); // 2
		 * adapter.addItem(R.drawable.ic_exercises, "Exercises"); // 3
		 * adapter.addItem(R.drawable.ic_forums2, "Discussion"); // 4
		 * adapter.addItem(R.drawable.ic_my_account, "My Account"); // 5
		 * adapter.addItem(R.drawable.ic_find_help, "Find Help"); // 6
		 * adapter.addItem(R.drawable.ic_report_issue, "Report Issue"); // 7
		 * adapter.addItem(R.drawable.ic_log_off, "Log Off"); // 8
		 */
		adapter.addItem(NavListAdapter.navigationHome);
		adapter.addItem(NavListAdapter.navigationLog);
		adapter.addItem(NavListAdapter.navigationTracker);
		adapter.addItem(NavListAdapter.navigationExercises);
		adapter.addItem(NavListAdapter.navigationDiscussion);
		adapter.addItem(NavListAdapter.navigationAccount);
		adapter.addItem(NavListAdapter.navigationContact);
		adapter.addItem(NavListAdapter.navigationReport);
		adapter.addItem(NavListAdapter.navigationLogOff);
	}

	private void initialiseNavigationDrawerAndShakingSensor() {
		// Initialise drawer components
		myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		myDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Make drawer list responsive
		myDrawerList
				.setAdapter(new NavListAdapter(this, R.layout.nav_list_row));
		myDrawerList.setOnItemClickListener(this);
		myDrawerList.setBackgroundColor(Color.LTGRAY);
		myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
				R.drawable.ic_drawer, // nav drawer image to replace 'Up' caret
				R.string.drawer_open, R.string.drawer_close);
		myDrawerLayout.setDrawerListener(myDrawerToggle);

		// Inisitalise shaking censor
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	private void setLocale() {
		String language = preference.getString("pref_language", "en");
		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

	}

	/** Return true if the phone is considered shaking **/
	private boolean isShake(float x, float y, float z) {
		return Math.abs(x) > 15 && Math.abs(y) > 15 && Math.abs(z) < 10;
	}

	public void setTitle(String title) {
		actionBar.setTitle("PAT - " + title);
	}
}
