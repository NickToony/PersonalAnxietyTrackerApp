package com.team5.navigationlist;

import java.util.ArrayList;
import java.util.List;

import com.team5.pat.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavListAdapter extends ArrayAdapter<NavListItem> {
	private Context context;
	private int resource;
	private List<NavListItem> items = new ArrayList<NavListItem>();
	
	private View myView;
	
	// Navigation Items
	private NavListItem[] navigationItems = new NavListItem[13];
	public final static int navigationHome = 0;
	public final static int navigationLog = 1;
	public final static int navigationTracker = 2;
	public final static int navigationExercises = 3;
	public final static int navigationDiscussion = 4;
	public final static int navigationAccount = 5;
	public final static int navigationContact = 6;
	public final static int navigationReport = 7;
	public final static int navigationLogOff = 8;
	public final static int navigationBrowse = 9;
	public final static int navigationCreate = 10;
	public final static int navigationFavourites = 11;
	public final static int navigationMine = 12;
	
	public NavListAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
		this.resource = resource;
		
		navigationItems[navigationHome] = new NavListItem(R.drawable.ic_log_off, R.string.navigation_home);
		navigationItems[navigationLog] = new NavListItem(R.drawable.ic_log, R.string.navigation_log);
		navigationItems[navigationTracker] = new NavListItem(R.drawable.ic_tracker, R.string.navigation_tracker);
		navigationItems[navigationExercises] = new NavListItem(R.drawable.ic_exercises, R.string.navigation_exercises);
		navigationItems[navigationDiscussion] = new NavListItem(R.drawable.ic_forums2, R.string.navigation_discussion);
		navigationItems[navigationAccount] = new NavListItem(R.drawable.ic_my_account, R.string.navigation_account);
		navigationItems[navigationContact] = new NavListItem(R.drawable.ic_find_help, R.string.navigation_contact);
		navigationItems[navigationReport] = new NavListItem(R.drawable.ic_report_issue, R.string.navigation_report);
		navigationItems[navigationLogOff] = new NavListItem(R.drawable.ic_log_off, R.string.navigation_logoff);
		navigationItems[navigationBrowse] = new NavListItem(R.drawable.ic_browseposts, R.string.navigation_browse);
		navigationItems[navigationCreate] = new NavListItem(R.drawable.ic_addpost, R.string.navigation_create);
		navigationItems[navigationFavourites] = new NavListItem(R.drawable.ic_favouriteposts, R.string.navigation_favourites);
		navigationItems[navigationMine] = new NavListItem(R.drawable.ic_myposts, R.string.navigation_mine);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		myView = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (myView == null) {
			myView = inflater.inflate(resource, parent, false);
		}

		ImageView icon = (ImageView) myView.findViewById(R.id.rowImage);
		TextView title = (TextView) myView.findViewById(R.id.rowTitle);
		//image.setImageResource(items.get(position).getImage());
		
		NavListItem myItem = getItem(position);
		icon.setImageDrawable(context.getResources().getDrawable( myItem.getImage() ) );
		title.setText(context.getResources().getString(myItem.getLabel() ));

		return myView;
	}
	
	@Override
	public int getCount()	{
		return items.size();
	}
	
	@Override
	public NavListItem getItem(int position)	{
		return items.get(position);
	}
	
	@Override
	public long getItemId(int position)	{
		for (int i = 0; i < navigationItems.length; i ++)	{
			if (navigationItems[i] == getItem(position))
				return i;
		}
		
		return -1;
	}
	
	public void addItem(int item)	{
		//items.add(new NavListItem(context.getResources().getDrawable( navArray[0] ), label));
		items.add(navigationItems[item]);
	}
}