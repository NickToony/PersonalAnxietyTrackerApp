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
	private Context myContext;
	private int myStatus = 1;
	
	public final static int STATUS_IDLE = 1; // safe to make a new request
	public final static int STATUS_BUSY = 2; // executing - can't do anymore
	public final static int STATUS_RESPONSE = 3; // executed - has a response
	
	private String urlString;
	private String urlParameters;
	private Response requestResponse;
	
	private Handler myHandler;
	private Runnable myTick = new Runnable() {
		public void run() {
			//requestResponse = handleRequest();
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
			this.urlString = urlString;
			this.urlParameters = urlParameters;
			// Set status to busy
			myStatus = STATUS_BUSY;
			// Make a new response object to use
			requestResponse = new Response();
			// Start the thread
			myHandler.postDelayed(myTick, 1); // execute ASAP
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
	
	private Document handleRequest() {
		// Get a new document builder factory
		DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
		
		// Have the factory create a new document
		DocumentBuilder document;
		try {
			document = documentBuilder.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			return null;
		}
		
		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e2) {
			return null;
		}
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e2) {
			return null;
		}
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		try {
			connection.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			return null;
		}
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setUseCaches (false);
		
		// Attempt to get the XML and parse it
		try {
			return document.parse(new URL(urlString).openStream());
		} catch (MalformedURLException e) {
			return null;
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}