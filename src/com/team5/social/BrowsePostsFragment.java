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
	private SocialFragment myParent;
	private ViewPager myPager;
	private ActionBar myActionBar;
	private SocialPagerAdapter myAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_container, container, false);
		myActivity = (HomeActivity) getActivity();
		
		myActionBar = myActivity.getActionBar();
		myActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		myActionBar.setDisplayHomeAsUpEnabled(true);
		myActionBar.setHomeButtonEnabled(true);
		myActionBar.removeAllTabs();

		ActionBar.Tab tabTop = myActionBar.newTab().setText("Top").setTabListener(this);
		ActionBar.Tab tabNew = myActionBar.newTab().setText("New").setTabListener(this);

		myActionBar.addTab(tabTop, 0);
		myActionBar.addTab(tabNew, 1);
		
		// Get pager view
		myPager = (ViewPager) myView.findViewById(R.id.social_fragment_login_pager);
		// Make an adapter for the view
		myAdapter = new SocialPagerAdapter(getFragmentManager(), this);
		//Add tabs
		myAdapter.addItem(new ListFragment().defineList(-1, -1, 0, -1));
		myAdapter.addItem(new ListFragment().defineList(-1, -1, 1, -1));
		// Set the adapter to the pager view
		myPager.setAdapter(myAdapter);
		// Set listener
		myPager.setOnPageChangeListener(this);
		
		return myView;
	}

	@Override
	public void setParentFragment(SocialFragmentInterface frag) {
		this.myParent = (SocialFragment) frag;
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
	
	@Override
	public void setCookies(Map<String, String> cookieMap)	{
		myParent.setCookies(cookieMap);
	}
	@Override
	public Map<String, String> getCookies()	{
		return myParent.getCookies();
	}
	
	@Override
	public void eventChild(int eventID)	{
		myParent.eventChild(eventID);
	}
}