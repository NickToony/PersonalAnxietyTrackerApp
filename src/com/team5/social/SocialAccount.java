package com.team5.social;

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

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class SocialAccount {
	/*private Context myContext;
	private int myStatus = STATUS_IDLE;
	
	public final static int STATUS_IDLE = 1; // safe to make a new request
	public final static int STATUS_BUSY = 2; // executing - can't do anymore
	public final static int STATUS_RESPONSE = 3; // executed - has a response
	
	private String urlString;
	private String urlParameters;
	private Response requestResponse;
	
	private Handler myHandler = new Handler();
	private Runnable myTick = new Runnable() {
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			myStatus = STATUS_RESPONSE;
			requestResponse = handleRequest();
		}
	};
	
	public SocialAccount(Context myContext) {
		// TODO Auto-generated constructor stub
		this.myContext = myContext;
	}
	
	public boolean makeRequest(String urlString, String urlParameters)	{
		// If not doing anything
		if (myStatus == STATUS_IDLE)	{
			// Take the request information
			//this.urlString = urlString;
			//this.urlParameters = urlParameters;
			// Set status to busy
			myStatus = STATUS_BUSY;
			// Make a new response object to use
			requestResponse = new Response();
			// Start the thread
			myHandler.post(myTick); // execute ASAP
			// Request started
			return true;
		}	else	{
			return false;
		}
	}
	
	public int getStatus()	{
		return myStatus;
	}
	
	public Response getResponse()	{
		if (myStatus == STATUS_RESPONSE)	{
			return requestResponse;
		}	else	{
			return null;
		}
	}
	
	private Response handleRequest() {
		// Get a new document builder factory
		DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
		
		// Have the factory create a new document
		DocumentBuilder myDocBuilder;
		try {
			myDocBuilder = documentBuilder.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			requestResponse.set(false, "Failed to configure parser.", null);
			return requestResponse;
		}
		
		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e2) {
			requestResponse.set(false, "Malformed URL.", null);
			return requestResponse;
		}
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e2) {
			requestResponse.set(false, "Failed to setup connection.", null);
			return requestResponse;
		}
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		try {
			connection.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			requestResponse.set(false, "Failed to initiate protocols.", null);
			return requestResponse;
		}
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setUseCaches (false);
		
		// Attempt to get the XML and parse it
		Document doc;
		try {
			doc = myDocBuilder.parse(url.openStream());
			requestResponse.set(true, "Response fetched successfully.", doc);
			return requestResponse;
		} catch (SAXException e) {
			requestResponse.set(false, "XML parsing failure.", null);
			return requestResponse;
		} catch (IOException e) {
			requestResponse.set(false, "Connection failure.", null);
			return requestResponse;
		}
	}*/

}