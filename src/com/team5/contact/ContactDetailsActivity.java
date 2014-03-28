package com.team5.contact;

import com.team5.pat.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

public class ContactDetailsActivity extends Activity {
	private ActionBar actionBar;
	private ViewPager viewPager;

	private TextView nameView;
	private TextView phoneView;
	private TextView emailView;
	private TextView addressView;
	private TextView postcodeView;

	private String name;
	private String phone;
	private String email;
	private String address;
	private String postcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_group_details);
		actionBar = getActionBar();

		nameView = (TextView) findViewById(R.id.contact_details_name);
		phoneView = (TextView) findViewById(R.id.contact_details_phone);
		emailView = (TextView) findViewById(R.id.contact_details_email);
		addressView = (TextView) findViewById(R.id.contact_details_address);
		postcodeView = (TextView) findViewById(R.id.contact_details_postcode);

		nameView.setText("Name: Milton");
		phoneView.setText("Mobile: 01234567891");
		emailView.setText("Email");
		addressView.setText("address");
		postcodeView.setText("postcode");

		// FragmentTransaction ft = getFragmentManager().beginTransaction();
		// ft.replace(android.R.id.content, new
		// ContactDetailsFragment()).commit();
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		actionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	private void addTabsToActionBar() {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		ActionBar.Tab tabGroup = actionBar.newTab().setText("Details")
				.setTabListener(this);
		ActionBar.Tab tabMap = actionBar.newTab().setText("Updates")
				.setTabListener(this);
		ActionBar.Tab tabFavourite = actionBar.newTab().setText("Gallery")
				.setTabListener(this);

		actionBar.addTab(tabGroup, 0);
		actionBar.addTab(tabMap, 1);
		actionBar.addTab(tabFavourite, 2);
	}
}

class ContactDetailsAdapter extends FragmentStatePagerAdapter {
	private final int NUM_OF_TABS = 2;
	
	public ContactDetailsAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new ContactDetailsFragment();
			break;
		case 1:
//			fragment = new ContactMapFragment();
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return NUM_OF_TABS;
	}

}