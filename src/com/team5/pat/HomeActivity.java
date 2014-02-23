package com.team5.pat;

import java.util.Locale;

import com.team5.fragment.BreathExerciseFragment;
import com.team5.fragment.GraphFragment;
import com.team5.fragment.MainMenuFragment;
import com.team5.fragment.SeekBarFragment;
import com.team5.fragment.SocialFragment;
import com.team5.fragment.StatusFragment;
import com.team5.navigationlist.NavListAdapter;
import com.team5.pat.R;

import android.os.Bundle;
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
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HomeActivity extends Activity implements OnItemClickListener {
	private DrawerLayout myDrawerLayout;
	private ListView myDrawerList;
	private ActionBarDrawerToggle myDrawerToggle;
	private ActionBar myActionBar;

	private SharedPreferences preference;

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
		myActionBar = getActionBar();
		myActionBar.setDisplayHomeAsUpEnabled(true);
		myActionBar.setHomeButtonEnabled(true);
		myActionBar.setTitle(R.string.app_name);

		initialiseDrawerComponents();
		furtherProcess();
		addItemsToNavList();

		changeFragment(new MainMenuFragment());
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

	public void changeFragment(Fragment fragment) {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		// Get rid of any additions to action bar
		myActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		// Close drawer
		myDrawerLayout.closeDrawer(myDrawerList);
		// Replace the frame with another fragment
		transaction.replace(R.id.content_frame, fragment).commit();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		switch (pos) {
		case 0: // Home
			changeFragment(new MainMenuFragment());
			break;
		case 1:
			changeFragment(new SeekBarFragment());
			break;
		case 2: // Log
			changeFragment(new GraphFragment());
			break;
		case 3: // Exercises
			changeFragment(new BreathExerciseFragment());
			break;
		case 4: // Discussion
			changeFragment(new SocialFragment());
		case 5: // My Account
			break;
		case 6: // Find Help
			startActivity(new Intent(this, ContactActivity.class));
			break;
		case 7: // Report issues
			break;
		case 8: // Log out
			finish();
		}
	}

	private void addItemsToNavList() {
		NavListAdapter adapter = (NavListAdapter) myDrawerList.getAdapter();
		adapter.addItem(R.drawable.ic_log_off, "Home"); // 0
		adapter.addItem(R.drawable.ic_log, "Log"); // 1
		adapter.addItem(R.drawable.ic_tracker, "Tracker"); // 2
		adapter.addItem(R.drawable.ic_exercises, "Exercises"); // 3
		adapter.addItem(R.drawable.ic_forums2, "Discussion"); // 4
		adapter.addItem(R.drawable.ic_my_account, "My Account"); // 5
		adapter.addItem(R.drawable.ic_find_help, "Find Help"); // 6
		adapter.addItem(R.drawable.ic_report_issue, "Report Issue"); // 7
		adapter.addItem(R.drawable.ic_log_off, "Log Off"); // 8

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

	private void initialiseDrawerComponents() {
		myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		myDrawerList = (ListView) findViewById(R.id.drawer_list);
	}

	private void furtherProcess() {
		// Make drawer list responsive
		myDrawerList
				.setAdapter(new NavListAdapter(this, R.layout.nav_list_row));
		myDrawerList.setOnItemClickListener(this);
		myDrawerList.setBackgroundColor(Color.LTGRAY);

		myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
				R.drawable.ic_drawer, // nav drawer image to replace 'Up' caret
				R.string.drawer_open, R.string.drawer_close);
		myDrawerLayout.setDrawerListener(myDrawerToggle);
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

	public void setTitle(String title) {
		myActionBar.setTitle("PAT - " + title);
	}
}
