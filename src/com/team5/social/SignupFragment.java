package com.team5.social;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

public class SignupFragment extends Fragment {
	private View myView;
	private HomeActivity myActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_signup, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Discussion - Sign In");
		
		return myView;
	}
}
