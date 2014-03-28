package com.team5.contact;

import java.util.List;

import com.team5.pat.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** Adapter for ContactGroupFragment that links layout of each row to a list **/
public class GroupAdapter extends ArrayAdapter<GroupItem> {
	private Context context;
	private int resource;
	private ImageView icon;
	private TextView name;
	private TextView phoneNumber;
	private List<GroupItem> items;

	public GroupAdapter(Context context, int resource, List<GroupItem> items) {
		super(context, resource, items);
		this.context = context;
		this.resource = resource;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(resource, parent, false);
		}

		icon = (ImageView) row.findViewById(R.id.contact_group_icon);
		name = (TextView) row.findViewById(R.id.contact_group_name);
		phoneNumber = (TextView) row.findViewById(R.id.contact_group_phone);

		icon.setImageResource(items.get(position).getIcon());
		name.setText(items.get(position).getName());
		phoneNumber.setText(items.get(position).getPhoneNumber());

		return row;
	}
}

/** Objects shown every row **/
class GroupItem {
	private int icon;
	private String name;
	private String phoneNumber;

	public GroupItem(int icon, String name, String phoneNumber) {
		this.icon = icon;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public int getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}
