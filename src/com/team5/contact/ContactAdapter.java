package com.team5.contact;

import java.util.List;

import com.team5.pat.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/** Adapter for ContactGroupFragment that links layout of each row to a list **/
public class ContactAdapter extends BaseAdapter {
	private Context context;
	private int resource;
	private ImageView icon;
	private TextView name;
	private TextView phoneNumber;
	private List<Contact> contacts;

	public ContactAdapter(Context context, int resource) {
		this.context = context;
		this.resource = resource;
		this.contacts = Contact.getAll(context);
		
		if (this.contacts == null)
			Toast.makeText(context, "Failed to read contacts", Toast.LENGTH_SHORT).show();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (contacts == null)
			return null;
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(resource, parent, false);
		}

		icon = (ImageView) row.findViewById(R.id.contact_group_icon);
		name = (TextView) row.findViewById(R.id.contact_group_name);
		phoneNumber = (TextView) row.findViewById(R.id.contact_group_phone);

		name.setText(contacts.get(position).name);
		phoneNumber.setText(contacts.get(position).phoneNumber);

		return row;
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
