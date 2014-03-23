package com.team5.social;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class AccountFragment extends Fragment implements
		SocialFragmentInterface, NetworkInterface {
	private HomeActivity myActivity;
	private SocialAccount mySocialAccount;
	private View myView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myActivity = (HomeActivity) getActivity();
		mySocialAccount = ((Session) myActivity.getApplication())
				.getSocialAccount();

		if (myView == null) {
			myView = inflater.inflate(R.layout.social_fragment_account,
					container, false);
			
			Request r = new Request(this, "http://nick-hope.co.uk/PAT/android/account.php", mySocialAccount.getCookies());
			r.start();
		}
		return myView;
	}

	@Override
	public void eventNetworkResponse(Request from, Response response) {	
		TextView errorOutput = (TextView) myView.findViewById(R.id.social_fragment_statisticsError);
		
		if (!response.isSuccess())	{
			errorOutput.setText(response.getMessage());
			return;
		}
		
		// Save cookies
		mySocialAccount.setCookies(response.getCookies());
		
		// Check logged in
		if (!response.getLoggedIn())	{
			mySocialAccount.handleEvent(SocialAccount.EVENT_SESSION_END);
			return;
		}
		
		// Get the request element
		Element eleRequest = response.getRequest();
		
		// Get script status
		int scriptStatus = Integer.parseInt(eleRequest.getElementsByTagName("status").item(0).getTextContent());
		// script failure
		if (scriptStatus != 0)	{
			String errorType = eleRequest.getElementsByTagName("error").item(0).getTextContent();
			String errorMessage = eleRequest.getElementsByTagName("message").item(0).getTextContent();
			errorOutput.setText(errorMessage);
			return;
		}
		
		// Get the post data
		int totalPosts = Integer.parseInt(response.getData().getElementsByTagName("total_posts").item(0).getTextContent());
		int totalTopics = Integer.parseInt(response.getData().getElementsByTagName("total_topics").item(0).getTextContent());
		int totalComments = Integer.parseInt(response.getData().getElementsByTagName("total_comments").item(0).getTextContent());
		
		// Get the rating data
		float averageRating = Float.parseFloat(response.getData().getElementsByTagName("average_post_rating").item(0).getTextContent());
		float highestRating = Float.parseFloat(response.getData().getElementsByTagName("highest_post_rating").item(0).getTextContent());
		
		((TextView) myView.findViewById(R.id.social_fragment_account_totalPosts)).setText(totalPosts + "");
		((TextView) myView.findViewById(R.id.social_fragment_account_totalTopics)).setText(totalTopics + "");
		((TextView) myView.findViewById(R.id.social_fragment_account_totalComments)).setText(totalComments + "");
		
		((RatingBar) myView.findViewById(R.id.social_fragment_account_averageRating)).setRating(averageRating);
		((RatingBar) myView.findViewById(R.id.social_fragment_account_highestRating)).setRating(highestRating);
	}
}
