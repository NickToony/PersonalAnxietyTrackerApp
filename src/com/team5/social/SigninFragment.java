package com.team5.social;

import java.util.Map;
import java.util.prefs.Preferences;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

public class SigninFragment extends Fragment implements SocialFragmentInterface, OnClickListener, NetworkInterface {
	private View myView;
	private HomeActivity myActivity;
	private SocialFragmentInterface myParent;
	private View myButton;
	private EditText myEmailView;
	private EditText myPasswordView;
	private boolean networking = false;
	private SocialAccount mySocialAccount;
	private ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_signin, container, false);
		myActivity = (HomeActivity) getActivity();
		mySocialAccount = ((Session) myActivity.getApplication()).getSocialAccount();
		
		myButton = myView.findViewById(R.id.social_fragment_signin_button);
		myButton.setOnClickListener(this);
		
		myEmailView = (EditText) myView.findViewById(R.id.social_fragment_signin_Email);
		myPasswordView = (EditText) myView.findViewById(R.id.social_fragment_signin_Password);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
		String emailRemembered = preferences.getString("prefer_key_discussions_email", "");
		if (emailRemembered.contentEquals(""))	{
			((CheckBox) myView.findViewById(R.id.social_fragment_signin_checkbox)).setChecked(false);
		}	else	{
			((EditText) myView.findViewById(R.id.social_fragment_signin_Email)).setText(emailRemembered);
			((CheckBox) myView.findViewById(R.id.social_fragment_signin_checkbox)).setChecked(true);
		}
		
		
		((Button) myView.findViewById(R.id.social_fragment_forgotten_password_button)).setOnClickListener(new OnClickListener()	{
			@Override
			public void onClick(View v) {
				mySocialAccount.handleEvent(SocialAccount.EVENT_GOTO_PASSWORD_RESET);
			}
		}	);
		
		return myView;
	}

	@Override
	public void onClick(View theView) {
		if (theView == myButton)	{
			if (networking == false)	{
				networking = true;
				progressDialog = ProgressDialog.show(myActivity, "", "Signing In...");
				
				Request r = new Request(this, "login.php", mySocialAccount.getCookies());
				r.addParameter("email", myEmailView.getText() + "@newcastle.ac.uk");
				r.addParameter("pass", myPasswordView.getText().toString());
				r.start();
				
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(myEmailView.getWindowToken(), 0);
				((TextView) myView.findViewById(R.id.social_fragment_signin_error)).setText("Signing in ... please wait");
				
				if (((CheckBox) myView.findViewById(R.id.social_fragment_signin_checkbox)).isChecked())	{
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(myActivity.getApplicationContext());
					preferences.edit().putString("prefer_key_discussions_email", myEmailView.getText() + "").apply();
				}
			}
		}
	}

	@Override
	public void eventNetworkResponse(Request from, Response response) {
		// Get rid of dialog
		if (progressDialog!=null) {
            if (progressDialog.isShowing()) {
            	progressDialog.dismiss();       
            }
        }
		
		// Find all outputs
		TextView outputSignin = (TextView) myView.findViewById(R.id.social_fragment_signin_error);
		TextView outputEmail = (TextView) myView.findViewById(R.id.social_fragment_signin_errorEmail);
		TextView outputPassword = (TextView) myView.findViewById(R.id.social_fragment_signin_errorPassword);
		
		
		if (!response.isSuccess())	{
			outputSignin.setText("Failure: " + response.getMessage());
			networking = false;
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
			outputSignin.setText("Error: " + errorType + ": " + errorMessage);
			networking = false;
			return;
		}
		
		// Get the request element
		Element eleData = response.getData();
		
		// Get the section elements
		TextView outputViews[] = {	outputSignin, outputPassword, outputEmail };
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
				success = false;
			}	else	{
				outputViews[i].setText("");
			}
		}
		
		if (success)	{
			// do something
			outputSignin.setText("Logged in!");
			mySocialAccount.handleEvent(SocialAccount.EVENT_SIGN_IN);
		}
				
		networking = false;
	}
}
