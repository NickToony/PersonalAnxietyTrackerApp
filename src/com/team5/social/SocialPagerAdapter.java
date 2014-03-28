package com.team5.social;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

/** 
 * The ViewPager adapter, allows tabbed views
 * All social fragments are responsibility of Nick, do not modify
 * @author Nick
 *
 */
public class SocialPagerAdapter extends FragmentStatePagerAdapter {
	private List<SocialFragmentInterface> myFragments = new ArrayList<SocialFragmentInterface>();
	
	public SocialPagerAdapter(FragmentManager fm) {
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
	
	public void addItem(SocialFragmentInterface fragment)	{
		myFragments.add(fragment);
		this.notifyDataSetChanged();
	}
	
	public void clearFragments()	{
		myFragments.clear();
		this.notifyDataSetChanged();
	}
}