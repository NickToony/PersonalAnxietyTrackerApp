package com.team5.navigationlist;

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
	private List<NavListItem> items;

	public NavListAdapter(Context context, int resource, List<NavListItem> items) {
		super(context, resource, items);
		this.context = context;
		this.resource = resource;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (row == null) {
			row = inflater.inflate(resource, parent, false);

		}

		icon = (ImageView) row.findViewById(R.id.rowImage);
		title = (TextView) row.findViewById(R.id.rowTitle);
		//image.setImageResource(items.get(position).getImage());
		icon.setImageDrawable(items.get(position).getImage());
		title.setText(items.get(position).getTitle());

		return row;

	}
}