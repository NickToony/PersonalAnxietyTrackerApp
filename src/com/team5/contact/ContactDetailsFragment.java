package com.team5.contact;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Displays the specific details of a contact.
 * 
 * Rewritten by Nick so it was functional.
 * @author Milton, Nick
 *
 */
public class ContactDetailsFragment extends Fragment {
	private TextView nameView;
	private TextView phoneView;
	private TextView emailView;
	private TextView addressView;
	private TextView postcodeView;

	private String name;
	private String phone;
	private String email;
	private String address;
	private String postcode;
	
	private View myView;
	
	int position = 0;
	Contact myContact;
	private HomeActivity myActivity;
	
	/**
	 * Create the view and output the data into it
	 * @author Milton
	 *
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle(getResources().getString(R.string.navigation_contact));
		
		if (myView == null)
			myView = inflater.inflate(R.layout.contact_group_details, container,false);
		
		if (savedInstanceState != null)	{
			this.position = savedInstanceState.getInt("position");
		}
		
		myContact = Contact.getOne(myActivity, position);
		
		nameView = (TextView) myView.findViewById(R.id.contact_details_name);
		phoneView = (TextView) myView.findViewById(R.id.contact_details_phone);
		emailView = (TextView) myView.findViewById(R.id.contact_details_email);
		addressView = (TextView) myView.findViewById(R.id.contact_details_address);
		postcodeView = (TextView) myView.findViewById(R.id.contact_details_postcode);
		ImageView iconView = (ImageView) myView.findViewById(R.id.contact_group_details_image);

		nameView.setText("Name: " + myContact.name);
		phoneView.setText("Phone: " + myContact.phoneNumber);
		emailView.setText("Email: " + myContact.email);
		addressView.setText("Address: " + myContact.address);
		postcodeView.setText("Post Code: " + myContact.postCode);
		int id = myActivity.getResources().getIdentifier(myContact.iconName, "drawable", myActivity.getPackageName());
		iconView.setImageResource(id);
		
		((Button) myView.findViewById(R.id.contact_group_details_button)).setOnClickListener(new OnClickListener()	{
			@Override
			public void onClick(View v) {
				myActivity.changeFragment(new ContactMapFragment().definePosition(position));				
			}
		});
		
		return myView;
	}
	
	/**
	 * Allows another fragment to define which contact to display
	 * @author Nick
	 *
	 */
	public Fragment definePosition(int position)	{
		this.position = position;
		return this;
	}
	
	/**
	 * Allows the fragment to be restored with the same values
	 * @author Nick
	 *
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    outState.putInt("position", position);
	    
	    super.onSaveInstanceState(outState);
	}
}
