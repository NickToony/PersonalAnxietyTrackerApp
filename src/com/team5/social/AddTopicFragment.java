package com.team5.social;

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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Fragment allowing user to create a new topic or reply to an already existing post
 * 
 * All social fragments responsibility of Nick, do not modify
 * @author Nick
 *
 */
public class AddTopicFragment extends Fragment implements SocialFragmentInterface, NetworkInterface, PostHandlerInterface {
	private Post postParent;
	private View myView;
	private HomeActivity myActivity;
	private SocialAccount mySocialAccount;
	private boolean networking = false;
	private ProgressDialog progressDialog;
	private RatingTouchListener ratingListener;
	
	/**
	 * Allows another fragment to define which post the user is replying to
	 * 
	 * @author Nick
	 *
	 */
	public Fragment defineParent(Post postParent) {
		this.postParent = postParent;
		return this;
	}
	
	/**
	 * Creates view, display parent post (if necessary)
	 * 
	 * @author Nick
	 *
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
		super.onCreate(savedInstanceState);
		
		myActivity = (HomeActivity) getActivity();
		mySocialAccount = ((Session) myActivity.getApplication()).getSocialAccount();
		
		if (myView == null)	{
			myView = inflater.inflate(R.layout.social_fragment_new_post, container, false);
		}
			
			View post = myView.findViewById(R.id.social_fragment_post_parent);
			if (postParent != null)	{
				// Make parent visible
				post.setVisibility(View.VISIBLE);
				
				// Find the fields of the post
				TextView textName = (TextView) post.findViewById(R.id.social_fragment_list_rowName);
				TextView textDate = (TextView) post.findViewById(R.id.social_fragment_list_rowDate);
				TextView textContent = (TextView) post.findViewById(R.id.social_fragment_list_rowContent);
				TextView textReplies = (TextView) post.findViewById(R.id.social_fragment_list_rowReplies);
				RatingBar starRatingAbove = (RatingBar) post.findViewById(R.id.social_fragment_list_rowStarRatingAbove);
				RatingBar starRatingBelow = (RatingBar) post.findViewById(R.id.social_fragment_list_rowStarRatingBelow);
				RatingBar starRatingUser = (RatingBar) post.findViewById(R.id.social_fragment_list_rowStarRatingBlue);
				TextView textFavourites = (TextView) myView.findViewById(R.id.social_fragment_list_rowFavourites);
				
				// Assign the data into it
				Post myItem = postParent;
				textName.setText(myItem.name);
				textDate.setText(myItem.date);
				textContent.setText(myItem.content);
				textReplies.setText("Replies: " + myItem.replies);
				
				textFavourites.setText("Favourited by " + myItem.favourites);
				
				RatingTouchListener starTouchListener;
				if (ratingListener == null)
					starTouchListener = new RatingTouchListener();
				else
					starTouchListener = ratingListener;
				ratingListener = starTouchListener;
				starTouchListener.setRatingBars(myItem, starRatingBelow, starRatingAbove, starRatingUser, mySocialAccount, this);
				
				Button buttonRight = (Button) myView.findViewById(R.id.social_fragment_list_buttonRight);
				Button buttonLeft = (Button) myView.findViewById(R.id.social_fragment_list_buttonLeft);
				
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
					post.findViewById(R.id.social_fragment_post_background).setBackgroundResource(R.drawable.social_container_highlight);
				else
					post.findViewById(R.id.social_fragment_post_background).setBackgroundResource(R.drawable.social_container);
				
				starTouchListener.setOthersRating(myItem.rating);
				if (myItem.myRating > 0)
					starTouchListener.setMyRating(myItem.myRating);
			}	else	{
				// No parent? No display
				post.setVisibility(View.GONE);
			}
		
		OnClickListener addClickListener = new OnClickListener() {
			@Override
			public void onClick( View v) {
				createTopic();
			}
		};
		
		myView.findViewById(R.id.social_fragment_new_post_button).setOnClickListener(addClickListener);
		return myView;
	}
	
	/**
	 * Begins networking request to create a topic
	 * 
	 * @author Nick
	 *
	 */
	private void createTopic()	{
		if (networking == false)	{
			networking = true;
			
			Request r = new Request(this, "post.php", mySocialAccount.getCookies());
			if (postParent != null)
				r.addParameter("parent", "" + postParent.id);
			r.addParameter("content", ((EditText) myView.findViewById(R.id.social_fragment_new_post_content)).getText() + "");
			r.start();
			
			progressDialog = ProgressDialog.show(myActivity, "", "Creating post...");
		}
	}

	/**
	 * Handles response from server
	 * 
	 * @author Nick
	 *
	 */
	@Override
	public void eventNetworkResponse(Request from, Response response) {
		networking = false;
		if (progressDialog!=null) {
            if (progressDialog.isShowing()) {
            	progressDialog.dismiss();       
            }
        }
		
		// Find error textview
		TextView errorOutput = (TextView) myView.findViewById(R.id.social_fragment_new_post_error);
		
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
		
		// Get the section elements
		TextView outputViews[] = {	errorOutput, (TextView) myView.findViewById(R.id.social_fragment_new_post_errorContent), errorOutput};
		NodeList sectionList = response.getData().getChildNodes();
		
		boolean success = true;
		for (int i = 0; i < sectionList.getLength(); i ++)	{
			// Get the section element
			Element sectionElement = (Element) sectionList.item(i);
			
			// Fetch the section data
			int sectionResponse = Integer.parseInt(sectionElement.getElementsByTagName("response").item(0).getTextContent());
			String sectionMessage = sectionElement.getElementsByTagName("message").item(0).getTextContent();
			
			// Output into correct position
			if (sectionResponse != 0)	{
				outputViews[i].setText(sectionElement.getNodeName() + ": " + sectionMessage);
				success = false;
			}	else	{
				outputViews[i].setText("");
			}
		}
		
		if (!success)
			return;
		
		if (errorOutput != null)
			errorOutput.setText("");
		
		// By here, success
		if (postParent != null)
			postParent.replies ++;
		mySocialAccount.handleEvent(SocialAccount.EVENT_GO_BACK);
	}

	/**
	 * Refreshs the view
	 * 
	 * @author Nick
	 *
	 */
	@Override
	public void refresh() {
		myView.invalidate();
	}
}
