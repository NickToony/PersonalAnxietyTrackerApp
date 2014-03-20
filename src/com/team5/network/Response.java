package com.team5.network;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Response {
	private boolean mySuccess;
	private String myMessage;
	private Document myDocument;
	private Map<String, String> myCookies = new HashMap<String, String>();
	
	public void set(boolean success, String message, Document document)	{
		this.mySuccess = success;
		this.myMessage = message;
		this.myDocument = document;
	}

	public boolean isSuccess() {
		return mySuccess;
	}

	public void setSuccess(boolean mySuccess) {
		this.mySuccess = mySuccess;
	}

	public String getMessage() {
		return myMessage;
	}

	public void setMessage(String myMessage) {
		this.myMessage = myMessage;
	}

	public Document getDocument() {
		return myDocument;
	}

	public void setDocument(Document myDocument) {
		this.myDocument = myDocument;
	}
	
	public Element getRequest()	{
		return (Element) myDocument.getElementsByTagName("request").item(0);
	}
	
	public Element getData()	{
		return (Element) myDocument.getElementsByTagName("data").item(0);
	}

	public void addCookie(String cookieName, String cookieValue) {
		myCookies.put(cookieName, cookieValue);
	}
	
	public Map<String, String> getCookies()	{
		return myCookies;
	}
	
	public boolean getLoggedIn()	{
		int logged_in = Integer.parseInt(getRequest().getElementsByTagName("logged_in").item(0).getTextContent());
		if (logged_in == 1)
			return true;
		return false;
	}
}
