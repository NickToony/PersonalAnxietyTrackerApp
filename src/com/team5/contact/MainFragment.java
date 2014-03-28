package com.team5.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.social.ListFragment;
import com.team5.social.SocialFragmentInterface;
import com.team5.social.SocialPagerAdapter;

public class MainFragment extends Fragment implements TabListener, OnPageChangeListener {
	private View myView;
	private HomeActivity myActivity;
	private SocialFragmentInterface myParent;
	private ViewPager myPager;
	private ActionBar myActionBar;
	private ContactsPagerAdapter myAdapter;
	
	private ContactGroupFragment leftFragment;
	private ContactMapFragment rightFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		if (myView == null){
			myView = inflater.inflate(R.layout.contacts_main_fragment, container, false);
			myActivity = (HomeActivity) getActivity();
			
			// Get pager view
			myPager = (ViewPager) myView.findViewById(R.id.contacts_pager);
			// Make an adapter for the view
			myAdapter = new ContactsPagerAdapter(getFragmentManager());
			// Create tabs
			if (leftFragment == null)	leftFragment = new ContactGroupFragment();
			if (rightFragment == null)	rightFragment = new ContactMapFragment();
			//Add tabs
			myAdapter.addItem(leftFragment);
			myAdapter.addItem(rightFragment);
			// Set the adapter to the pager view
			myPager.setAdapter(myAdapter);
			// Set listener
			myPager.setOnPageChangeListener(this);
		}
		
		myActionBar = myActivity.getActionBar();
		myActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		myActionBar.setDisplayHomeAsUpEnabled(true);
		myActionBar.setHomeButtonEnabled(true);
		myActionBar.removeAllTabs();

		ActionBar.Tab leftTab = myActionBar.newTab().setText("Contacts").setTabListener(this);
		ActionBar.Tab rightTab = myActionBar.newTab().setText("Map").setTabListener(this);

		myActionBar.addTab(leftTab, 0);
		myActionBar.addTab(rightTab, 1);
		return myView;
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
		myActionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (myPager != null)
			myPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	class ContactsPagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> myFragments = new ArrayList<Fragment>();
		
		public ContactsPagerAdapter(FragmentManager fm) {
			super(fm);			
		}

		@Override
		public Fragment getItem(int position) {
			return (Fragment) myFragments.get(position);
		}

		@Override
		public int getCount() {
			return myFragments.size();
		}
		
		public void addItem(Fragment fragment)	{
			myFragments.add(fragment);
			this.notifyDataSetChanged();
		}
		
		public void clearFragments()	{
			myFragments.clear();
			this.notifyDataSetChanged();
		}
	}
}
