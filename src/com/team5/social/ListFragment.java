package com.team5.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

public class ListFragment extends Fragment implements SocialFragmentInterface, NetworkInterface  {
	private View myView;
	private HomeActivity myActivity;
	
	private ListView listView;
	private ListAdapter listAdapter;
	
	private Post postParent = null; // fetch by post parent?
	private int postOwner = -1; // fetch by owner?
	private int postOrder = 0; // 0 = new, 1 = top,
	private int postFavourites = 0; // fetch by favourites?
	
	private int scrollPosition = 0;
	
	private boolean networking = false;
	private SocialAccount mySocialAccount;
	
	private ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		
		myActivity = (HomeActivity) getActivity();
		mySocialAccount = ((Session) myActivity.getApplication()).getSocialAccount();
		
		if (savedInstanceState != null)	{
			
			this.postParent = (Post) savedInstanceState.getParcelable("postParent");
			this.postFavourites = savedInstanceState.getInt("postFavourites");
			this.postOrder = savedInstanceState.getInt("postOrder");
			this.postOwner = savedInstanceState.getInt("postOwner");
		}
		
		if (myView == null)	{
			myView = inflater.inflate(R.layout.social_fragment_list, container, false);
			
			// Get the list view
			listView = (ListView) myView.findViewById(R.id.social_fragment_list_listView);
			// Create a custom adapter
			listAdapter = new ListAdapter(myActivity, R.layout.social_fragment_list_row_parent, R.layout.social_fragment_list_row_child);
			// Finally, assign the custom adapter to the list
			listView.setAdapter(listAdapter);
			
			OnItemClickListener topicClickListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					scrollPosition = position;
					Post item = (Post) parent.getAdapter().getItem(position);
					mySocialAccount.changeFragment(new ListFragment().defineList(item, -1, 0, -1));
				}
			};
			
			OnClickListener addClickListener = new OnClickListener() {
				@Override
				public void onClick( View v) {
					mySocialAccount.changeFragment(new AddTopicFragment().defineParent(postParent));
				}
			};
			
			listView.setOnItemClickListener(topicClickListener);
			myView.findViewById(R.id.social_fragment_list_addPost).setOnClickListener(addClickListener);
		}
		
		// Insert test data..
		//listAdapter.addItem(new ListItem(1, "jsmith92", "16:35 02 Mar", "I am very anxious about an upcoming exam. Does anyone have any advice for me? Or recomend any techniques for calming down?", 4, 4.5));
		//listAdapter.addItem(new ListItem(2, "bdavidson12", "13:22 01 Mar", "I have a big class presentation tomorrow infront of a lot of people. I feel nauseous. Does anyone have any tips for me?", 1, 2.5));
		String parameters = "";
		if (postParent != null)	{
			// Add the parameters
			parameters += "parent=" + postParent.id + "&";
			// Output the parent
			listAdapter.addItem(postParent, true);
		}
		parameters += "owner=" + postOwner + "&";
		parameters += "favourites=" + postFavourites + "&";
		parameters += "order=" + postOrder + "";
		
		new Request(this, "http://nick-hope.co.uk/PAT/android/fetchposts.php", parameters, mySocialAccount.getCookies());
		
		myView.findViewById(R.id.social_fragment_list_progress).setVisibility(View.VISIBLE);
		progressDialog = ProgressDialog.show(myActivity, "", "Fetching Posts...");
		networking = true;
		
		return myView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		/*
		 * 	private ListItem postParent = null; // fetch by post parent?
			private int postOwner = -1; // fetch by owner?
			private int postOrder = 0; // 0 = new, 1 = top,
			private int postFavourites = 0; // fetch by favourites?
			
			private int scrollPosition = 0;
		 */
		
		outState.putParcelable("postParent", postParent);
	    outState.putInt("postOwner", postOwner);
	    outState.putInt("postOrder", postOrder);
	    outState.putInt("postFavourites", postFavourites);
	    
	    super.onSaveInstanceState(outState);
	}
	
	public ListFragment defineList(Post postParent, int postOwner, int postOrder, int postFavourites)	{
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
		myView.findViewById(R.id.social_fragment_list_progress).setVisibility(View.INVISIBLE);
		if (progressDialog!=null) {
            if (progressDialog.isShowing()) {
            	progressDialog.dismiss();       
            }
        }
		
		// Find error textview
		TextView errorOutput = (TextView) myView.findViewById(R.id.social_fragment_list_errorMessage);
		
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
		
		// Clear posts already there
		listAdapter.clear();
		
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
			// Add the item to list
			boolean isParent;
			if (postParent == null)
				isParent = true;
			else
				isParent = false;
			listAdapter.addItem(new Post(postID, postOwner, postDate, postContent, postResponses, postRating, postOwnerID), isParent);
		}
		
		if (errorOutput != null)
			errorOutput.setText("");
				
		networking = false;
		
		// move scroll to correct position
		listView.setSelectionFromTop(scrollPosition, 0);
		Log.i("List", "Position should be: " + scrollPosition);
	}
	
	
	
	
	
	/*
	 * ListAdapter
	 * 
	 * Adapter for the topics list
	 */
	private class ListAdapter extends BaseAdapter {
		private List<Post> items = new ArrayList<Post>();
		private List<Boolean> itemIsParent = new ArrayList<Boolean>();
		private Context context;
		private int resourceParent;
		private int resourceChild;
		
		public ListAdapter(Context context, int resourceParent, int resourceChild) {
			super();
			this.context = context;
			this.resourceParent = resourceParent;
			this.resourceChild = resourceChild;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			int type = getItemViewType(position);
			if (convertView == null)	{
				if (type == 0)
					convertView = inflater.inflate(resourceParent, null);
				else
					convertView = inflater.inflate(resourceChild, null);
			}

			TextView textName = (TextView) convertView.findViewById(R.id.social_fragment_list_rowName);
			TextView textDate = (TextView) convertView.findViewById(R.id.social_fragment_list_rowDate);
			TextView textContent = (TextView) convertView.findViewById(R.id.social_fragment_list_rowContent);
			TextView textReplies = (TextView) convertView.findViewById(R.id.social_fragment_list_rowReplies);
			TextView textRating = (TextView) convertView.findViewById(R.id.social_fragment_list_rowRating);
			
			
			Post myItem = getItem(position);
			textName.setText(myItem.name);
			textDate.setText(myItem.date);
			textContent.setText(myItem.content);
			textReplies.setText("Replies: " + myItem.replies);
			textRating.setText("Rating: " + myItem.rating);
			
			return convertView;
		}
		
		@Override
		public int getCount()	{
			return items.size();
		}
		
		@Override
		public Post getItem(int position)	{
			return items.get(position);
		}
		
		@Override
		public long getItemId(int position)	{
			return getItem(position).id;
		}
		
		public void addItem(Post item, boolean isParent)	{
			//items.add(new NavListItem(context.getResources().getDrawable( navArray[0] ), label));
			items.add(item);
			itemIsParent.add(isParent);
			((BaseAdapter) this).notifyDataSetChanged(); 
		}
		
		// This code disables highlightung
		/*public boolean areAllItemsEnabled() 
        { 
                return false; 
        }*/ 
		@Override
        public boolean isEnabled(int position) 
        { 
        	if (postParent == null)
        		return true;
        	return !(itemIsParent.get(position));
        }
		
		@Override
		public int getViewTypeCount()	{
			return 2;
		}
		
		@Override
		public int getItemViewType(int position)	{
			if (itemIsParent.get(position))
				return 0;
			else
				return 1;
		}
		
		public void clear()	{
			for (int i = 1; i < items.size(); i ++)	{
				items.clear();
				itemIsParent.clear();
			}
			((BaseAdapter) this).notifyDataSetChanged(); 
		}
		
	}
}
