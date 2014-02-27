package com.team5.social;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

public class SigninFragment extends Fragment implements SocialFragmentInterface {
	private View myView;
	private HomeActivity myActivity;
	private LoginFragment myParent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_signin, container, false);
		myActivity = (HomeActivity) getActivity();
		
		View button = myView.findViewById(R.id.social_fragment_signin_button);
		button.setOnClickListener(new OnClickListener()	{
			@Override
			public void onClick(View arg0) {
				if (myParent != null)
				myParent.logIn();
			}
		}	);
		
		return myView;
	}

	@Override
	public void setParentFragment(SocialFragmentInterface frag) {
		this.myParent = (LoginFragment) frag;
	}
}
