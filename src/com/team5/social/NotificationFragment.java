package com.team5.social;

import java.util.List;

import com.team5.network.Request;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;

import android.app.Fragment;
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

public class NotificationFragment extends Fragment implements SocialFragmentInterface {
	private View myView;
	private HomeActivity myActivity;
	private SocialAccount mySocialAccount;
	private ListView listView;
	private ListAdapter listAdapter;
	
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
				
				mySocialAccount.changeFragment(new ListFragment().defineList(notification.postID, -1, -1, -1, true));
				
				mySocialAccount.removeNotication(position);
				listAdapter.notifyDataSetChanged();
			}
		};
		
		listView.setOnItemClickListener(itemListener);
		
		return myView;
	}
	
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
