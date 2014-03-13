package com.team5.social;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

public class SignupFragment extends Fragment implements SocialFragmentInterface, OnClickListener, NetworkInterface {
	private View myView;
	private HomeActivity myActivity;
	private LoginFragment myParent;
	private View myButton;
	private boolean networking = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_signup, container, false);
		myActivity = (HomeActivity) getActivity();
		
		myButton = myView.findViewById(R.id.social_fragment_signup_button);
		myButton.setOnClickListener(this);
		
		return myView;
	}
	
	@Override
	public void setParentFragment(SocialFragmentInterface frag) {
		this.myParent = (LoginFragment) frag;
	}
	
	@Override
	public void onClick(View theView) {
		if (theView == myButton)	{
			if (networking == false)	{
				networking = true;
				new Request(this, "http://nick-hope.co.uk/PAT/android/signup.php", "");
			}
		}
	}

	@Override
	public void eventNetworkResponse(Request from, Response response) {
		TextView text = (TextView) myView.findViewById(R.id.socialCheapOutput);
		if (response.isSuccess())	{
			text.setText(response.getDocument().getTextContent());
		}	else	{
			text.setText("Failure: " + response.getMessage());
		}
		
	}
}
