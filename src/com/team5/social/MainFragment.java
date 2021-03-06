package com.team5.social;

import java.util.Map;

import com.team5.navigationlist.HomeMenuAdapter;
import com.team5.navigationlist.NavListAdapter;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The main fragment displays a grid-layout navigation of social features
 * 
 * All social fragments responsibility of Nick, do not modify
 * @author Nick
 *
 */

public class MainFragment extends Fragment implements SocialFragmentInterface {
	private View myView;
	private HomeActivity myActivity;
	private SocialFragmentInterface myParent;
	private SocialAccount mySocialAccount;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_main, container, false);
		myActivity = (HomeActivity) getActivity();
		mySocialAccount = ((Session) myActivity.getApplication()).getSocialAccount();
		
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
	
	public void doNavigation(int nav)	{
		switch (nav)	{
		case NavListAdapter.navigationLogOff:
			mySocialAccount.handleEvent(SocialAccount.EVENT_SIGN_OUT);
			break;
		case NavListAdapter.navigationBrowse:
			mySocialAccount.handleEvent(SocialAccount.EVENT_GOTO_BROWSE);
			break;
		case NavListAdapter.navigationCreate:
			mySocialAccount.handleEvent(SocialAccount.EVENT_NEW_POST);
			break;
		case NavListAdapter.navigationAccount:
			mySocialAccount.handleEvent(SocialAccount.EVENT_GOTO_ACCOUNT);
			break;
		case NavListAdapter.navigationFavourites:
			mySocialAccount.handleEvent(SocialAccount.EVENT_GOTO_FAVOURITES);
			break;
		case NavListAdapter.navigationMine:
			mySocialAccount.handleEvent(SocialAccount.EVENT_GOTO_MINE);
			break;
		}
	}

}
