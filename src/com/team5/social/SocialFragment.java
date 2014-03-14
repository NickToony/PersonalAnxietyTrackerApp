
package com.team5.social;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.team5.fragment.SeekBarFragment;
import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SocialFragment extends Fragment implements NetworkInterface, SocialFragmentInterface {	
	private View myView;
	private HomeActivity myActivity;
	private Map<String, String> myCookies = new HashMap<String, String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Discussion");
		
		// Make a request
		//new Request(this, "http://193.35.58.219/PAT/android/login.php", "email=Nick&pass=Pass");

		// Replace the frame with another fragment
		changeFragment(new LoginFragment());
		
		return myView;
	}
	
	public void changeFragment(SocialFragmentInterface theFrag)	{
		theFrag.setParentFragment(this);
		
		// Replace the frame with another fragment
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.social_fragment_frame, (Fragment) theFrag).commit();
	}

	@Override
	public void eventNetworkResponse(Request request, Response response) {
		// Default toast text
		String theOutput = "Didn't do anything";
		// if request was successful
		if (response.isSuccess())	{
			theOutput = "Successful.";
		}	else	{
			// output using the error message provided
			theOutput = "Not successful: " + response.getMessage();
		}
		
		// Make the toast with the connection message
		Toast.makeText(getActivity().getApplicationContext(), theOutput, 
				   Toast.LENGTH_LONG).show();
		
		// Find the TextView
		//TextView myText = (TextView) myView.findViewById(R.id.social_fragment_output);  -- FIX
		
		// Don't continue if connection failed
		if (!response.isSuccess())	{
			return;
		}
		
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
		//myText.setText(theOutput); -- FIX
	}

	@Override
	public void setParentFragment(SocialFragmentInterface frag) {
	}
	
	public void setCookies(Map<String, String> cookieMap)	{
		myCookies.putAll(cookieMap);
	}
	
	public Map<String, String> getCookies()	{
		return myCookies;
	}

}
