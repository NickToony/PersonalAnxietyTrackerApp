package com.team5.social;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Button;

public class AccountFragment extends Fragment implements
		SocialFragmentInterface, NetworkInterface {
	private HomeActivity myActivity;
	private SocialAccount mySocialAccount;
	private View myView;
	
	private final int NETWORK_STATISTICS = 0;
	private final int NETWORK_CHANGE_USERNAME = 1;
	private final int NETWORK_CHANGE_PASSWORD = 2;
	private final int NETWORK_DELETE_ACCOUNT = 3;
	
	private EditText usernameField;
	private EditText passwordField;
	
	private ProgressDialog progressDialog;

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
			
			// Start the fetching of account data
			new Request(this, "account.php", mySocialAccount.getCookies()).setIdentifier(NETWORK_STATISTICS).start();
		}
		
		usernameField = (EditText) myView.findViewById(R.id.social_fragment_account_changeUsernameField);
		passwordField = (EditText) myView.findViewById(R.id.social_fragment_account_changePasswordField);
		
		((Button) myView.findViewById(R.id.social_fragment_account_changeUsernameButton)).setOnClickListener(new OnClickListener()	{
			@Override
			public void onClick(View buttonView) {
				Request r = new Request(AccountFragment.this, "changename.php", mySocialAccount.getCookies());
				r.setIdentifier(NETWORK_CHANGE_USERNAME);
				r.addParameter("name", usernameField.getText().toString());
				r.start();
				
				progressDialog = ProgressDialog.show(myActivity, "", "Changing Username...");
			}
		});
		
		((Button) myView.findViewById(R.id.social_fragment_account_changePasswordButton)).setOnClickListener(new OnClickListener()	{
			@Override
			public void onClick(View buttonView) {
				Request r = new Request(AccountFragment.this, "changepass.php", mySocialAccount.getCookies());
				r.setIdentifier(NETWORK_CHANGE_PASSWORD);
				r.addParameter("pass", passwordField.getText().toString());
				r.start();
				
				progressDialog = ProgressDialog.show(myActivity, "", "Changing Password...");
			}
		});
		
		((Button) myView.findViewById(R.id.social_fragment_account_deleteAccountButton)).setOnClickListener(new OnClickListener()	{
			@Override
			public void onClick(View buttonView) {
				Request r = new Request(AccountFragment.this, "deleteaccount.php", mySocialAccount.getCookies());
				r.setIdentifier(NETWORK_DELETE_ACCOUNT);
				r.start();
				
				progressDialog = ProgressDialog.show(myActivity, "", "Deleting Account...");
			}
		});
		
		return myView;
	}

	@Override
	public void eventNetworkResponse(Request request, Response response) {	
		if (progressDialog!=null) {
            if (progressDialog.isShowing()) {
            	progressDialog.dismiss();       
            }
        }
		
		TextView errorOutput;
		switch (request.getIdentifier())	{
		case NETWORK_STATISTICS:
			errorOutput = (TextView) myView.findViewById(R.id.social_fragment_statisticsError);
			break;
			
		case NETWORK_CHANGE_USERNAME:
			errorOutput = (TextView) myView.findViewById(R.id.social_fragment_account_changeUsernameError);
			break;
			
		case NETWORK_CHANGE_PASSWORD:
			errorOutput = (TextView) myView.findViewById(R.id.social_fragment_account_changePasswordError);
			break;
			
		case NETWORK_DELETE_ACCOUNT:
			errorOutput = (TextView) myView.findViewById(R.id.social_fragment_account_deleteAccountError);
			break;
			
		default:
			errorOutput = (TextView) myView.findViewById(R.id.social_fragment_statisticsError);
			break;
		}
		
		if (!response.isSuccess())	{
			errorOutput.setText(response.getMessage());
			return;
		}
		
		// Save cookies
		mySocialAccount.setCookies(response.getCookies());
		
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
		
		// Check logged in
		if (!response.getLoggedIn())	{
			mySocialAccount.handleEvent(SocialAccount.EVENT_SESSION_END);
			return;
		}
		
		switch (request.getIdentifier())	{
		case NETWORK_STATISTICS:
			networkStatistics(request, response);
			break;
			
		case NETWORK_CHANGE_USERNAME:
			networkChangeUsername(request, response);
			break;
			
		case NETWORK_CHANGE_PASSWORD:
			networkChangePassword(request, response);
			break;
			
		case NETWORK_DELETE_ACCOUNT:
			networkDeleteAccount(request, response);
			break;
			
		default:
			errorOutput.setText("Miscommunication with the server");
			break;
		}
	}
	
	private void networkDeleteAccount(Request request, Response response) {		
		TextView outputChange = (TextView) myView.findViewById(R.id.social_fragment_account_deleteAccountError);
		
		outputChange.setText("Account Deletion Requested!");
		
		AlertDialog alert = new AlertDialog.Builder(myActivity).create();
        alert.setTitle("Account Deletion");
        alert.setMessage("You've successfully requested for your account to be deleted. Please check your email for next steps.");
        alert.setCancelMessage(null);
        alert.show();
	}

	private void networkStatistics(Request request, Response response)	{
		TextView errorOutput = (TextView) myView.findViewById(R.id.social_fragment_statisticsError);
		
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
	
	private void networkChangeUsername(Request request, Response response)	{				
		// Get the request element
		Element eleData = response.getData();
		
		TextView outputChange = (TextView) myView.findViewById(R.id.social_fragment_account_changeUsernameError);
		outputChange.setText("");
		
		// Get the section elements
		NodeList sectionList = eleData.getChildNodes();
		
		for (int i = sectionList.getLength() - 1; i >= 0; i --)	{
			// Get the section element
			Element sectionElement = (Element) sectionList.item(i);
			
			// Fetch the section data
			int sectionResponse = Integer.parseInt(sectionElement.getElementsByTagName("response").item(0).getTextContent());
			String sectionMessage = sectionElement.getElementsByTagName("message").item(0).getTextContent();
			
			// Output into correct position
			if (sectionResponse != 0)	{
				outputChange.setText(sectionMessage);
				return;
			}
		}
		
		outputChange.setText("Username successfully changed!");
	}
	
	private void networkChangePassword(Request request, Response response)	{				
		// Get the request element
		Element eleData = response.getData();
		
		TextView outputChange = (TextView) myView.findViewById(R.id.social_fragment_account_changePasswordError);
		outputChange.setText("");
		
		// Get the section elements
		NodeList sectionList = eleData.getChildNodes();
		
		for (int i = sectionList.getLength() - 1; i >= 0; i --)	{
			// Get the section element
			Element sectionElement = (Element) sectionList.item(i);
			
			// Fetch the section data
			int sectionResponse = Integer.parseInt(sectionElement.getElementsByTagName("response").item(0).getTextContent());
			String sectionMessage = sectionElement.getElementsByTagName("message").item(0).getTextContent();
			
			// Output into correct position
			if (sectionResponse != 0)	{
				outputChange.setText(sectionMessage);
				return;
			}
		}
		
		outputChange.setText("Password successfully changed!");
	}
}
