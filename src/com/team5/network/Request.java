package com.team5.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;


public class Request {
	private String myUrl;
	private List<String> myUrlParameters = new ArrayList<String>();
	private NetworkInterface myTarget;
	private Request myObject = this;
	private Map<String, String> myCookies;
	private boolean usePost = false;
	private boolean started = false;
	
	public Request(NetworkInterface myTarget, String myUrl)	{
		this.myUrl = myUrl;
		this.myTarget = myTarget;
		this.myCookies = null;
	}
	
	public Request(NetworkInterface myTarget, String myUrl, Map<String, String> myCookies)	{
		this.myUrl = myUrl;
		this.myTarget = myTarget;
		this.myCookies = myCookies;
	}
	
	public Request start()	{
		// Start the ASYNC task
		if (started == false)
			new RequestTask().execute();
		started = true;
		
		return this;
	}
	
	public Request addParameter(String key, String value)	{
		if (started == false)	{
			try {
				value = URLEncoder.encode(value, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// Already failed.
				started = true;
				// Make the response object
				Response theResponse = new Response();
				theResponse.set(false, "Failed to encode parameter: " + key, null);
				returnAnswer(theResponse);
				return this;
			}
			
			myUrlParameters.add(key + "=" + value);
		}
		return this;
	}
	
	private class RequestTask  extends AsyncTask<Void, Void, Response>	{
		@Override
		protected Response doInBackground(Void... params) {
			// Log that we're starting a request
			Log.i("Request", "Request.doInBackground() — making a request ");
			
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
			
			String theParameters = "";
			for (int i = 0; i < myUrlParameters.size(); i ++)	{
				theParameters += myUrlParameters.get(i) + "&";
			}
			
			// Handle url generation
			try {
				// Attempt to create the URL object
				if (usePost)
					theUrl = new URL(myUrl);
				else	{
					theUrl = new URL(myUrl + "?" + theParameters);
				}
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
			
			// Attach cookies
			if (myCookies != null)	{
				for (Map.Entry<String, String> cookie : myCookies.entrySet())	{
					theConnection.setRequestProperty("Cookie", cookie.getKey() + "=" + cookie.getValue());
				}
			}
			
			if (usePost)	{
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
			theConnection.setRequestProperty("Content-Length", "" + Integer.toString(theParameters.getBytes().length));
			
			// NO caching
			theConnection.setUseCaches (false);
			
			// We need to write the POST form data
			try {
				// Create a DataOutputStream to help us write to the connections output stream to the server
				theOutputStream = new DataOutputStream(theConnection.getOutputStream());
				// Write the form data
				theOutputStream.writeBytes(theParameters);
				// Push it to the output stream
				theOutputStream.flush();
				// No longer need the DataOutputStream
				theOutputStream.close();
				// Debug
				Log.i("Request", "Request.doInBackground() — wrote parameters: " + theParameters);
			} catch (IOException e1) {
				// Encoding issues?
				theResponse.set(false, "Failing to attach parameters.", null);
				theConnection.disconnect();
				return theResponse;
			}
			
			}
			
			// Attempt to get the XML and parse it
			try {
				// We take the data from the stream from the server, and try to parse it
				theDoc = myDocBuilder.parse(theConnection.getInputStream());
				theResponse.set(true, "Response fetched successfully.", theDoc);
				
				// handle cookies
				String headerName=null;
				for (int i=1; (headerName = theConnection.getHeaderFieldKey(i))!=null; i++) {
				 	if (headerName.equals("Set-Cookie")) {                  
				 		String cookie = theConnection.getHeaderField(i);
				 		cookie = cookie.substring(0, cookie.indexOf(";"));
				        String cookieName = cookie.substring(0, cookie.indexOf("="));
				        String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
				        theResponse.addCookie(cookieName, cookieValue);
				 	}
				}
				
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
			returnAnswer(response);
		}
	}
	
	private void returnAnswer(Response response)	{
		if (myTarget != null)
			myTarget.eventNetworkResponse(myObject, response);
	}
}

