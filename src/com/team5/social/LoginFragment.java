package com.team5.social;

import java.util.Map;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Handles Viewpager which contains the sign in and sign up fragments
 * 
 * All social fragments responsibility of Nick, do not modify
 * @author Nick
 *
 */

public class LoginFragment extends Fragment implements SocialFragmentInterface, TabListener, OnPageChangeListener  {
	private View myView;
	private HomeActivity myActivity;
	private SocialFragmentInterface myParent;
	private ViewPager myPager;
	private SocialPagerAdapter myAdapter;
	private ActionBar myActionBar;
	private SocialAccount mySocialAccount;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		if (myView == null){
			myView = inflater.inflate(R.layout.social_fragment_container, container, false);
			myActivity = (HomeActivity) getActivity();
			mySocialAccount = ((Session) myActivity.getApplication()).getSocialAccount();
		
			// Get pager view
			myPager = (ViewPager) myView.findViewById(R.id.social_fragment_login_pager);
			// Make an adapter for the view
			myAdapter = new SocialPagerAdapter(getFragmentManager());
			//Add tabs
			myAdapter.addItem(new SigninFragment());
			myAdapter.addItem(new SignupFragment());
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

		ActionBar.Tab tabSignIn = myActionBar.newTab().setText("Sign In").setTabListener(this);
		ActionBar.Tab tabSignUp = myActionBar.newTab().setText("Sign Up").setTabListener(this);

		myActionBar.addTab(tabSignIn, 0);
		myActionBar.addTab(tabSignUp, 1);
		
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