package com.team5.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

public class ListFragment extends Fragment implements SocialFragmentInterface, NetworkInterface  {
	private View myView;
	private HomeActivity myActivity;
	private MainFragment myParent;
	
	private ListView listView;
	private ListAdapter listAdapter;
	
	private int postParent = -1; // fetch by post parent?
	private int postOwner = -1; // fetch by owner?
	private int postOrder = 0; // 0 = new, 1 = top,
	private int postFavourites = 0; // fetch by favourites?
	
	private boolean networking = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.social_fragment_list, container, false);
		myActivity = (HomeActivity) getActivity();
		
		// Get the list view
		listView = (ListView) myView.findViewById(R.id.social_fragment_list_listView);
		// Create a custom adapter
		listAdapter = new ListAdapter(myActivity, R.layout.social_fragment_list_row);
		
		// Insert test data..
		//listAdapter.addItem(new ListItem(1, "jsmith92", "16:35 02 Mar", "I am very anxious about an upcoming exam. Does anyone have any advice for me? Or recomend any techniques for calming down?", 4, 4.5));
		//listAdapter.addItem(new ListItem(2, "bdavidson12", "13:22 01 Mar", "I have a big class presentation tomorrow infront of a lot of people. I feel nauseous. Does anyone have any tips for me?", 1, 2.5));
		new Request(this, "http://nick-hope.co.uk/PAT/android/fetchposts.php", "parent=" + postParent + "&owner=" + postOwner + "&order=" + postOrder + "&favourites=" + postFavourites, getCookies());
		networking = true;
		
		// Finally, assign the custom adapter to the list
		listView.setAdapter(listAdapter);
		
		
		
		return myView;
	}
	
	public ListFragment defineList(int postParent, int postOwner, int postOrder, int postFavourites)	{
		this.postParent = postParent; // fetch by post parent?
		this.postOwner = postOwner; // fetch by owner?
		
		if (postOrder != -1)
			this.postOrder = postOrder;
		if (postFavourites != -1)
			this.postFavourites = postFavourites;
			
		
		return this;
	}
	
	@Override
	public void eventNetworkResponse(Request request, Response response)	{
		networking = false;
		
		// Find error textview
		TextView errorOutput = (TextView) myView.findViewById(R.id.social_fragment_list_errorMessage);
		
		if (!response.isSuccess())	{
			errorOutput.setText("Error: " + response.getMessage());
			return;
		}
		
		// Save cookies
		setCookies(response.getCookies());
		
		// Check logged in
		if (!response.getLoggedIn())	{
			myParent.eventChild(SocialFragment.EVENT_SESSION_END);
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
		
		// For each post
		for (int i = 0; i < sectionList.getLength(); i ++)	{
			// Get the post element
			Element sectionElement = (Element) sectionList.item(i);
			
			// Fetch the post data			
			int postID = Integer.parseInt(sectionElement.getElementsByTagName("ID").item(0).getTextContent());
			int postOwnerID = Integer.parseInt(sectionElement.getElementsByTagName("Owner").item(0).getTextContent());
			String postOwner = sectionElement.getElementsByTagName("Name").item(0).getTextContent();
			String postContent = sectionElement.getElementsByTagName("Content").item(0).getTextContent();
			String postDate = sectionElement.getElementsByTagName("Posted").item(0).getTextContent();
			int postResponses = Integer.parseInt(sectionElement.getElementsByTagName("Responses").item(0).getTextContent());
			float postRating;
			try	{
				postRating = Float.parseFloat(sectionElement.getElementsByTagName("Rating").item(0).getTextContent());
			}	catch	(NumberFormatException e)	{
				postRating = 0;
			}
			
			// Add the item to list
			listAdapter.addItem(new ListItem(postID, postOwner, postDate, postContent, postResponses, postRating, postOwnerID));
		}
		
		errorOutput.setText("");
				
		networking = false;
	}

	@Override
	public void setParentFragment(SocialFragmentInterface frag) {
		this.myParent = (MainFragment) frag;
	}
	
	@Override
	public void setCookies(Map<String, String> cookieMap)	{
		myParent.setCookies(cookieMap);
	}
	@Override
	public Map<String, String> getCookies()	{
		return myParent.getCookies();
	}
	
	@Override
	public void eventChild(int eventID)	{
		myParent.eventChild(eventID);
	}
	
	
	/*
	 * ListItem
	 * 
	 * Contains data of each item in the list
	 */
	private class ListItem {
		public String name;
		public String date;
		public String content;
		public int replies;
		public double rating;
		
		public int id;
		public int ownerId;
		
		public ListItem(int id, String name, String date, String content, int replies, double rating, int ownerId)	{
			this.id = id;
			this.name = name;
			this.date = date;
			this.content = content;
			this.replies = replies;
			this.rating = rating;
			this.ownerId = ownerId;
		}
	}
	
	
	/*
	 * ListAdapter
	 * 
	 * Adapter for the topics list
	 */
	private class ListAdapter extends ArrayAdapter<ListItem> {
		private List<ListItem> items = new ArrayList<ListItem>();
		private Context context;
		private int resource;
		
		public ListAdapter(Context context, int resource) {
			super(context, resource);
			this.context = context;
			this.resource = resource;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			myView = convertView;
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (myView == null) {
				myView = inflater.inflate(resource, parent, false);
			}

			TextView textName = (TextView) myView.findViewById(R.id.social_fragment_list_rowName);
			TextView textDate = (TextView) myView.findViewById(R.id.social_fragment_list_rowDate);
			TextView textContent = (TextView) myView.findViewById(R.id.social_fragment_list_rowContent);
			TextView textReplies = (TextView) myView.findViewById(R.id.social_fragment_list_rowReplies);
			TextView textRating = (TextView) myView.findViewById(R.id.social_fragment_list_rowRating);
			
			
			ListItem myItem = getItem(position);
			textName.setText(myItem.name);
			textDate.setText(myItem.date);
			textContent.setText(myItem.content);
			textReplies.setText("Replies: " + myItem.replies);
			textRating.setText("Rating: " + myItem.rating);

			return myView;
		}
		
		@Override
		public int getCount()	{
			return items.size();
		}
		
		@Override
		public ListItem getItem(int position)	{
			return items.get(position);
		}
		
		@Override
		public long getItemId(int position)	{
			return getItem(position).id;
		}
		
		public void addItem(ListItem item)	{
			//items.add(new NavListItem(context.getResources().getDrawable( navArray[0] ), label));
			items.add(item);
			((BaseAdapter) this).notifyDataSetChanged(); 
		}
		
		// This code disables highlightung
		public boolean areAllItemsEnabled() 
        { 
                return false; 
        } 
        public boolean isEnabled(int position) 
        { 
                return false; 
        }
		
	}
}