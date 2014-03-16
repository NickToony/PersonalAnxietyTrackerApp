package com.team5.social;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

public class SocialPagerAdapter extends FragmentStatePagerAdapter {
	private List<SocialFragmentInterface> myFragments = new ArrayList<SocialFragmentInterface>();
	private SocialFragmentInterface myParent;
	
	public SocialPagerAdapter(FragmentManager fm, SocialFragmentInterface parent) {
		super(fm);			
		this.myParent = parent;
	}

	@Override
	public Fragment getItem(int position) {
		return (Fragment) myFragments.get(position);
	}
	
	public SocialFragmentInterface getSocialItem(int position)	{
		return myFragments.get(position);
	}

	@Override
	public int getCount() {
		return myFragments.size();
	}
	
	public void addItem(SocialFragmentInterface fragment)	{
		fragment.setParentFragment(myParent);
		myFragments.add(fragment);
		this.notifyDataSetChanged();
	}
}