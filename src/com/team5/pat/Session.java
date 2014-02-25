package com.team5.pat;

import com.team5.user.UserAccount;
import com.team5.network.NetworkInterface;

import android.app.Application;
import android.content.Context;

public class Session extends Application {
	private Context myContext;
	private UserAccount myUserAccount;
	private NetworkInterface mySocialAccount;
	private boolean setup = false;
	
	public void initiate(Context c)	{
		myContext = c;
		
		myUserAccount = new UserAccount(myContext);
		//mySocialAccount = new SocialAccount(myContext);
		
		setup = true;
	}
	
	public UserAccount getUserAccount()	{
		return myUserAccount;
	}
	
	public NetworkInterface getSocialAccount()	{
		return mySocialAccount;
	}
	
	public boolean checkSetup()	{
		return setup;
	}
}
