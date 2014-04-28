package com.team5.pat;

import java.util.Locale;

import com.team5.contact.MainFragment;
import com.team5.fragment.BreathExerciseFragment;
import com.team5.fragment.FeedbackFragment;
import com.team5.fragment.GraphFragment;
import com.team5.fragment.HomeFragment;
import com.team5.fragment.NewsFragment;
import com.team5.fragment.SeekBarFragment;
import com.team5.navigationlist.NavListAdapter;
import com.team5.pat.R;
import com.team5.social.SocialAccount;
import com.team5.social.SocialFragmentInterface;
import com.team5.contact.ContactMapFragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Responsible for managing fragments and backstack. Also provides sessions.
 * @author Nick
 *
 */
public class HomeActivity extends Activity implements OnItemClickListener {
	private final long BACK_PRESS_DURATION = 1000;
	private long previousPress = 0;

	private DrawerLayout myDrawerLayout;
	private ListView myDrawerList;
	private ActionBarDrawerToggle myDrawerToggle;
	private ActionBar actionBar;
	private SharedPreferences preference;
	private SocialAccount mySocialAccount;
	private MenuItem myDropDown;
	private MenuItem mySocialNotifications;

	/**
	 * Sets up the activity, side menu, preferences and session.
	 * @author Nick, Milton
	 *
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		((Session) getApplication()).initiate(this);
		((Session) getApplication()).getUserAccount().logIn("1234");
		mySocialAccount = ((Session) getApplication()).getSocialAccount();
		mySocialAccount.useActivity(this);

		preference = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		setLocale();

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.app_name);

		createDrawerListAndAddListener();
		addItemsToNavList();

		// Launch home fragment if this application is first started
		if (savedInstanceState == null) {
			changeFragment(new HomeFragment());
		}
	}

	/**
	 * Prepares menu items, such as the dropdown and notifications
	 * @author Nick, Milton
	 *
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu); // Milton's
		
		
		// Below: Nick's
		myDropDown = menu.findItem( R.id.dropdown );
		myDropDown.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		mySocialNotifications = menu.findItem( R.id.notifications);
		mySocialNotifications.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		if (mySocialAccount.isNotifications())
			mySocialAccount.fixNotifications();
		
		return true;
	}
	
	/**
	 * Returns the dropdown menu item
	 * @author Nick
	 *
	 */
	public MenuItem getDropDown()	{
		return myDropDown;
	}
	
	/**
	 * Returns the notifications button menu item
	 * @author Nick
	 *
	 */
	public MenuItem getNotifications()	{
		return mySocialNotifications;
	}

	/**
	 * Triggered when a user selects from the options menu
	 * @author Milton
	 *
	 */
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
			doNavigation(NavListAdapter.navigationReport);
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	/**
	 * Upon selecting a row from the side menu, navigation is triggered
	 * @author Nick
	 *
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		doNavigation((int) myDrawerList.getItemIdAtPosition(pos));
	}

	/**
	 * Fetches the current active fragment
	 * @author Nick
	 *
	 */
	private Fragment getCurrentFragment() {
		return getFragmentManager().findFragmentById(R.id.content_frame);
	}

	/**
	 * Handles backstack and special cases.
	 * @author Nick
	 *
	 */
	@Override
	public void onBackPressed() {
		// If social fragment
		if (getCurrentFragment() instanceof SocialFragmentInterface) {
			// Perform the go back event
			((Session) getApplication()).getSocialAccount().handleEvent(
					SocialAccount.EVENT_GO_BACK);
			return;
		} else if (getCurrentFragment() instanceof HomeFragment) {
			long currentPress = System.currentTimeMillis();
			if (currentPress - previousPress > BACK_PRESS_DURATION) {
				previousPress = currentPress;
				Toast.makeText(getApplicationContext(), "Press to exit",
						Toast.LENGTH_SHORT).show();
			} // Exit this application when user presses back for the second
				// time
			else {
				doNavigation(NavListAdapter.navigationLogOff);
			}
			return;
		} else if (getCurrentFragment() instanceof NewsFragment) {
			if (	((NewsFragment) getCurrentFragment()).doBackPress()	)
				return;
		}
		
		if (getCurrentFragment() instanceof ContactMapFragment)	{
			((ContactMapFragment) getCurrentFragment()).freeMap();
		}
		if (getCurrentFragment() instanceof com.team5.contact.MainFragment)	{
			((com.team5.contact.MainFragment) getCurrentFragment()).freeMap();
		}
		
		getFragmentManager().popBackStack();
		
		// Reset action bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.removeAllTabs(); // get rid of all tabs - they're maintained
									// across fragments!!
		
		// Fix orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}

	/**
	 * Sync the side menu
	 * @author Milton
	 *
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		myDrawerToggle.syncState();
	}

	/**
	 * Pass any configuration changes to the side menu
	 * @author Milton
	 *
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		myDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Handles fragment switches. Ensures all fragments have no unexpected items left over from previous fragment.
	 * @author Nick
	 *
	 */
	public void changeFragment(Fragment fragment) {
		myDrawerLayout.closeDrawer(myDrawerList);

		// If they're the same class
		if (getCurrentFragment() != null)
			if (getCurrentFragment().getClass().equals(fragment.getClass()))
				if (!(getCurrentFragment() instanceof SocialFragmentInterface))
					return;
		if (getCurrentFragment() != null)
			if (getCurrentFragment() instanceof ContactMapFragment)	{
				((ContactMapFragment) getCurrentFragment()).freeMap();
			}
		if (getCurrentFragment() != null)
			if (getCurrentFragment() instanceof com.team5.contact.MainFragment)	{
				((com.team5.contact.MainFragment) getCurrentFragment()).freeMap();
			}
		
		// Replace the frame with another fragment
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		// transaction.setCustomAnimations(R.anim.fragment_enter,
		// R.anim.fragment_exit, R.anim.fragment_pop_enter,
		// R.anim.fragment_pop_exit);
		transaction
				.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack(null);
		transaction.replace(R.id.content_frame, fragment)
				.commitAllowingStateLoss();
		;

		// Reset action bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.removeAllTabs(); // get rid of all tabs - they're maintained
									// across fragments!!
		
		// Fix orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		
		// Get rid of any custom views here
		//mySocialAccount.showListSpinnerAdapter(false);
		if (myDropDown != null)
			myDropDown.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		if (mySocialNotifications != null)	{
			if (mySocialAccount.isNotifications())	{
				mySocialNotifications.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				mySocialAccount.updateNotifications();
			}
			else
				mySocialNotifications.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}
	}

	/**
	 * Responds to a navigation request, leading to a fragment switch.
	 * @author Nick
	 *
	 */
	public void doNavigation(int theItem) {
		switch (theItem) {
		case NavListAdapter.navigationHome:
			changeFragment(new HomeFragment());
			mySocialAccount.resetFragments();
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
			((Session) getApplication()).getSocialAccount().navigateTo();
			break;
		case NavListAdapter.navigationContact:
			// startActivity(new Intent(this, ContactActivity.class));
			changeFragment(new com.team5.contact.ContactListFragment());
			break;
		case NavListAdapter.navigationLogOff:
			finish();
			break;
		case NavListAdapter.navigationNews:
			changeFragment(new NewsFragment());
			break;
		case NavListAdapter.navigationReport:
			changeFragment(new FeedbackFragment());
			break;
		}
	}

	/**
	 * Creates the navlsit adapter (for the side menu) and populates it
	 * @author Nick
	 *
	 */
	private void addItemsToNavList() {
		NavListAdapter adapter = (NavListAdapter) myDrawerList.getAdapter();
		
		adapter.addItem(NavListAdapter.navigationHome);
		adapter.addItem(NavListAdapter.navigationLog);
		adapter.addItem(NavListAdapter.navigationTracker);
		adapter.addItem(NavListAdapter.navigationExercises);
		adapter.addItem(NavListAdapter.navigationDiscussion);
		adapter.addItem(NavListAdapter.navigationNews); 
		adapter.addItem(NavListAdapter.navigationContact);
		adapter.addItem(NavListAdapter.navigationReport);
		adapter.addItem(NavListAdapter.navigationLogOff);
	}

	/**
	 * Creates the drawer list and sets up listener
	 * Rewritten by Nick to use the new adapters and navigation system
	 * @author Nick, Milton
	 *
	 */
	private void createDrawerListAndAddListener() {
		// initialise drawer components
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
	}
	
	/**
	 * Allows the language to be changed. Handles strings.
	 * @author Milton
	 *
	 */
	private void setLocale() {
		String language = preference.getString("pref_language", "en");
		Locale locale = new Locale(language);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

	}
	
	/**
	 * Quickfix: for dealing with rotation of screen and the map
	 * 
	 * I am sorry for this dirty workaround!
	 * @author Nick
	 *
	 */
	@Override
	public void onDestroy()	{
		if (getCurrentFragment() instanceof ContactMapFragment)	{
			((ContactMapFragment) getCurrentFragment()).freeMap();
		}
		if (getCurrentFragment() instanceof com.team5.contact.MainFragment)	{
			((com.team5.contact.MainFragment) getCurrentFragment()).freeMap();
		}
		
		try	{
			Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
			if (fragment != null)	{
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.remove(fragment);
			ft.commit(); }
			}	catch (Exception e)	{
			}
		
		super.onDestroy();
	}

	/**
	 * Quickfix: for dealing with rotation of screen and the map
	 * 
	 * I am sorry for this dirty workaround!
	 * @author Nick
	 *
	 */
	@Override
	public void onPause()	{
		if (getCurrentFragment() instanceof ContactMapFragment)	{
			((ContactMapFragment) getCurrentFragment()).freeMap();
		}
		if (getCurrentFragment() instanceof com.team5.contact.MainFragment)	{
			((com.team5.contact.MainFragment) getCurrentFragment()).freeMap();
		}
		
		try	{
			Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
			if (fragment != null)	{
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.remove(fragment);
			ft.commit(); }
			}	catch (Exception e)	{
			}
		
		super.onPause();
	}
	
	/**
	 * Allows a fragment to set the activities title
	 * @author Nick
	 *
	 */
	public void setTitle(String title) {
		actionBar.setTitle(title);
	}
}
