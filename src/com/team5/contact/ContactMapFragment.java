package com.team5.contact;

import com.team5.fragment.LocationFragment;
import com.team5.pat.R;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactMapFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_map_fragment, container,
				false);

		Fragment fragment = new LocationFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.ContactMapFrame, fragment).commit();

		return view;
	}
}
