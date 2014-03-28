package com.team5.pat;

import java.util.HashMap;
import java.util.Map;

import com.team5.social.SocialAccount;
import com.team5.user.UserAccount;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
/**
 * A replacement for the Android Application. It maintains persistent states
 * @author Nick
 *
 */
public class Session extends Application {
	private Context myContext;
	private UserAccount myUserAccount;
	private boolean setup = false;
	private SocialAccount mySocialAccount;
	
	/**
	 * Initiates the persistent classes.
	 * @author Nick
	 *
	 */
	public void initiate(Context c)	{
		myContext = c;
		
		if (setup == false)	{
			myUserAccount = new UserAccount(myContext);
			mySocialAccount = new SocialAccount(myContext);
		}
			
		setup = true;
	}
	
	/**
	 * Returns the user account (For database)
	 * @author Nick
	 *
	 */
	public UserAccount getUserAccount()	{
		return myUserAccount;
	}
	
	/**
	 * Checks if setup
	 * @author Nick
	 *
	 */
	public boolean checkSetup()	{
		return setup;
	}
	
	/**
	 * Fetches social account
	 * @author Nick
	 *
	 */
	public SocialAccount getSocialAccount()	{
		return mySocialAccount;
	}
}
