package com.team5.network;

public interface NetworkInterface	{
	public static final String PAT_SERVER = "http://nick-hope.co.uk/PAT/android/";
	
	public void eventNetworkResponse(Request from, Response response);
}