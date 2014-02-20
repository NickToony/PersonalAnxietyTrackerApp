package com.team5.pat;

import com.team5.contact.ContactFavouriteFragment;
import com.team5.contact.ContactGroupFragment;
import com.team5.contact.ContactMapFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class ContactActivity extends Activity implements TabListener,
		OnPageChangeListener {
	private ActionBar actionBar;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		actionBar = getActionBar();

		addTabsToActionBar();

		viewPager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = getFragmentManager();
		viewPager.setAdapter(new ContactAdapter(fm));
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		if (viewPager != null) {
			viewPager.setCurrentItem(tab.getPosition());
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		actionBar.setSelectedNavigationItem(position);
	}

	private void addTabsToActionBar() {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		ActionBar.Tab tabGroup = actionBar.newTab().setText("Groups")
				.setTabListener(this);
		ActionBar.Tab tabMap = actionBar.newTab().setText("Map")
				.setTabListener(this);
		ActionBar.Tab tabFavourite = actionBar.newTab().setText("Favourite")
				.setTabListener(this);

		actionBar.addTab(tabGroup, 0);
		actionBar.addTab(tabMap, 1);
		actionBar.addTab(tabFavourite, 2);
	}
}

class ContactAdapter extends FragmentStatePagerAdapter {

	public ContactAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new ContactGroupFragment();
			break;
		case 1:
			fragment = new ContactMapFragment();
			break;
		case 2:
			fragment = new ContactFavouriteFragment();
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}

}