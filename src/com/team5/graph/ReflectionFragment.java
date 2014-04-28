package com.team5.graph;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.team5.fragment.GraphFragment;
import com.team5.fragment.SeekBarFragment;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by DavidOyeku on 16/02/2014.
 */
public class ReflectionFragment extends Fragment implements OnClickListener {

	private View v;

	private int value;
	private String thought;
	List<UserRecord> points;
	List<UserRecord> newPoints;
	private long timeStamp;
	private TextView seriousness;
	private TextView reflectionText;


	private EditText seriousnessScore;
	private String line;
	private int whatAverageToCalc;
	private Button cancel;
	private Button moreDetails;
	private String[] descriptionValues;

	private EditText anxietyScore;

	public void setValue(float val, String line,String comment) {
		this.value = (int) val;
		this.line = line;
		this.thought = comment;

	}

	public void setThought(String thought) {
		this.thought = thought;
	}

	public void setTimeStamp(long time, int lines) {
		this.whatAverageToCalc = lines;
		this.timeStamp = time;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//get resources
		v = inflater.inflate(R.layout.reflection_fragment, container, false);
		seriousnessScore = (EditText) v
				.findViewById(R.id.seriousness_edit_text_ref);
		anxietyScore = (EditText) v.findViewById(R.id.anxiety_edit_text_ref);

		descriptionValues = getResources().getStringArray(
				R.array.x_axis_grid_week);
		seriousness = (TextView) v.findViewById(R.id.seriousness_text_view);
		reflectionText = (TextView) v.findViewById(R.id.reflectionText);
		reflectionText.setText(thought);
		moreDetails = (Button) v.findViewById(R.id.more_details_button);
		moreDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// if in the month do the following

				if (whatAverageToCalc == 1) {
					// make new date
					Date d = new Date();
					d.setTime(timeStamp);
					// make new calender
					Calendar cal = Calendar.getInstance();
					// set the current date
					cal.setTimeInMillis(d.getTime());
					// first day of the current week
					Calendar first = (Calendar) cal.clone();
					first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek()
							+ 1 - first.get(Calendar.DAY_OF_WEEK));
					// and add six days to the end date
					Calendar last = (Calendar) first.clone();
					last.add(Calendar.DAY_OF_YEAR, 6 + 1);
					d.setTime(first.getTimeInMillis());
					Date d2 = new Date();
					d2.setTime(last.getTimeInMillis());
					List<Date> dateForTheWeek = generateDateListBetween(d, d2);
					points = ((Session) getActivity().getApplication())
							.getUserAccount().getRecordByDayAverage(
									first.getTimeInMillis(),
									last.getTimeInMillis());
					newPoints = fillMissingDataByDay(dateForTheWeek, points);

					((HomeActivity) getActivity())
							.changeFragment(new GraphFragment().define(
									newPoints, 0));
				}
				// else if its in the year
				if (whatAverageToCalc == 2) {
					Calendar c7 = GregorianCalendar.getInstance();
					int min = Calendar.getInstance().getActualMinimum(
							Calendar.DAY_OF_MONTH);
					int max = Calendar.getInstance().getActualMaximum(
							Calendar.DAY_OF_MONTH);
					c7.set(Calendar.DAY_OF_MONTH, min);
					Calendar c8 = GregorianCalendar.getInstance();
					c8.set(Calendar.DAY_OF_MONTH, max - 1);
					points = ((Session) getActivity().getApplication())
							.getUserAccount().getRecordByMonthAverage(
									c7.getTimeInMillis(), c8.getTimeInMillis());
					((HomeActivity) getActivity())
							.changeFragment(new GraphFragment().define(points,
									1));

				}
			}
		});

		cancel = (Button) v.findViewById(R.id.ref_fragment_cancel_button);
		cancel.setOnClickListener(this);
	

		if (line.toLowerCase().equals("blue")) {
			anxietyScore.setText("" + value);
		}
		if (line.toLowerCase().equals("red")) {
			seriousnessScore.setText("" + value);
		}

		// description.setText(" ");

		return v;

	}

	public void setTextViewName(int i) {
		seriousness.setText(descriptionValues[i]);
	}

	@Override
	public void onClick(View v) {
		getFragmentManager().beginTransaction().remove(this).commit();

	}

	public List<Date> generateDateListBetween(Date startDate, Date endDate) {
		// Flip the input if necessary, to prevent infinite loop
		if (startDate.after(endDate)) {
			Date temp = startDate;
			startDate = endDate;
			endDate = temp;
		}

		List<Date> resultList = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		do {
			resultList.add(cal.getTime());
			cal.roll(Calendar.DAY_OF_MONTH, true); // Roll one day forwards
		} while (cal.getTime().before(endDate));

		return resultList;
	}

	public List<UserRecord> fillMissingDataByDay(List<Date> fullWeekDate,
			List<UserRecord> userRecord) {
		List<UserRecord> madeUpData = new ArrayList<UserRecord>();
		// loop thru the dates for the week
		for (int i = 0; i < fullWeekDate.size(); i++) {
			UserRecord use = new UserRecord();
			use.setTimestamp(fullWeekDate.get(i).getTime());
			madeUpData.add(use);
		}

		Date d = new Date();
		Date d2 = new Date();

		for (int i = 0; i < userRecord.size(); i++) {
			d.setTime(userRecord.get(i).getTimestamp());
			for (int j = 0; j < madeUpData.size(); j++) {
				d2.setTime(madeUpData.get(j).getTimestamp());
				if (d.getDay() == d2.getDay()) {
					madeUpData.set(j, userRecord.get(i));
				}
			}
		}
		return madeUpData;
	}

}
