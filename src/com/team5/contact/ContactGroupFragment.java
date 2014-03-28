package com.team5.contact;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.R;

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
	private List<String> names = new ArrayList<String>();
	private List<String> phoneNumbers = new ArrayList<String>();
	private View view;
	private int size;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.contact_group_fragment, container,
				false);

		new Request(this, "contact.xml").start();

		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), ContactDetailsActivity.class);
		startActivity(intent);
	}

	private void addItemsToTheList() {
		items = new ArrayList<GroupItem>();

		TypedArray icons = getResources().obtainTypedArray(
				R.array.contact_icons);

		for (int i = 0; i < size; i++) {
			items.add(new GroupItem(icons.getResourceId(i, -1), names.get(i),
					phoneNumbers.get(i)));
		}

		icons.recycle();
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
			size = nodeList.getLength();

			// Extract text content from each tag
			for (int i = 0; i < size; i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					names.add(element.getElementsByTagName("name").item(0)
							.getTextContent());
					phoneNumbers.add(element.getElementsByTagName("phone")
							.item(0).getTextContent());
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
}