package com.team5.network;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The response from a Request object
 * @author Nick
 *
 */
public class Response {
	private boolean mySuccess;
	private String myMessage;
	private Document myDocument;
	private Map<String, String> myCookies = new HashMap<String, String>();
	
	/**
	 * Allows the Request to create the response with all parameters. Should not be used elsewhere.
	 * @author Nick
	 *
	 */
	public void set(boolean success, String message, Document document)	{
		this.mySuccess = success;
		this.myMessage = message;
		this.myDocument = document;
	}

	/**
	 * Determines if the request as a success.
	 * @author Nick
	 *
	 */
	public boolean isSuccess() {
		return mySuccess;
	}

	/**
	 * Define if was success
	 * @author Nick
	 *
	 */
	public void setSuccess(boolean mySuccess) {
		this.mySuccess = mySuccess;
	}

	/**
	 * If not a success, a message is attached.
	 * @author Nick
	 *
	 */
	public String getMessage() {
		return myMessage;
	}

	/**
	 * An error message can be attached.
	 * @author Nick
	 *
	 */
	public void setMessage(String myMessage) {
		this.myMessage = myMessage;
	}

	/**
	 * Fetches the XML document in the response.
	 * @author Nick
	 *
	 */
	public Document getDocument() {
		return myDocument;
	}
	
	/**
	 * Sets the XML document
	 * @author Nick
	 *
	 */
	public void setDocument(Document myDocument) {
		this.myDocument = myDocument;
	}
	
	/**
	 * Get the original request object (for identification)
	 * @author Nick
	 *
	 */
	public Element getRequest()	{
		return (Element) myDocument.getElementsByTagName("request").item(0);
	}
	
	/**
	 * Fetch Data element from XML response
	 * @author Nick
	 *
	 */
	public Element getData()	{
		return (Element) myDocument.getElementsByTagName("data").item(0);
	}

	/**
	 * Add a cookie to the response.
	 * @author Nick
	 *
	 */
	public void addCookie(String cookieName, String cookieValue) {
		myCookies.put(cookieName, cookieValue);
	}
	
	/**
	 * Fetch cookies returned by the response
	 * @author Nick
	 *
	 */
	public Map<String, String> getCookies()	{
		return myCookies;
	}
	
	/**
	 * Determine from the XML is the user is logged in or not.
	 * @author Nick
	 *
	 */
	public boolean getLoggedIn()	{
		int logged_in = Integer.parseInt(getRequest().getElementsByTagName("logged_in").item(0).getTextContent());
		if (logged_in == 1)
			return true;
		return false;
	}
}
