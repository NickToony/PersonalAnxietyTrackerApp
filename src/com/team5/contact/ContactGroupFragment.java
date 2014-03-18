package com.team5.contact;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.team5.pat.R;
import com.team5.social.NetworkInterface;
import com.team5.social.Request;
import com.team5.social.Response;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ContactGroupFragment extends ListFragment implements
		NetworkInterface {
	private List<GroupItem> items;
	private String[] names = new String[10];
	private String[] phoneNumbers = new String[10];
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.contact_group_fragment, container,
				false);

		new Request(this, "http://193.35.58.219/PAT/android/contact.xml", "");		

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ContactDetailsFragment fragment = new ContactDetailsFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(this);
		ft.replace(android.R.id.content, fragment).commit();
	}

	@Override
	public void eventNetworkResponse(Request from, Response response) {
		// Exit if not connected to the network
		if (!response.isSuccess()) {
			Toast.makeText(getActivity(), "No internet", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Read all attributes from the XML file
		try {
			Document document = response.getDocument();
			NodeList nodeList = document.getElementsByTagName("contact");
			int size = nodeList.getLength();

			// Extract text content from each tag
			for (int i = 0; i < size; i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					names[i] = element.getElementsByTagName("name").item(0)
							.getTextContent();
					phoneNumbers[i] = element.getElementsByTagName("phone")
							.item(0).getTextContent();
				}
			}
			
			// Set up the list of contacts if records are read from server
			addItemsToTheList();
			
			GroupAdapter adapter = new GroupAdapter(getActivity(),
					R.layout.contact_group_list_item, items);
			setListAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), "XML error", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void addItemsToTheList() {
		items = new ArrayList<GroupItem>();

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
