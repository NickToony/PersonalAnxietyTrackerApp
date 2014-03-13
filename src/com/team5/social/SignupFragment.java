package com.team5.social;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
	private LoginFragment myParent;
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
		this.myParent = (LoginFragment) frag;
	}
	
	@Override
	public void onClick(View theView) {
		if (theView == myButton)	{
			if (networking == false)	{
				networking = true;
				new Request(this, "http://nick-hope.co.uk/PAT/android/signup.php", "name=" + myNameView.getText() + "&email=" + myEmailView.getText() + "@Newcastle.ac.uk" + "&pass=" + myPasswordView.getText());
			}
		}
	}

	@Override
	public void eventNetworkResponse(Request from, Response response) {
		TextView text = (TextView) myView.findViewById(R.id.socialCheapOutput);
		if (!response.isSuccess())	{
			text.setText("Problem: " + response.getMessage());
		}	else	{
			String theOutput;
			
			// Make a transformer factory
			TransformerFactory theTransformerFactory = TransformerFactory.newInstance();
			Transformer theTransformer;
			// We're transforming the XML into a string, so need a string writer
			StringWriter theWriter = new StringWriter();

			
			try {
				// Try to create a transformer object
				theTransformer = theTransformerFactory.newTransformer();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
				return;
			}
			
			// Tell the transformer we're dealing with XML
			theTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			try {
				// Attempt to convert XML into String
				theTransformer.transform(new DOMSource(response.getDocument()), new StreamResult(theWriter));
			} catch (TransformerException e) {
				e.printStackTrace();
				return;
			}
			// Now make a string from the writer
			theOutput = theWriter.getBuffer().toString();
			
			// Put that string into the text view
			text.setText(theOutput);
			text.setMovementMethod(new ScrollingMovementMethod());
		}
		
		networking = false;
	}
}
