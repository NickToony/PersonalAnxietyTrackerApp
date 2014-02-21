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
	private NetworkInterface myTarget;
	private Request myObject = this;
	
	public Request(NetworkInterface myTarget, String myUrl, String myUrlParameters)	{
		this.myUrl = myUrl;
		this.myUrlParameters = myUrlParameters;
		this.myTarget = myTarget;
		
		new RequestTask().execute();
	}
	
	
	
	private class RequestTask  extends AsyncTask<Void, Void, Response>	{
		@Override
		protected Response doInBackground(Void... params) {
			Log.i("Request", "Request.doInBackground() — making a request ");
			
			Response myResponse = new Response();
			
			// Get a new document builder factory
			DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
			
			// Have the factory create a new document
			DocumentBuilder myDocBuilder;
			try {
				myDocBuilder = documentBuilder.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				myResponse.set(false, "Failed to configure parser.", null);
				return null;
			}
			
			URL url;
			try {
				url = new URL(myUrl);
			} catch (MalformedURLException e2) {
				myResponse.set(false, "Malformed URL.", null);
				return myResponse;
			}
			HttpURLConnection connection;
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (IOException e2) {
				myResponse.set(false, "Failed to setup connection.", null);
				return myResponse;
			}
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			try {
				connection.setRequestMethod("POST");
			} catch (ProtocolException e1) {
				myResponse.set(false, "Failed to initiate protocols.", null);
				return myResponse;
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
				return myResponse;
			} catch (SAXException e) {
				myResponse.set(false, "XML parsing failure.", null);
				return myResponse;
			} catch (IOException e) {
				myResponse.set(false, "Connection failure.", null);
				return myResponse;
			}
		}
		
		protected void onPostExecute(Response response)	{
			myTarget.eventNetworkResponse(myObject, response);
		}
	}
}

