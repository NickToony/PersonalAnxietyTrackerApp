
package com.team5.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.team5.navigationlist.NavListAdapter;
import com.team5.network.Request;
import com.team5.pat.HomeActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class SocialAccount {	
	private HomeActivity myActivity;
	private List<Fragment> myStack = new ArrayList<Fragment>();
	private boolean socialLoggedIn = false;
	private Map<String, String> myCookies = new HashMap<String, String>();
	
	public final static int EVENT_SIGN_IN = 0;
	public final static int EVENT_SIGN_OUT = 1;
	public final static int EVENT_SESSION_END = 2;
	public final static int EVENT_GOTO_BROWSE = 3;
	public final static int EVENT_GO_BACK = 4;
	public final static int EVENT_EXIT = 5;
	public static final int EVENT_NEW_POST = 6;
	
	public SocialAccount	(Context c)	{
		myActivity = (HomeActivity) c;
	}
	
	public void useActivity(Context c)	{
		this.myActivity = (HomeActivity) c;
	}
	
	public void navigateTo()	{
		resetFragments();
		
		if (isLoggedIn())	{
			changeFragment(new MainFragment());
		}	else	{
			changeFragment(new LoginFragment());
		}
	}
	
	public void changeFragment(Fragment theFrag)	{	
		// Store it on the stack
		myStack.add(theFrag);
		
		// Have HomeActivity change to the new fragment
		myActivity.changeFragment(theFrag);
	}
	
	private void popFragment()	{
		if (myStack.size() > 1)	{
			// Remove current item
			myStack.remove(myStack.size() - 1);
			// Navigate to item before
			Fragment frag = myStack.get(myStack.size() - 1);
			if (frag instanceof BrowsePostsFragment)
				changeFragment(new BrowsePostsFragment());
			else
				changeFragment(frag);
			// Remove the new one added
			myStack.remove(myStack.size() - 1);
		}	else	{
			handleEvent(EVENT_EXIT);
		}
	}
	
	public void resetFragments()	{
		/*for (int i = 0; i < myStack.size(); i ++)	{
			FragmentTransaction fragmentTransaction = myActivity.getFragmentManager().beginTransaction();
			fragmentTransaction.remove(myStack.get(i)).commit();
		}*/
		
		myStack.clear();
	}
	
	public void handleEvent(int eventID)	{
		Log.i("Social Fragment", "Triggered Event: " + eventID);
		switch (eventID)	{
		
		// Sign in event
		case EVENT_SIGN_IN:
			setLoggedIn(true);
			// Go to main fragment
			resetFragments();
			changeFragment(new MainFragment());
			break;
			
		// User wants to sign out
		case EVENT_SIGN_OUT:
			clearCookies();
			setLoggedIn(false);
			
			new Request(null, "http://nick-hope.co.uk/logout.php");
			
			handleEvent(EVENT_EXIT);
			break;
			
		// Users session expired (or a problem)
		case EVENT_SESSION_END:
			setLoggedIn(false);
			// resetFragments(); -- commented to allow for recovery of fragments
			// go to login fragment
			navigateTo();
			
			Toast.makeText(myActivity.getApplicationContext(), "Lost Connection to Session", 
					   Toast.LENGTH_LONG).show();
			
			break;
			
		// User wants to browse topics
		case EVENT_GOTO_BROWSE:
			changeFragment(new BrowsePostsFragment());
			break;
			
		// User wants to go back a fragment
		case EVENT_GO_BACK:
			popFragment();
			break;
			
		// User wants to leave social
		case EVENT_EXIT:
			myActivity.doNavigation(NavListAdapter.navigationHome);
			break;
			
		// User wants to make a new topic
		case EVENT_NEW_POST:
			changeFragment(new AddTopicFragment());
			break;
		}
	}
	
	public boolean isLoggedIn()	{
		return socialLoggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn)	{
		this.socialLoggedIn = loggedIn;
	}
	
	public void setCookies(Map<String, String> cookieMap)	{
		myCookies.putAll(cookieMap);
	}
	
	public Map<String, String> getCookies()	{
		return myCookies;
	}
	
	public void clearCookies()	{
		myCookies = new HashMap<String, String>();
	}
}

class StackedFragment	{
	public Fragment myFragment;
	public Bundle myFragmentState;
}