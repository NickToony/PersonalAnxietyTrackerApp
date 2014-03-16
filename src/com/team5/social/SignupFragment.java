package com.team5.social;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Fragment;
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

public class SignupFragment extends Fragment implements SocialFragmentInterface, OnClickListener, NetworkInterface {
	private View myView;
	private HomeActivity myActivity;
	private SocialFragmentInterface myParent;
	private View myButton;
	private EditText myNameView;
	private EditText myEmailView;
	private EditText myPasswordView;
	private boolean networking = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_signup, container, false);
		myActivity = (HomeActivity) getActivity();
		
		myButton = myView.findViewById(R.id.social_fragment_signup_button);
		myButton.setOnClickListener(this);
		
		myNameView = (EditText) myView.findViewById(R.id.social_fragment_signup_Name);
		myEmailView = (EditText) myView.findViewById(R.id.social_fragment_signup_Email);
		myPasswordView = (EditText) myView.findViewById(R.id.social_fragment_signup_Password);
		
		return myView;
	}
	
	@Override
	public void setParentFragment(SocialFragmentInterface frag) {
		this.myParent = frag;
	}
	
	@Override
	public void changeFragment(SocialFragmentInterface theFrag)	{
		myParent.changeFragment(theFrag);
	}
	
	@Override
	public void onClick(View theView) {
		if (theView == myButton)	{
			if (networking == false)	{
				networking = true;
				new Request(this, "http://nick-hope.co.uk/PAT/android/signup.php", "name=" + myNameView.getText() + "&email=" + myEmailView.getText() + "@newcastle.ac.uk" + "&pass=" + myPasswordView.getText(), getCookies());
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(myEmailView.getWindowToken(), 0);
					((TextView) myView.findViewById(R.id.social_fragment_signup_error)).setText("Signing up ... please wait");
			}
		}
	}

	@Override
	public void eventNetworkResponse(Request from, Response response) {
		// Find all outputs
		TextView outputSignup = (TextView) myView.findViewById(R.id.social_fragment_signup_error);
		TextView outputName = (TextView) myView.findViewById(R.id.social_fragment_signup_errorName);
		TextView outputEmail = (TextView) myView.findViewById(R.id.social_fragment_signup_errorEmail);
		TextView outputPassword = (TextView) myView.findViewById(R.id.social_fragment_signup_errorPassword);
		
		
		if (!response.isSuccess())	{
			outputSignup.setText("Failure: " + response.getMessage());
			networking = false;
			return;
		}
		
		// Store cookies
		setCookies(response.getCookies());
		
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
				success = false;
			}	else	{
				outputViews[i].setText("");
			}
		}
		
		if (success)	{
			// do something
			outputSignup.setText("Registered! Try logging in.");
		}
				
		networking = false;
	}
	
	@Override
	public void setCookies(Map<String, String> cookieMap)	{
		myParent.setCookies(cookieMap);
	}
	@Override
	public Map<String, String> getCookies()	{
		return myParent.getCookies();
	}
	
	@Override
	public void eventChild(int eventID)	{
		myParent.eventChild(eventID);
	}
}
