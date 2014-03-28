package com.team5.social;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment which displays all the user's current notifications
 * 
 * All social fragments responsibility of Nick, do not modify
 * @author Nick
 *
 */
public class NotificationFragment extends Fragment implements SocialFragmentInterface, NetworkInterface {
	private View myView;
	private HomeActivity myActivity;
	private SocialAccount mySocialAccount;
	private ListView listView;
	private ListAdapter listAdapter;
	private ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		
		myActivity = (HomeActivity) getActivity();
		mySocialAccount = ((Session) myActivity.getApplication()).getSocialAccount();
		
		if (myView == null)	{
			myView = inflater.inflate(R.layout.social_fragment_notifications, container, false);
		}
		
		// Get the list view
		listView = (ListView) myView.findViewById(R.id.social_fragment_notificationsList);
		// Create a custom adapter
		listAdapter = new ListAdapter();
		// Finally, assign the custom adapter to the list
		listView.setAdapter(listAdapter);
		
		OnItemClickListener itemListener = new OnItemClickListener()	{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				List<Notification> notifications = mySocialAccount.getNotifications();
				Notification notification = notifications.get(position);
				
				Request r = new Request(NotificationFragment.this, "fetchpost.php", mySocialAccount.getCookies());
				r.addParameter("post", notification.postID + "" );
				r.start();
				
				progressDialog = ProgressDialog.show(myActivity, "", "Finding Post...");
				
				mySocialAccount.removeNotication(position);
				listAdapter.notifyDataSetChanged();
			}
		};
		
		listView.setOnItemClickListener(itemListener);
		
		return myView;
	}
	
	/**
	 * Handles the receiving of the notifications target topic, and forwards on to the listfragment
	 * 
	 * @author Nick
	 *
	 */
	@Override
	public void eventNetworkResponse(Request from, Response response) {
		if (progressDialog!=null) {
            if (progressDialog.isShowing()) {
            	progressDialog.dismiss();       
            }
        }
		
		// Find error textview
		TextView errorOutput = (TextView) myView.findViewById(R.id.social_fragment_notificationsError);
		errorOutput.setVisibility(View.VISIBLE);
		
		if (!response.isSuccess())	{
			errorOutput.setText("Error: " + response.getMessage());
			return;
		}
		
		// Save cookies
		mySocialAccount.setCookies(response.getCookies());
		
		// Check logged in
		if (!response.getLoggedIn())	{
			mySocialAccount.handleEvent(SocialAccount.EVENT_SESSION_END);
			return;
		}
		
		// Get the request element
		Element eleRequest = response.getRequest();
		
		// Get script status
		int scriptStatus = Integer.parseInt(eleRequest.getElementsByTagName("status").item(0).getTextContent());
		// script failure
		if (scriptStatus != 0)	{
			String errorType = eleRequest.getElementsByTagName("error").item(0).getTextContent();
			String errorMessage = eleRequest.getElementsByTagName("message").item(0).getTextContent();
			errorOutput.setText("Error: " + errorType + ": " + errorMessage);
			return;
		}
		
		// Get the request element
		Element eleData = response.getData();
		
		// Get the section elements
		NodeList sectionList = eleData.getChildNodes();
		
		// Get the post element
		Element sectionElement = (Element) sectionList.item(0);
		
		// Fetch the post data			
		int postID = Integer.parseInt(sectionElement.getElementsByTagName("ID").item(0).getTextContent());
		int postOwnerID = Integer.parseInt(sectionElement.getElementsByTagName("Owner").item(0).getTextContent());
		String postOwner = sectionElement.getElementsByTagName("Name").item(0).getTextContent();
		String postContent = sectionElement.getElementsByTagName("Content").item(0).getTextContent();
		String postDate = sectionElement.getElementsByTagName("Posted").item(0).getTextContent();
		int postResponses = Integer.parseInt(sectionElement.getElementsByTagName("Responses").item(0).getTextContent());
		float postRating = 0;
		try	{
			postRating = Float.parseFloat(sectionElement.getElementsByTagName("Rating").item(0).getTextContent());
		}	catch	(NumberFormatException e)	{
			postRating = 0;
		}
		float postMyRating = 0;
		try	{
			postMyRating = Float.parseFloat(sectionElement.getElementsByTagName("MyRating").item(0).getTextContent());
		}	catch	(NumberFormatException e)	{
			postMyRating = 0;
		}
		int postMine = Integer.parseInt(sectionElement.getElementsByTagName("Mine").item(0).getTextContent());
		int postFavourites = Integer.parseInt(sectionElement.getElementsByTagName("Favourites").item(0).getTextContent());
		int postFavourited = Integer.parseInt(sectionElement.getElementsByTagName("Favourited").item(0).getTextContent());
		
		Post post = new Post(postID, postOwner, postDate, postContent, postResponses, postRating, postOwnerID, postMyRating, postMine, postFavourites, postFavourited);
		
		mySocialAccount.changeFragment(new ListFragment().defineList(post, -1, -1, -1, true));
		
		errorOutput.setText("");
		errorOutput.setVisibility(View.GONE);
	}
	
	/**
	 * ListAdapter for the notifications list
	 * 
	 * @author Nick
	 *
	 */
	class ListAdapter extends BaseAdapter	{

		@Override
		public int getCount() {
			return mySocialAccount.getNotifications().size();
		}

		@Override
		public Object getItem(int position) {
			return mySocialAccount.getNotifications().get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup container) {
			View myView = view;
			LayoutInflater inflater = (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (myView == null)
				myView = inflater.inflate(R.layout.social_fragment_notification_row, container, false);
			
			Notification notification = (Notification) getItem(position);
			
			((TextView) myView.findViewById(R.id.social_fragment_notificationDate)).setText(notification.date);
			((TextView) myView.findViewById(R.id.social_fragment_notificationContent)).setText(notification.content);
			
			((Button) myView.findViewById(R.id.social_fragment_notificationButton)).setOnClickListener(new DeleteClickListener(position));
			
			return myView;
		}
	}
	
	/**
	 * Basic listener that allows use of delete notification button
	 * 
	 * @author Nick
	 *
	 */
	class DeleteClickListener implements OnClickListener	{
		private int position;
		
		public DeleteClickListener(int position)	{
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			mySocialAccount.removeNotication(position);
			listAdapter.notifyDataSetChanged();
		}
	}
}
