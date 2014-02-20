package com.team5.fragment;

import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.pat.R.layout;
import com.team5.social.Request;
import com.team5.social.Response;
import com.team5.social.SocialAccount;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SocialFragment extends Fragment {	
	private Handler myHandler = new Handler();
	private Runnable myTick = new Runnable() {
		public void run() {
			step();
			myHandler.postDelayed(this, 1000); // 60 fps
		}
	};
	
	private static final int STATE_IDLE = 0;
	private static final int STATE_REQUESTING = 1;
	private static final int STATE_FATAL = 2;
	
	private final int PAGE_REQUESTING = R.layout.activity_social;
	
	private int myState = STATE_IDLE;
	private int myPage = PAGE_REQUESTING;
	private Request myRequest;
	//private SocialAccount mySocialAccount;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.activity_social, container, false);
		
		// We should store this
		//mySocialAccount = ((Session) (getActivity().getApplication())).getSocialAccount();
		
		// Begin step events
		myRequest = new Request("http://193.35.58.219/PAT/android/login.php", "");
		myState = STATE_REQUESTING;
		
		myHandler.post(myTick);
		
		return view;
	}
	
	private void step()	{
		Log.i("SocialFragment", "SocialFragment.step() — state is " + myState);
		
		if (myState == STATE_IDLE)	{
			stepIdle();
		}	else if (myState == STATE_REQUESTING)	{
			stepRequesting();
		}
	}
	
	private void stepIdle()	{
		/*if (mySocialAccount.makeRequest("http://193.35.58.219/PAT/android/login.php", ""))	{
			myState = STATE_REQUESTING;
		}	else	{
			myState = STATE_FATAL;
		}
		
		myRequest = new Request();
		myRequest.execute("http://193.35.58.219/PAT/android/login.php", "");
		myState = STATE_REQUESTING;*/
	}
	
	private void stepRequesting()	{
		if (myRequest.isRequestDone()) {
			String output = "Didn't do anything";
			Response response = myRequest.getResponse();
			if (response == null) {
				output = "Epic Failure.";
			}	else	{
				if (response.isSuccess())	{
					output = "Successful.";
				}	else	{
					output = "Not successful.";
				}
			}
			
			Toast.makeText(getActivity().getApplicationContext(), output, 
					   Toast.LENGTH_LONG).show();
			
			myState = STATE_FATAL;
		}
	}

}
