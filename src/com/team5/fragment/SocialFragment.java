package com.team5.fragment;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.social.SocialAccount;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SocialFragment extends Fragment {
	private View myView;
	private HomeActivity myActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myView = inflater
				.inflate(R.layout.activity_social, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Discussion");

		SocialAccount mySocialAccount = ((Session) (getActivity()
				.getApplication())).getSocialAccount();

		String output = "Didn't do anything";
		/*
		 * if (mySocialAccount.test()) { output = "Successful."; } else { output
		 * = "Not Successful."; }
		 */

		Toast.makeText(getActivity().getApplicationContext(), output,
				Toast.LENGTH_LONG).show();

		return myView;
	}

}
