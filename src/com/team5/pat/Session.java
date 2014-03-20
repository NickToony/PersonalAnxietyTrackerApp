package com.team5.pat;

import java.util.HashMap;
import java.util.Map;

import com.team5.social.SocialAccount;
import com.team5.user.UserAccount;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class Session extends Application {
	private Context myContext;
	private UserAccount myUserAccount;
	private boolean setup = false;
	private SocialAccount mySocialAccount;
	
	public void initiate(Context c)	{
		myContext = c;
		
		if (setup == false)	{
			myUserAccount = new UserAccount(myContext);
			mySocialAccount = new SocialAccount(myContext);
		}
			
		setup = true;
	}
	
	public UserAccount getUserAccount()	{
		return myUserAccount;
	}
	
	public boolean checkSetup()	{
		return setup;
	}
	
	public SocialAccount getSocialAccount()	{
		return mySocialAccount;
	}
}
