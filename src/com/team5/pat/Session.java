package com.team5.pat;

import java.util.HashMap;
import java.util.Map;

import com.team5.user.UserAccount;

import android.app.Application;
import android.content.Context;

public class Session extends Application {
	private Context myContext;
	private UserAccount myUserAccount;
	private boolean setup = false;
	private Map<String, String> myCookies = new HashMap<String, String>();
	private boolean socialLoggedIn = false;
	
	public void initiate(Context c)	{
		myContext = c;
		
		myUserAccount = new UserAccount(myContext);
		//mySocialAccount = new SocialAccount(myContext);
		
		setup = true;
	}
	
	public UserAccount getUserAccount()	{
		return myUserAccount;
	}
	
	public boolean checkSetup()	{
		return setup;
	}
	
	public void setCookies(Map<String, String> cookieMap)	{
		myCookies.putAll(cookieMap);
	}
	
	public Map<String, String> getCookies()	{
		return myCookies;
	}
	
	public boolean isLoggedIn()	{
		return socialLoggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn)	{
		this.socialLoggedIn = loggedIn;
	}
	
	public void clearCookies()	{
		myCookies = new HashMap<String, String>();
	}
}
