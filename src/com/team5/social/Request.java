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

import android.os.AsyncTask;
import android.util.Log;


public class Request {
	private String myUrl;
	private String myUrlParameters;
	private boolean requestDone = false;
	Response myResponse = new Response();
	
	public Request(String myUrl, String myUrlParameters)	{
		this.myUrl = myUrl;
		this.myUrlParameters = myUrlParameters;
		
		new RequestTask().execute();
	}
	
	public boolean isRequestDone()	{
		return requestDone;
	}
	
	public Response getResponse()	{
		if (requestDone)
			return myResponse;
		else
			return null;
	}
	
	private class RequestTask  extends AsyncTask<Void, Void, Void>	{
		@Override
		protected Void doInBackground(Void... params) {
			Log.i("Request", "Request.doInBackground() — making a request ");
			
			// Get a new document builder factory
			DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
			
			// Have the factory create a new document
			DocumentBuilder myDocBuilder;
			try {
				myDocBuilder = documentBuilder.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				myResponse.set(false, "Failed to configure parser.", null);
				requestDone = true;
				return null;
			}
			
			URL url;
			try {
				url = new URL(myUrl);
			} catch (MalformedURLException e2) {
				myResponse.set(false, "Malformed URL.", null);
				requestDone = true;
				return null;
			}
			HttpURLConnection connection;
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (IOException e2) {
				myResponse.set(false, "Failed to setup connection.", null);
				requestDone = true;
				return null;
			}
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			try {
				connection.setRequestMethod("POST");
			} catch (ProtocolException e1) {
				myResponse.set(false, "Failed to initiate protocols.", null);
				requestDone = true;
				return null;
			}
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(myUrlParameters.getBytes().length));
			connection.setUseCaches (false);
			
			// Attempt to get the XML and parse it
			Document doc;
			try {
				doc = myDocBuilder.parse(url.openStream());
				myResponse.set(true, "Response fetched successfully.", doc);
				requestDone = true;
				return null;
			} catch (SAXException e) {
				myResponse.set(false, "XML parsing failure.", null);
				requestDone = true;
				return null;
			} catch (IOException e) {
				myResponse.set(false, "Connection failure.", null);
				requestDone = true;
				return null;
			}
		}
	}
}

