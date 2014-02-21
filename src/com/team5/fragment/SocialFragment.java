package com.team5.fragment;

import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.pat.R.layout;
import com.team5.social.Request;
import com.team5.social.Response;
import com.team5.social.NetworkInterface;

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

public class SocialFragment extends Fragment implements NetworkInterface {		
	private static final int STATE_IDLE = 0;
	private static final int STATE_REQUESTING = 1;
	private static final int STATE_FATAL = 2;
	
	private final int LAYOUT_REQUESTING = R.layout.activity_social;
	
	private int myState = STATE_IDLE;
	private Request myRequest;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.activity_social, container, false);
		
		// Begin step events
		myRequest = new Request(this, "http://193.35.58.219/PAT/android/login.php", "");
		myState = STATE_REQUESTING;
		
		return view;
	}

	@Override
	public void eventNetworkResponse(Request request, Response response) {
		String output = "Didn't do anything";
		if (response.isSuccess())	{
			output = "Successful.";
		}	else	{
			output = "Not successful.";
		}
		
		Toast.makeText(getActivity().getApplicationContext(), output, 
				   Toast.LENGTH_LONG).show();
		
		myState = STATE_FATAL;
	}

}
