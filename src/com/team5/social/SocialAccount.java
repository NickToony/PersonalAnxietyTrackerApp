
package com.team5.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.team5.navigationlist.NavListAdapter;
import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SocialAccount implements NetworkInterface {	
	private HomeActivity myActivity;
	private List<Fragment> myStack = new ArrayList<Fragment>();
	private boolean socialLoggedIn = false;
	private Map<String, String> myCookies = new HashMap<String, String>();
	private List<Notification> myNotifications;
	private long lastNotificationUpdate = 0;
	
	public final static int EVENT_SIGN_IN = 0;
	public final static int EVENT_SIGN_OUT = 1;
	public final static int EVENT_SESSION_END = 2;
	public final static int EVENT_GOTO_BROWSE = 3;
	public final static int EVENT_GO_BACK = 4;
	public final static int EVENT_EXIT = 5;
	public static final int EVENT_NEW_POST = 6;
	public static final int EVENT_GOTO_ACCOUNT = 7;
	public static final int EVENT_GOTO_MINE = 8;
	public static final int EVENT_GOTO_FAVOURITES = 9;
	public static final int EVENT_GOTO_NOTIFICATIONS = 10;
	
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
		
		myActivity.setTitle("Discussion");
	}
	
	public boolean isNotifications()	{
		return myNotifications != null;
	}
	
	public List<Notification> getNotifications()	{
		return myNotifications;
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
	
	private void setupNotifications()	{
		myNotifications = new ArrayList<Notification>();
		
		fixNotifications();
		
		updateNotifications();
	}
	
	public void fixNotifications()	{
			myActivity.getNotifications().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		OnClickListener onClick = new OnClickListener()	{

			@Override
			public void onClick(View v) {
				SocialAccount.this.handleEvent(EVENT_GOTO_NOTIFICATIONS);
			}
			
		};
		myActivity.getNotifications().getActionView().findViewById(R.id.social_notification_layout).setOnClickListener(onClick);
		
		setNotificationText();
	}
	
	private void deleteNotifications()	{
		myNotifications = null;
		myActivity.getNotifications().setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}
	
	public void updateNotifications()	{
		long currentTime = System.currentTimeMillis() / 1000;
		if (currentTime <= lastNotificationUpdate + 15)
			return;
		
		Request r = new Request(this, "http://nick-hope.co.uk/PAT/android/notifications.php", getCookies());
		r.start();
		
		lastNotificationUpdate = currentTime;
	}
	
	@Override
	public void eventNetworkResponse(Request request, Response response)	{
		myNotifications.clear();
		
		if (!response.isSuccess())	{
			return;
		}
		
		// Save cookies
		setCookies(response.getCookies());
		
		// Get the request element
		Element eleRequest = response.getRequest();
		
		// Get script status
		int scriptStatus = Integer.parseInt(eleRequest.getElementsByTagName("status").item(0).getTextContent());
		// script failure
		if (scriptStatus != 0)	{
			return;
		}
		
		// Check logged in
		if (!response.getLoggedIn())	{
			handleEvent(SocialAccount.EVENT_SESSION_END);
			return;
		}
		
		// Get the request element
		Element eleData = response.getData();
		
		// Get the section elements
		NodeList sectionList = eleData.getChildNodes();
		
		// For each post
		for (int i = 0; i < sectionList.getLength(); i ++)	{
			// Get the post element
			Element sectionElement = (Element) sectionList.item(i);
			
			// Fetch the post data			
			int notificationID = Integer.parseInt(sectionElement.getElementsByTagName("ID").item(0).getTextContent());
			int postID = Integer.parseInt(sectionElement.getElementsByTagName("Post").item(0).getTextContent());
			String date = sectionElement.getElementsByTagName("Date").item(0).getTextContent();
			String content = sectionElement.getElementsByTagName("Content").item(0).getTextContent();
			
			myNotifications.add(new Notification(notificationID, postID, date, content));
			
		}
		
		
		setNotificationText();
	}
	
	private void setNotificationText()	{
		TextView text = (TextView) myActivity.getNotifications().getActionView().findViewById(R.id.social_notification_text);
		text.setText(myNotifications.size() + " Notification");
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
			
			setupNotifications();
			break;
			
		// User wants to sign out
		case EVENT_SIGN_OUT:
			clearCookies();
			setLoggedIn(false);
			
			new Request(null, "http://nick-hope.co.uk/logout.php");
			
			deleteNotifications();
			
			handleEvent(EVENT_EXIT);
			break;
			
		// Users session expired (or a problem)
		case EVENT_SESSION_END:
			setLoggedIn(false);
			// resetFragments(); -- commented to allow for recovery of fragments
			// go to login fragment
			navigateTo();
			
			deleteNotifications();
			
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
			
		case EVENT_GOTO_ACCOUNT:
			changeFragment(new AccountFragment());
			break;
			
		case EVENT_GOTO_MINE:
			changeFragment(new ListFragment().defineList(null, 1, ListFragment.ORDER_NEW, -1, true));
			break;
			
		case EVENT_GOTO_FAVOURITES:
			changeFragment(new ListFragment().defineList(null, -1, ListFragment.ORDER_NEW, 1, true));
			break;
			
		case EVENT_GOTO_NOTIFICATIONS:
			changeFragment(new NotificationFragment());
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

	public void removeNotication(int position) {
		Request r = new Request(null, "http://nick-hope.co.uk/PAT/android/deletenotification.php", getCookies());
		r.addParameter("notification", myNotifications.get(position).notificationID + "");
		r.start();
		
		myNotifications.remove(position);
		
		setNotificationText();
	}
}

class StackedFragment	{
	public Fragment myFragment;
	public Bundle myFragmentState;
}