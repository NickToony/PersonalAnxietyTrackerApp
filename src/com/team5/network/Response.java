package com.team5.network;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Response {
	private boolean mySuccess;
	private String myMessage;
	private Document myDocument;
	
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
}
