package com.team5.navigationlist;

import java.util.ArrayList;
import java.util.List;

import com.team5.pat.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavListAdapter extends ArrayAdapter<NavListItem> {
	private Context context;
	private int resource;
	private ImageView icon;
	private TextView title;
	private List<NavListItem> items = new ArrayList<NavListItem>();
	
	private View myView;

	public NavListAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		myView = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (myView == null) {
			myView = inflater.inflate(resource, parent, false);
		}

		icon = (ImageView) myView.findViewById(R.id.rowImage);
		title = (TextView) myView.findViewById(R.id.rowTitle);
		//image.setImageResource(items.get(position).getImage());
		icon.setImageDrawable(items.get(position).getImage());
		title.setText(items.get(position).getLabel());

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
		return position;
	}
	
	public void addItem(int drawable, String label)	{
		items.add(new NavListItem(context.getResources().getDrawable( drawable ), label));
	}
}