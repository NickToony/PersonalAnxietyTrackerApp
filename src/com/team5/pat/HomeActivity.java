package com.team5.pat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.team5.fragment.GraphFragment;
import com.team5.fragment.MenuFragment;
import com.team5.fragment.MenuHexagonFragment;
import com.team5.fragment.RecordGraphFragment;
import com.team5.fragment.SeekBarFragment;
import com.team5.fragment.SocialFragment;
import com.team5.navigationlist.NavListAdapter;
import com.team5.navigationlist.NavListItem;
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
import android.content.res.TypedArray;
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
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private SharedPreferences preference;

	// private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mTitles;

	private List<NavListItem> items;
	private ActionBar actionBar;

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

		initialiseDrawerComponents();
		addItemsToNavList();
		furtherProcess();
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
		if (mDrawerToggle.onOptionsItemSelected(item)) {
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
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Fragment fragment = null;
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Bundle args = new Bundle();
		args.putInt(getResources().getString(R.string.string_choice), pos);

		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		mTitle = getResources().getStringArray(R.array.nav_list_titles)[pos];
		// Create the respective fragment used to replace the current one
		switch (pos) {
		case 0:
			fragment = new GraphFragment();
			break;
		case 1:
			fragment = new GraphFragment();
			break;
		case 2:
			fragment = new GraphFragment();
			break;
		case 3:
			fragment = new MenuFragment();
			break;
		case 4:
			fragment = new MenuHexagonFragment();
			break;
		case 5:
			fragment = new SeekBarFragment();
			break;
		case 6:
			fragment = new RecordGraphFragment();
			break;
		case 7:
			fragment = new SocialFragment();
			break;
		default:
			break;
		}

		// Replace the frame with another fragment
		if ((pos >= 0) && (pos <= 7)) {
			actionBar.setTitle(mTitle);
			transaction.replace(R.id.content_frame, fragment).commit();
			fragment.setArguments(args);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	/** Sync the toggle state after onRestoreInstanceState has occurred **/
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/** Pass any configuration change to the drawer toggles **/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void initialiseDrawerComponents() {
		// mTitle = mDrawerTitle = getTitle();
		mTitles = getResources().getStringArray(R.array.nav_list_titles);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
	}

	private void addItemsToNavList() {
		items = new ArrayList<NavListItem>();
		TypedArray icons = getResources().obtainTypedArray(
				R.array.nav_list_icons);

		int size = mTitles.length;
		for (int i = 0; i < size; i++) {
			items.add(new NavListItem(mTitles[i], icons.getDrawable(i)));
		}
		icons.recycle();
	}

	private void furtherProcess() {
		// Make drawer list responsive
		mDrawerList.setAdapter(new NavListAdapter(this, R.layout.sub_menu_row,
				items));
		mDrawerList.setOnItemClickListener(this);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav drawer image to replace 'Up' caret
				R.string.drawer_open, R.string.drawer_close);
		// public void onDrawerClosed(View view) {
		// actionBar.setTitle("Open");
		// }
		//
		// public void onDrawerOpened(View drawerView) {
		// actionBar.setTitle("Close");
		// }
		mDrawerLayout.setDrawerListener(mDrawerToggle);
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
}
