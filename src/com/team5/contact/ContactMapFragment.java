package com.team5.contact;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.R;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ContactMapFragment extends Fragment implements NetworkInterface,
		LocationListener {
	// Attributes for each contact
	private List<String> names = new ArrayList<String>();
	private List<Float> latitudes = new ArrayList<Float>();
	private List<Float> longitudes = new ArrayList<Float>();
	private List<String> addresses = new ArrayList<String>();

	private GoogleMap map;
	private LatLng position;
	private int size;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_map_fragment, container,
				false);
		new Request(this, "http://nick-hope.co.uk/PAT/android/contact.xml")
				.start();

		// Get a handle to the Map Fragment
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(true);

		return view;
	}

	@Override
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
					latitudes.add(Float.parseFloat(element
							.getElementsByTagName("latitude").item(0)
							.getTextContent()));
					longitudes.add(Float.parseFloat(element
							.getElementsByTagName("longitude").item(0)
							.getTextContent()));
					addresses.add(element.getElementsByTagName("address")
							.item(0).getTextContent());

					// add locations of contacts to the map
					position = new LatLng(latitudes.get(i), longitudes.get(i));
					map.addMarker(new MarkerOptions().title(names.get(i))
							.snippet(addresses.get(i)).position(position));
				}
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), "XML error", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		LatLng latLng = new LatLng(lat, lng);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

	}
}
