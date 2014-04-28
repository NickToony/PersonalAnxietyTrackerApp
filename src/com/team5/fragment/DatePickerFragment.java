package com.team5.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

public class DatePickerFragment extends Fragment {
	private View myView;
	private HomeActivity myActivity;
	private Button from;
	private Button to;
	private Button ok;
	int [] dateArr = new int[3];
	int [] dateArr2 = new int[3];


	private DatePicker date;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.date_picker_layout, container, false);
		myActivity = (HomeActivity) getActivity();
		from=(Button) myView.findViewById(R.id.date_picker_from_button);
		to = (Button) myView.findViewById(R.id.date_picker_to_button);
		ok = (Button) myView.findViewById(R.id.date_picker_ok);
		date = (DatePicker) myView.findViewById(R.id.date_picker);
		
		from.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int day = date.getDayOfMonth();
				int month = date.getMonth();
				int year = date.getYear()-1900;
				dateArr[0] = day;
				dateArr[1] = month;
				dateArr[2] = year;
			}
		});
to.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int day = date.getDayOfMonth();
				int month = date.getMonth();
				int year = date.getYear()-1900;
				dateArr2[0] = day;
				dateArr2[1] = month;
				dateArr2[2] = year;
				
			}
		});
ok.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//((HomeActivity) getActivity())
		//.changeFragment(new AgendaFragment().dirtySet(dateArr, dateArr2));
	}
});
		
		return myView;
	}
}
