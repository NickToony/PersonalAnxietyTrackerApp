package com.team5.social;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;


public class Request {
	private String myUrl;
	private String myUrlParameters;
	private NetworkInterface myTarget;
	private Request myObject = this;
	
	public Request(NetworkInterface myTarget, String myUrl, String myUrlParameters)	{
		this.myUrl = myUrl;
		this.myUrlParameters = myUrlParameters;
		this.myTarget = myTarget;
		
		// Start the ASYNC task
		new RequestTask().execute();
	}
	
	private class RequestTask  extends AsyncTask<Void, Void, Response>	{
		@Override
		protected Response doInBackground(Void... params) {
			// Log that we're starting a request
			Log.i("Request", "Request.doInBackground() � making a request ");
			
			// Make the response object
			Response theResponse = new Response();
			// And other objects we'll need
			URL theUrl;
			HttpURLConnection theConnection;
			DataOutputStream theOutputStream;
			Document theDoc;
			
			// Get a new document builder factory
			DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
			
			// Have the factory create a new document builder
			DocumentBuilder myDocBuilder;
			try {
				myDocBuilder = documentBuilder.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// Failure
				theResponse.set(false, "Failed to configure parser.", null);
				return null;
			}
			
			// Handle url generation
			try {
				// Attempt to create the URL object
				theUrl = new URL(myUrl);
			} catch (MalformedURLException e2) {
				// Failure
				theResponse.set(false, "Malformed URL.", null);
				return theResponse;
			}
			
			
			// Handle HTTP connection
			try {
				// Attempt to connect to the web server
				theConnection = (HttpURLConnection) theUrl.openConnection();
			} catch (IOException e2) {
				// No connection
				theResponse.set(false, "Failed to setup connection.", null);
				return theResponse;
			}
			// We want to output data to the server (POST)
			theConnection.setDoOutput(true);
			// We want to take data back from the server (the response)
			theConnection.setDoInput(true);
			// We refuse any redirects
			theConnection.setInstanceFollowRedirects(false);
			
			
			try {
				// Attempt to set form data mode to POST, rather than GET
					// NOTE: Doing this triggers it to connect to the server (hence no .connect() further up)
				theConnection.setRequestMethod("POST");
			} catch (ProtocolException e1) {
				// Couldn't setup protocols, usually occurs when connection failed.
				theResponse.set(false, "Failed to initiate protocols. Server not responding?", null);
				theConnection.disconnect();
				return theResponse;
			}
			
			// Output HTTP header properties
			theConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// the POST data's format
			theConnection.setRequestProperty("charset", "utf-8"); 
			// the POST data's size
			theConnection.setRequestProperty("Content-Length", "" + Integer.toString(myUrlParameters.getBytes().length));
			
			// NO caching
			theConnection.setUseCaches (false);
			
			// We need to write the POST form data
			try {
				// Create a DataOutputStream to help us write to the connections output stream to the server
				theOutputStream = new DataOutputStream(theConnection.getOutputStream());
				// Write the form data
				theOutputStream.writeBytes(myUrlParameters);
				// Push it to the output stream
				theOutputStream.flush();
				// No longer need the DataOutputStream
				theOutputStream.close();
				// Debug
				Log.i("Request", "Request.doInBackground() � wrote parameters: " + myUrlParameters);
			} catch (IOException e1) {
				// Encoding issues?
				theResponse.set(false, "Failing to attach parameters.", null);
				theConnection.disconnect();
				return theResponse;
			}
			
			// Attempt to get the XML and parse it
			try {
				// We take the data from the stream from the server, and try to parse it
				theDoc = myDocBuilder.parse(theConnection.getInputStream());
				theResponse.set(true, "Response fetched successfully.", theDoc);
				theConnection.disconnect();
				return theResponse;
			} catch (SAXException e) {
				theResponse.set(false, "XML parsing failure.", null);
				theConnection.disconnect();
				return theResponse;
			} catch (IOException e) {
				theResponse.set(false, "Connection failure.", null);
				theConnection.disconnect();
				return theResponse;
			}
		}
		
		protected void onPostExecute(Response response)	{
			// Trigger the event on the object which made the request
			myTarget.eventNetworkResponse(myObject, response);
		}
	}
}
