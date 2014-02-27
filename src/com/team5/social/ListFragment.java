package com.team5.social;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

public class ListFragment extends Fragment implements SocialFragmentInterface  {
	private View myView;
	private HomeActivity myActivity;
	private MainFragment myParent;
	
	private ListView listView;
	private ListAdapter listAdapter;
	
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
		listAdapter.addItem(new ListItem("A new topic", "Hello there, I am a subtitle.", 1));
		listAdapter.addItem(new ListItem("Another topic", "Hello there, I am another subtitle.", 2));
		listAdapter.addItem(new ListItem("Proof", "..that the listview works.", 3));
		listAdapter.addItem(new ListItem("Desperate plea", "A story of anxiety. Lovely!", 4));
		
		// Finally, assign the custom adapter to the list
		listView.setAdapter(listAdapter);
		
		
		
		return myView;
	}

	@Override
	public void setParentFragment(SocialFragmentInterface frag) {
		this.myParent = (MainFragment) frag;
	}
	
	
	/*
	 * ListItem
	 * 
	 * Contains data of each item in the list
	 */
	private class ListItem {
		public String title;
		public String description;
		public int id;
		
		public ListItem(String title, String description, int id)	{
			this.title = title;
			this.description = description;
			this.id = id;
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

			TextView textTitle = (TextView) myView.findViewById(R.id.social_fragment_list_rowTitle);
			TextView textDescription = (TextView) myView.findViewById(R.id.social_fragment_list_rowDescription);
			
			ListItem myItem = getItem(position);
			textTitle.setText(myItem.title);
			textDescription.setText(myItem.description);

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
		}
		
	}
}