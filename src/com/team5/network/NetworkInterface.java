package com.team5.network;

/**
 * Implement this interface and its event method to be able to receive networking responses.
 * @author Nick
 */

public interface NetworkInterface	{
	public static final String PAT_SERVER = "http://nick-hope.co.uk/PAT/android/";
	
	public void eventNetworkResponse(Request from, Response response);
}