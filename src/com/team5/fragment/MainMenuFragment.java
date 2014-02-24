package com.team5.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.team5.navigationlist.HomeMenuAdapter;
import com.team5.navigationlist.NavListAdapter;
import com.team5.pat.ContactActivity;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

public class MainMenuFragment extends Fragment {
	private View myView;
	private HomeActivity myActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.home_layout, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Home");
		
		// Setup icon grid
		GridView myIconGrid = (GridView) myView.findViewById(R.id.homeGrid);
		HomeMenuAdapter myAdapter = new HomeMenuAdapter(getActivity());
		myIconGrid.setAdapter(myAdapter);
		myAdapter.addItem(NavListAdapter.navigationLog);
		myAdapter.addItem(NavListAdapter.navigationTracker);
		myAdapter.addItem(NavListAdapter.navigationExercises);
		myAdapter.addItem(NavListAdapter.navigationDiscussion);
		myAdapter.addItem(NavListAdapter.navigationAccount);
		myAdapter.addItem(NavListAdapter.navigationContact);
		myAdapter.addItem(NavListAdapter.navigationReport);
		myAdapter.addItem(NavListAdapter.navigationLogOff);
		myIconGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	myActivity.doNavigation((int) parent.getItemIdAtPosition(position)); 
            }
        });

		return myView;
	}
}
