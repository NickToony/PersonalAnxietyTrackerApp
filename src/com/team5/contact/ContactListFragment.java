package com.team5.contact;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Complete rewrite by Nick
 * Creates a list of contacts to show
 * @author Milton, Nick
 *
 */
public class ContactListFragment extends Fragment { //implements NetworkInterface {
	private View view;
	private ContactAdapter myAdapter;
	private HomeActivity myActivity;
	private ListView myListView;

	
	/**
	 * Creates the view and handles the adapter to display contacts
	 * @author Nick
	 *
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle(getResources().getString(R.string.navigation_contact));
		
		view = inflater.inflate(R.layout.contact_group_fragment, container,
				false);
		
		//new Request(this, "contact.xml").start();
		myAdapter = new ContactAdapter(myActivity, R.layout.contact_group_list_item);
		myListView = (ListView) view.findViewById(R.id.contact_group_fragment_list);
		myListView.setAdapter(myAdapter);
		myListView.setOnItemClickListener(new OnItemClickListener()	{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,long id) {
				myActivity.changeFragment(new ContactDetailsFragment().definePosition(position));
			}
		});
		
		

		return view;
	}

	/*@Override
	public void eventNetworkResponse(Request from, Response response) {
		// Exit if not connected to the network
		if (!response.isSuccess()) {
			Toast.makeText(getActivity(), "Disconnected from server",
					Toast.LENGTH_SHORT).show();
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
	}*/
}