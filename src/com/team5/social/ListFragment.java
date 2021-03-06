package com.team5.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

/**
 * Fragment which displays a list of posts. It will fetches posts by order, parent, owner and favourites.
 * 
 * All social fragments responsibility of Nick, do not modify
 * @author Nick
 *
 */
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
	private boolean doActionBar = true;
	private SpinnerAdapter dropDownAdapter;
	private SocialAccount mySocialAccount;
	
	private ProgressDialog progressDialog;
	
	public static final int ORDER_NEW = 0;
	public static final int ORDER_OLD = 1;
	public static final int ORDER_TOP = 2;
	
	/**
	 * Restores state if resumed. Creates views and begins request.
	 * Also sets up action bar if required (for sorting)
	 * 
	 * @author Nick
	 *
	 */
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
			this.doActionBar = savedInstanceState.getBoolean("doActionBar");
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
					mySocialAccount.changeFragment(new ListFragment().defineList(item, -1, 0, -1, true));
				}
			};
			
			/* OnClickListener addClickListener = new OnClickListener() {
				@Override
				public void onClick( View v) {
					mySocialAccount.changeFragment(new AddTopicFragment().defineParent(postParent));
				}
			};*/
			
			listView.setOnItemClickListener(topicClickListener);
			// myView.findViewById(R.id.social_fragment_list_addPost).setOnClickListener(addClickListener);
		}
		
		fetchPosts();
		
		if (doActionBar)	{
			setHasOptionsMenu(true);
		}
		
		return myView;
	}
	
	/**
	 * Begins the request, adding on fetching parameters
	 * 
	 * @author Nick
	 *
	 */
	private void fetchPosts()	{
		if (networking == true)	return;
		
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
		
		
		Request r = new Request(this, "fetchposts.php", mySocialAccount.getCookies());
		if (postParent != null)
			r.addParameter("parent", postParent.id + "");
		r.addParameter("owner", postOwner + "");
		r.addParameter("favourites", postFavourites + "");
		r.addParameter("order", postOrder + "");
		r.start();
		
		myView.findViewById(R.id.social_fragment_list_progress).setVisibility(View.VISIBLE);
		//progressDialog = ProgressDialog.show(myActivity, "", "Fetching Posts...");
		networking = true;
	}
	
	/**
	 * Stores list parameters for resumed state
	 * 
	 * @author Nick
	 *
	 */
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
	    outState.putBoolean("doActionBar", doActionBar);
	    
	    super.onSaveInstanceState(outState);
	}
	
	/**
	 * Creates the action bar (if necessary)
	 * 
	 * Allows sorting of posts based on rating and age
	 * 
	 * @author Nick
	 *
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    
		// If this fragment is to setup the dropdown
				if (doActionBar)	{
					// If no adapter is ready
					if (dropDownAdapter == null)	{
						// Make a new adapter using strings
						dropDownAdapter = ArrayAdapter.createFromResource(getActivity(),
							R.array.social_list_items, R.layout.social_fragment_list_dropdown);
					}
					// Fetch spinner (dropdown)
					Spinner theSpinner = (Spinner) myActivity.getDropDown().getActionView();
					// Apply adapter
					theSpinner.setAdapter( dropDownAdapter);
					// Apply onclick listener
					theSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
						@Override
						public void onItemSelected(AdapterView<?> view, View row, int position, long id) {
							switch (position) {
							case 0:
								postOrder = ListFragment.ORDER_NEW;
								fetchPosts();
								return;
							case 1:
								postOrder = ListFragment.ORDER_OLD;
								fetchPosts();
								return;
							case 2:
								postOrder = ListFragment.ORDER_TOP;
								fetchPosts();
								return;
							default:
								return;
							}
							
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							return;
						}
				    	
				    });
					// Make the dropdown visible
					myActivity.getDropDown().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
					
					mySocialAccount.fixNotifications();
				}
		
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	/**
	 * Define the parameters for which posts to display
	 * 
	 * @author Nick
	 *
	 */
	public ListFragment defineList(Post postParent, int postOwner, int postOrder, int postFavourites, boolean doActionBar)	{
		this.postParent = postParent; // fetch by post parent?
		this.postOwner = postOwner; // fetch by owner?
		
		if (postOrder != -1)	{
			this.postOrder = postOrder;
		}
		if (postFavourites != -1)	{
			this.postFavourites = postFavourites;
		}
			
		this.doActionBar = doActionBar;
		
		return this;
	}
	
	/**
	 * Listener for the dropdown, changes sorting accordingly
	 * 
	 * @author Nick
	 *
	 */
	class DropDownListener implements OnNavigationListener {
		@Override
		public boolean onNavigationItemSelected(int position, long id) {
			switch (position) {
			case 0:
				postOrder = ListFragment.ORDER_NEW;
				fetchPosts();
				return true;
			case 1:
				postOrder = ListFragment.ORDER_OLD;
				fetchPosts();
				return true;
			case 2:
				postOrder = ListFragment.ORDER_TOP;
				fetchPosts();
				return true;
			default:
				return false;
			}
		}
	}
	
	/**
	 * Handles the receiving of the posts
	 * 
	 * @author Nick
	 *
	 */
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
		
		// Add space at top
		listAdapter.addItem(null, true);
		
		if (postParent != null)	{
			// Output the parent
			listAdapter.addItem(postParent, true);
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
			
			// Add the item to list
			// Add the item to list
			boolean isParent;
			if (postParent == null)
				isParent = true;
			else
				isParent = false;
			listAdapter.addItem(new Post(postID, postOwner, postDate, postContent, postResponses, postRating, postOwnerID, postMyRating, postMine, postFavourites, postFavourited), isParent);
		}
		
		listAdapter.addItem(null, true);
		
		if (errorOutput != null)
			errorOutput.setText("");
				
		networking = false;
		
		// move scroll to correct position
		listView.setSelectionFromTop(scrollPosition, 0);
		Log.i("List", "Position should be: " + scrollPosition);
	}
	
	
	
	
	
	/**
	 * Adapter for the list of topics, handles whats clickable, and how it appears.
	 * 
	 * @author Nick
	 *
	 */
	private class ListAdapter extends BaseAdapter implements PostHandlerInterface {
		private List<Post> items = new ArrayList<Post>();
		private List<Boolean> itemIsParent = new ArrayList<Boolean>();
		private List<RatingTouchListener> ratingListeners = new ArrayList<RatingTouchListener>();
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
				else if (type == 1)
					convertView = inflater.inflate(resourceChild, null);
				else if (type == 2)	{
					//convertView = inflater.inflate(R.id.spacer, null);
					convertView = inflater.inflate(R.layout.social_fragment_spacer, null);
				}
			}
			
			if (type == 2)	{
				//convertView.setBackground(null);
				return convertView;
			}

			TextView textName = (TextView) convertView.findViewById(R.id.social_fragment_list_rowName);
			TextView textDate = (TextView) convertView.findViewById(R.id.social_fragment_list_rowDate); 
			TextView textContent = (TextView) convertView.findViewById(R.id.social_fragment_list_rowContent);
			TextView textReplies = (TextView) convertView.findViewById(R.id.social_fragment_list_rowReplies);
			//TextView textRating = (TextView) convertView.findViewById(R.id.social_fragment_list_rowRating);
			RatingBar starRatingAbove = (RatingBar) convertView.findViewById(R.id.social_fragment_list_rowStarRatingAbove);
			RatingBar starRatingBelow = (RatingBar) convertView.findViewById(R.id.social_fragment_list_rowStarRatingBelow);
			RatingBar starRatingUser = (RatingBar) convertView.findViewById(R.id.social_fragment_list_rowStarRatingBlue);
			TextView textFavourites = (TextView) convertView.findViewById(R.id.social_fragment_list_rowFavourites);
			
			Post myItem = getItem(position);
			
			
			textName.setText(myItem.name);
			textDate.setText(myItem.date);
			textContent.setText(myItem.content);
			if (myItem.replies > 0)
				textReplies.setText("Replies: " + myItem.replies);
			else
				textReplies.setText("No Replies Yet!");
			textFavourites.setText("Favourited by " + myItem.favourites);
			
			RatingTouchListener starTouchListener;
			if (ratingListeners.get(position) == null)
				starTouchListener = new RatingTouchListener();
			else
				starTouchListener = ratingListeners.get(position);
			starTouchListener.setRatingBars(myItem, starRatingBelow, starRatingAbove, starRatingUser, mySocialAccount, this);
			
			Button buttonRight = (Button) convertView.findViewById(R.id.social_fragment_list_buttonRight);
			Button buttonLeft = (Button) convertView.findViewById(R.id.social_fragment_list_buttonLeft);
			
			if (myItem.favourited)
				buttonLeft.setText("Favourited");
			else
				buttonLeft.setText("Favourite");
			
			if (buttonRight != null)
				buttonRight.setOnClickListener(starTouchListener);
			if (buttonLeft != null)
				buttonLeft.setOnClickListener(starTouchListener);
			
			starTouchListener.setOthersRating(myItem.rating);
			if (myItem.myRating > 0)
				starTouchListener.setMyRating(myItem.myRating);
			else
				starTouchListener.resetMyRating();
			
			if (!myItem.mine)
				convertView.findViewById(R.id.social_fragment_post_background).setBackgroundResource(R.drawable.social_container_highlight);
			else
				convertView.findViewById(R.id.social_fragment_post_background).setBackgroundResource(R.drawable.social_container);
					
			
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
			if (getItem(position) != null)
				return getItem(position).id;
			return -1;
		}
		
		public void addItem(Post item, boolean isParent)	{
			//items.add(new NavListItem(context.getResources().getDrawable( navArray[0] ), label));
			items.add(item);
			itemIsParent.add(isParent);
			ratingListeners.add(null);
			((BaseAdapter) this).notifyDataSetChanged(); 
		}
		
		@Override
        public boolean isEnabled(int position) 
        { 
			if (items.get(position) == null)
				return false;
        	if (postParent == null)
        		return true;
        	return !(itemIsParent.get(position));
        }
		
		@Override
		public int getViewTypeCount()	{
			return 3;
		}
		
		@Override
		public int getItemViewType(int position)	{
			if (items.get(position) == null)
				return 2;
			if (itemIsParent.get(position))
				return 0;
			else
				return 1;
		}
		
		public void clear()	{
			//for (int i = 0; i < items.size(); i ++)	{
				items.clear();
				itemIsParent.clear();
			//}
			((BaseAdapter) this).notifyDataSetChanged(); 
		}

		@Override
		public void refresh() {
			notifyDataSetChanged();
		}
		
	}

}
