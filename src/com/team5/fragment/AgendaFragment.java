package com.team5.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

public class AgendaFragment extends Fragment {
	private View myView;

	private HomeActivity myActivity;
	private EditText from;
	private Button setFrom,setTo,ok;
	private EditText to;
	private Date d1,d2;
	private SimpleDateFormat format;
	private DatePicker date;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.agenda_fragment, container, false);
		myActivity = (HomeActivity) getActivity();
		to = (EditText) myView.findViewById(R.id.to_date_picker_edit_text);
		date = (DatePicker) myView.findViewById(R.id.dpResult);
		d1 = new Date();
		d2 = new Date();
		setFrom = (Button) myView.findViewById(R.id.from_button_set);
		setTo = (Button) myView.findViewById(R.id.to_button_set);
		ok = (Button) myView.findViewById(R.id.ok);
		from = (EditText) myView.findViewById(R.id.from_date_picker_edit_text);
		format = new SimpleDateFormat("dd-MM-yyyy");
		setFrom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				d1.setDate(date.getDayOfMonth());
				d1.setMonth(date.getMonth());
				d1.setYear(date.getYear()-1900);
				from.setHint(format.format(d1));

			}
		});

		setTo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d2.setDate(date.getDayOfMonth());
				d2.setMonth(date.getMonth());
				d2.setYear(date.getYear()-1900);
				to.setHint(format.format(d2));
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//get the results from the database
				List<UserRecord>points = ((Session) getActivity().getApplication())
						.getUserAccount().getRecordByTime(
								d1.getTime(), d2.getTime());
				
				((HomeActivity) getActivity())
				//passing the data across to this class
				.changeFragment(new UserRecordList().passDataAcross(points));
				
			}
		});

		return myView;
	}

	// public Fragment dirtySet(int [] dateArr,int[] dateArr2) {
	// passDateFrom = dateArr;
	// passDateTo = dateArr2;
	// return this;
	// }
}
