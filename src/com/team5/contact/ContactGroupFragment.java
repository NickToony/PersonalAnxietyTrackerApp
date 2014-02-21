package com.team5.contact;

import java.util.ArrayList;
import java.util.List;

import com.team5.pat.R;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ContactGroupFragment extends ListFragment {
	private List<GroupItem> items;
	private String[] names;
	private String[] phoneNumbers;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.contact_group_fragment, container,
				false);

		addItemsToTheList();

		GroupAdapter adapter = new GroupAdapter(getActivity(),
				R.layout.contact_group_list_item, items);
		setListAdapter(adapter);

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Intent intent = new Intent(getActivity(), ContactDetailsActivity.class);
		// startActivity(intent);
	}

	private void addItemsToTheList() {
		items = new ArrayList<GroupItem>();

		names = getResources().getStringArray(R.array.contact_name);
		phoneNumbers = getResources().getStringArray(
				R.array.contact_phone_number);
		TypedArray icons = getResources().obtainTypedArray(
				R.array.contact_icons);

		int size = names.length;
		for (int i = 0; i < size; i++) {
			items.add(new GroupItem(icons.getResourceId(i, -1), names[i],
					phoneNumbers[i]));
		}

		icons.recycle();
	}
}
