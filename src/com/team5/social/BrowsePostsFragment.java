package com.team5.social;

import java.util.Map;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BrowsePostsFragment extends Fragment implements SocialFragmentInterface, TabListener, OnPageChangeListener  {
	private View myView;
	private HomeActivity myActivity;
	private SocialFragmentInterface myParent;
	private ViewPager myPager;
	private ActionBar myActionBar;
	private SocialPagerAdapter myAdapter;
	
	private ListFragment topList;
	private ListFragment newList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		if (myView == null){
			myView = inflater.inflate(R.layout.social_fragment_container, container, false);
			myActivity = (HomeActivity) getActivity();
			
			// Get pager view
			myPager = (ViewPager) myView.findViewById(R.id.social_fragment_login_pager);
			// Make an adapter for the view
			myAdapter = new SocialPagerAdapter(getFragmentManager());
			// Create tabs
			if (topList == null)	topList = new ListFragment().defineList(null, -1, 2, -1, false);
			if (newList == null)	newList = new ListFragment().defineList(null, -1, 0, -1, false);
			//Add tabs
			myAdapter.addItem(topList);
			myAdapter.addItem(newList);
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

		ActionBar.Tab tabTop = myActionBar.newTab().setText("Top").setTabListener(this);
		ActionBar.Tab tabNew = myActionBar.newTab().setText("New").setTabListener(this);

		myActionBar.addTab(tabTop, 0);
		myActionBar.addTab(tabNew, 1);
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
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction frag) {
		if (myPager != null)
			myPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}
}