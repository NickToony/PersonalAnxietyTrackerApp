package com.team5.contact;

import com.team5.pat.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactDetailsFragment extends Fragment {
	private TextView name;
	private TextView phone;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_group_details, container,
				false);
		
		// name = (TextView) view.findViewById(R.id.contactDetailsName);
		// phone = (TextView) view.findViewById(R.id.contactDetailsPhone);
		name.setText("Name: Milton");
		phone.setText("Mobile: 01234567891");
		
		return view;
	}
}
