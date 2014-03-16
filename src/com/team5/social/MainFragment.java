package com.team5.social;

import java.util.Map;

import com.team5.navigationlist.HomeMenuAdapter;
import com.team5.navigationlist.NavListAdapter;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MainFragment extends Fragment implements SocialFragmentInterface {
	private View myView;
	private HomeActivity myActivity;
	private SocialFragment myParent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_main, container, false);
		myActivity = (HomeActivity) getActivity();
		
		// social_fragment_mainGrid#
		
		// Setup icon grid
		GridView myIconGrid = (GridView) myView.findViewById(R.id.social_fragment_mainGrid);
		HomeMenuAdapter myAdapter = new HomeMenuAdapter(getActivity());
		myIconGrid.setAdapter(myAdapter);
		myAdapter.addItem(NavListAdapter.navigationBrowse);
		myAdapter.addItem(NavListAdapter.navigationCreate);
		myAdapter.addItem(NavListAdapter.navigationFavourites);
		myAdapter.addItem(NavListAdapter.navigationMine);
		myAdapter.addItem(NavListAdapter.navigationAccount);
		myAdapter.addItem(NavListAdapter.navigationLogOff);
		myIconGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				doNavigation((int) parent
						.getItemIdAtPosition(position));
			}
		});
		
		return myView;
	}
	
	@Override
	public void setParentFragment(SocialFragmentInterface frag) {
		this.myParent = (SocialFragment) frag;
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
	
	public void doNavigation(int nav)	{
		switch (nav)	{
		case NavListAdapter.navigationLogOff:
			myParent.eventChild(SocialFragment.EVENT_SIGN_OUT);
			break;
		case NavListAdapter.navigationBrowse:
			myParent.eventChild(SocialFragment.EVENT_GOTO_BROWSE);
			break;
		}
	}

}
