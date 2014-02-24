
package com.team5.fragment;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.team5.pat.R;
import com.team5.social.Request;
import com.team5.social.Response;
import com.team5.social.NetworkInterface;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SocialFragment extends Fragment implements NetworkInterface {	
	// WORK IN PROGRESS - Nick
	// private static final int STATE_IDLE = 0;
	// private static final int STATE_REQUESTING = 1;
	// private static final int STATE_FATAL = 2;
	
	// private final int LAYOUT_REQUESTING = R.layout.activity_social;
	
	// private int myState = STATE_IDLE;
	// private Request myRequest; // If you're dealing with multiple requests, you can store its object to keep track
	private View myView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.activity_social, container, false);
		
		// Make a request
		new Request(this, "http://193.35.58.219/PAT/android/login.php", "email=Nick&pass=Pass");
		
		return myView;
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
		TextView myText = (TextView) myView.findViewById(R.id.output);
		
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
		myText.setText(theOutput);
	}

}
