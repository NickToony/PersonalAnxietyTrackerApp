package com.team5.social;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.social.NotificationFragment.ListAdapter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Fragment allowing user to reset an account password
 * 
 * All social fragments responsibility of Nick, do not modify
 * @author Nick
 *
 */
public class ResetPasswordFragment extends Fragment implements SocialFragmentInterface, NetworkInterface {
	private View myView;
	private HomeActivity myActivity;
	private SocialAccount mySocialAccount;
	private ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		
		myActivity = (HomeActivity) getActivity();
		mySocialAccount = ((Session) myActivity.getApplication()).getSocialAccount();
		
		if (myView == null)	{
			myView = inflater.inflate(R.layout.social_fragment_reset_password, container, false);
		}
		
		OnClickListener itemListener = new OnClickListener()	{
			@Override
			public void onClick(View view) {
				Request r = new Request(ResetPasswordFragment.this, "http://www.nick-hope.co.uk/PAT/android/resetpassword.php", null);
				r.addParameter("email", ((EditText)myView.findViewById(R.id.social_fragment_reset_email)).getText() + "@newcastle.ac.uk");
				r.start();
				
				progressDialog = ProgressDialog.show(myActivity, "", "Resetting password...");
			}
		};

		((Button) myView.findViewById(R.id.social_fragment_resetButton)).setOnClickListener(itemListener);
		
		return myView;
	}

	/**
	 * Handles the reply, outputting any errors
	 * 
	 * @author Nick
	 *
	 */
	@Override
	public void eventNetworkResponse(Request from, Response response) {
		// Dismiss dialog
		if (progressDialog!=null) {
            if (progressDialog.isShowing()) {
            	progressDialog.dismiss();       
            }
        }
		
		// Find all outputs
		TextView outputReset = (TextView) myView.findViewById(R.id.social_fragment_reset_error);
		TextView outputEmail = (TextView) myView.findViewById(R.id.social_fragment_reset_emailError);
		
		
		outputReset.setVisibility(View.VISIBLE);
		if (!response.isSuccess())	{
			outputReset.setText("Failure: " + response.getMessage());
			return;
		}
		
		// Store cookies
		mySocialAccount.setCookies(response.getCookies());
		
		// Get the request element
		Element eleRequest = response.getRequest();
		
		// Get script status
		int scriptStatus = Integer.parseInt(eleRequest.getElementsByTagName("status").item(0).getTextContent());
		// script failure
		if (scriptStatus != 0)	{
			String errorType = eleRequest.getElementsByTagName("error").item(0).getTextContent();
			String errorMessage = eleRequest.getElementsByTagName("message").item(0).getTextContent();
			outputReset.setText("Error: " + errorType + ": " + errorMessage);
			return;
		}
		
		// Get the request element
		Element eleData = response.getData();
		
		// Get the section elements
		TextView outputViews[] = {	outputReset, outputEmail };
		NodeList sectionList = eleData.getChildNodes();
		
		boolean success = true;
		for (int i = 0; i < sectionList.getLength(); i ++)	{
			// Get the section element
			Element sectionElement = (Element) sectionList.item(i);
			
			// Fetch the section data
			int sectionResponse = Integer.parseInt(sectionElement.getElementsByTagName("response").item(0).getTextContent());
			String sectionMessage = sectionElement.getElementsByTagName("message").item(0).getTextContent();
			
			// Output into correct position
			if (sectionResponse != 0)	{
				outputViews[i].setText(sectionElement.getNodeName() + ": " + sectionMessage);
				outputViews[i].setVisibility(View.VISIBLE);
				success = false;
			}	else	{
				outputViews[i].setText("");
				outputViews[i].setVisibility(View.GONE);
			}
		}
		
		if (success)	{
			// do something
			outputReset.setText("Password reset! Check your email.");
			
			AlertDialog alert = new AlertDialog.Builder(myActivity).create();
	        alert.setTitle("Password Reset");
	        alert.setMessage("You've successfully requested for your password to be reset. Check your email for further steps.");
	        alert.setCancelMessage(null);
	        alert.show();
		}
	}
}
