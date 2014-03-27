package com.team5.contact;

import com.team5.pat.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

public class ContactDetailsActivity extends Activity {
	private ActionBar actionBar;
	private ViewPager viewPager;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_group_details);
		actionBar = getActionBar();

		nameView = (TextView) findViewById(R.id.contact_details_name);
		phoneView = (TextView) findViewById(R.id.contact_details_phone);
		emailView = (TextView) findViewById(R.id.contact_details_email);
		addressView = (TextView) findViewById(R.id.contact_details_address);
		postcodeView = (TextView) findViewById(R.id.contact_details_postcode);

		nameView.setText("Name: Milton");
		phoneView.setText("Mobile: 01234567891");
		emailView.setText("Email");
		addressView.setText("address");
		postcodeView.setText("postcode");

		// FragmentTransaction ft = getFragmentManager().beginTransaction();
		// ft.replace(android.R.id.content, new
		// ContactDetailsFragment()).commit();
	}
}