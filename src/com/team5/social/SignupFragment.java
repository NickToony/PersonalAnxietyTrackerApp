package com.team5.social;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

/**
 * Fragment allowing user to signup
 * 
 * All social fragments responsibility of Nick, do not modify
 * @author Nick
 *
 */
public class SignupFragment extends Fragment implements SocialFragmentInterface, OnClickListener, NetworkInterface {
	private View myView;
	private HomeActivity myActivity;
	private SocialAccount mySocialAccount;
	private View myButton;
	private EditText myNameView;
	private EditText myEmailView;
	private EditText myPasswordView;
	private boolean networking = false;
	private ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_signup, container, false);
		myActivity = (HomeActivity) getActivity();
		mySocialAccount = ((Session) myActivity.getApplication()).getSocialAccount();
		
		myButton = myView.findViewById(R.id.social_fragment_signup_button);
		myButton.setOnClickListener(this);
		
		myNameView = (EditText) myView.findViewById(R.id.social_fragment_signup_Name);
		myEmailView = (EditText) myView.findViewById(R.id.social_fragment_signup_Email);
		myPasswordView = (EditText) myView.findViewById(R.id.social_fragment_signup_Password);
		
		return myView;
	}
	
	/**
	 * Handles the clicking of "Sign Up"
	 * 
	 * @author Nick
	 *
	 */
	@Override
	public void onClick(View theView) {
		if (theView == myButton)	{
			if (networking == false)	{
				networking = true;
				progressDialog = ProgressDialog.show(myActivity, "", "Registering Account...");
				
				Request r = new Request(this, "signup.php", mySocialAccount.getCookies());
				r.addParameter("name", myNameView.getText().toString());
				r.addParameter("email", myEmailView.getText() + "@newcastle.ac.uk");
				r.addParameter("pass", myPasswordView.getText().toString());
				r.start();
				
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(myEmailView.getWindowToken(), 0);
					((TextView) myView.findViewById(R.id.social_fragment_signup_error)).setText("Signing up ... please wait");
			}
		}
	}

	/**
	 * Handles response, displaying any errors
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
		TextView outputSignup = (TextView) myView.findViewById(R.id.social_fragment_signup_error);
		TextView outputName = (TextView) myView.findViewById(R.id.social_fragment_signup_errorName);
		TextView outputEmail = (TextView) myView.findViewById(R.id.social_fragment_signup_errorEmail);
		TextView outputPassword = (TextView) myView.findViewById(R.id.social_fragment_signup_errorPassword);
		
		
		outputSignup.setVisibility(View.VISIBLE);
		if (!response.isSuccess())	{
			outputSignup.setText("Failure: " + response.getMessage());
			networking = false;
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
			outputSignup.setText("Error: " + errorType + ": " + errorMessage);
			networking = false;
			return;
		}
		
		// Get the request element
		Element eleData = response.getData();
		
		// Get the section elements
		TextView outputViews[] = {	outputSignup, outputName, outputPassword, outputEmail };
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
			outputSignup.setText("Registered! Check your email before logging in");
		}
				
		networking = false;
	}
}
