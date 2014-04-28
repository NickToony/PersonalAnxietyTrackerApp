package com.team5.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.team5.graph.LineGraphView;
import com.team5.graph.ReflectionFragment;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

public class GraphFragment extends Fragment implements OnTouchListener {
	private LineGraphView lineGraph;
	private CheckBox seriousness;
	private CheckBox anxiety;
	private Date d1, d2;
	final int blueLine = 0;
	final int redLine = 1;
	private String[] titles;
	private int amountOfLines = 5;
	private List<UserRecord> fromReflectionList;
	private int dropDownPosition = 0;
	private int classDropDownPos = 0;
	private View myView;
	private HomeActivity myActivity;
	private ActionBar actionBar;
	private TextView xAxisTitle;
	private List<UserRecord> reflectionPoints;

	public GraphFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.graph_layout, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Tracker");
		xAxisTitle = (TextView) myView.findViewById(R.id.x_axis_title);

		SpinnerAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.spinner_list_items, R.layout.spinner_list);

		titles = getResources().getStringArray(R.array.spinner_list_items);

		// Action bar settings
		actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(adapter, new DropDownListener());
		actionBar.setSelectedNavigationItem(dropDownPosition);

		initialiseGraphs();
		// addPointsToLineGraph();

		// Setting up checkBox and adding listener to them!
		lineGraph.setOnTouchListener(this);
		lineGraph.setVisibility(View.VISIBLE);

		return myView;
	}

	/**
	 * Remove drop down list when this fragment is removed *
	 **/
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		// Create point object to store coordinates
		UserRecord userTouch = new UserRecord();
		switch (event.getAction()) {
		// when the user presses down
		case MotionEvent.ACTION_DOWN: {
			// stores the co-ordinates that they
			userTouch.setX(event.getX());
			userTouch.setY(event.getY());

			List<UserRecord> point = lineGraph.getSeriousCordinates();
			point.addAll(lineGraph.getAnxietyCordinates());
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			for (UserRecord p : point) {
				int distance = userTouch.getTolerance(p);

				// If a point is touched in the graph, either pop up a box
				// showing the related figures or navigate to SeekBarFragment
				if (distance < lineGraph.getTouchTolerance()) {

					if (p.getRecorded()) {
						ReflectionFragment fragment = new ReflectionFragment();
						if (p.getColor() == Color.BLUE) {
							fragment.setValue(p.getAnxiety(), "blue",p.getComments());

						} else {
							fragment.setValue(p.getSeriousness(), "red",p.getComments());

						}
						Date d1 = new Date();
						d1.setTime(p.getTimestamp());
						SimpleDateFormat form = new SimpleDateFormat(
								"EEE MMM d ''yy");
						Toast.makeText(getActivity(),"Recorded on: "+ form.format(d1),
								Toast.LENGTH_SHORT).show();
						// fragment.setAnx(p.getAnxiety());
						fragment.setThought(p.getComments());
						// get the time and the amount of lines to determine
						// if it is in week, month or year
						fragment.setTimeStamp(p.getTimestamp(),
								classDropDownPos);

						transaction.add(android.R.id.content, fragment);
						transaction.commit();
						break;
					} else {
						// Allow user to navigate backward
						transaction.addToBackStack(null);
						transaction.replace(R.id.content_frame,
								new SeekBarFragment()).commit();
						break;
					}
				}
			}
		}
		}

		return true;
	}

	class DropDownListener implements OnNavigationListener {

		@Override
		public boolean onNavigationItemSelected(int position, long id) {

			List<UserRecord> points;
			int mRedLine;
			int mBlueLine;
			classDropDownPos = position;

			switch (position) {
			case 0:
				//sets the amount of lines which is 7 cause of 7 days a week
				//gives the x axis the labels
				//generate 2 dates the first is the first day of this week and the 2nd
				//is last day of this week
				
				lineGraph.setGridSpace(7, position);
				xAxisTitle.setText(R.string.x_axis_title_days);
				Date d[] = generateFirstAndLastDayOfWeek();
				List<Date> dateForTheWeek = lineGraph.generateDateListBetween(
						d[0], d[1]);
				points = ((Session) getActivity().getApplication())
						.getUserAccount().getRecordByDayAverage(d[0].getTime(),
								d[1].getTime());
				//fill in missing data
				List<UserRecord> newPoints = fillMissingDataByDay(
						dateForTheWeek, points);
				if (reflectionPoints != null) {
					newPoints = reflectionPoints;
				}
				//clear any existing records
				lineGraph.clearAnxietyPoints();
				lineGraph.clearSeriousnessPoints();
				lineGraph.clearRecords();
				mRedLine = lineGraph.addLine(Color.RED);
				mBlueLine = lineGraph.addLine(Color.BLUE);
				//add the lines for graph to render
				for (int i = 0; i < newPoints.size(); i++) {
					// add to graph
					lineGraph.addPoint(mBlueLine, i, newPoints.get(i)
							.getAnxiety(), "Random string", newPoints.get(i)
							.getTimestamp());
					lineGraph.addPoint(mRedLine, i, newPoints.get(i)
							.getSeriousness(), "Random string", newPoints
							.get(i).getTimestamp());
				}
				//where the graph should start drawing from
				//this is useful for making the lines disappear
				lineGraph.setOnDrawStart(0);
				lineGraph.setOnDrawStop(lineGraph.getLineDataSize());
				lineGraph.invalidate();
				reflectionPoints = null;

				return true;
			case 1:
				lineGraph.clearAnxietyPoints();
				lineGraph.clearRecords();
				lineGraph.clearSeriousnessPoints();
				//generate first and last day of month and fetch data from database
				//using this values
				Calendar c7 = GregorianCalendar.getInstance();
				int min = Calendar.getInstance().getActualMinimum(
						Calendar.DAY_OF_MONTH);
				int max = Calendar.getInstance().getActualMaximum(
						Calendar.DAY_OF_MONTH);
				c7.set(Calendar.DAY_OF_MONTH, min);
				//set the amount of lines for the month graph
				//4 weeks in a month so 4 lines
				lineGraph.setGridSpace(4, position);
				//get the titles for the x axis
				xAxisTitle.setText(R.string.x_axis_title_month);

				Calendar c8 = GregorianCalendar.getInstance();

				c8.set(Calendar.DAY_OF_MONTH, max -1);
				points = ((Session) getActivity().getApplication())
						.getUserAccount().getRecordByMonthAverage(
								c7.getTimeInMillis(), c8.getTimeInMillis());
				//fill in the missing data
				List<Date> weekForTheMonth = lineGraph.generateWeeksForMonth(
						c7.getTime(), c8.getTime());
				List<UserRecord> newPointsWeek = fillMissingDataByWeek(
						weekForTheMonth, points);

				if (reflectionPoints != null) {

					points = reflectionPoints;

				}

				mRedLine = lineGraph.addLine(Color.RED);
				mBlueLine = lineGraph.addLine(Color.BLUE);

				for (int i = 0; i < newPointsWeek.size(); i++) {
					// add to graph
					lineGraph.addPoint(mBlueLine, i, newPointsWeek.get(i)
							.getAnxiety(), "Random string", newPointsWeek
							.get(i).getTimestamp());
					lineGraph.addPoint(mRedLine, i, newPointsWeek.get(i)
							.getSeriousness(), "Random string", newPointsWeek
							.get(i).getTimestamp());
				}
				//where to start drawing from
				int size = lineGraph.getLineDataSize();
				lineGraph.setOnDrawStart(size - size);
				lineGraph.setOnDrawStop(size);

				lineGraph.invalidate();

				return true;
			case 2:
				//grab data from 1st day of year to last day of year
				Calendar c = GregorianCalendar.getInstance();
				c.set(Calendar.MONTH, Calendar.JANUARY);
				c.set(Calendar.DATE, 1);

				Calendar c2 = GregorianCalendar.getInstance();
				c2.set(Calendar.MONTH, Calendar.MAY);
				c2.set(Calendar.DATE, 31);
				//fetch from database
				points = ((Session) getActivity().getApplication())
						.getUserAccount().getRecordByYearAverage(
								c.getTimeInMillis(), c2.getTimeInMillis());
				List<Date> months = lineGraph.generateMonthBetween(c.getTime(),
						c2.getTime());
				//fill missing data
				List<UserRecord> generatedPoint = fillMissingDataByYear(months,
						points);
				//clear all records
				lineGraph.clearAnxietyPoints();
				lineGraph.clearSeriousnessPoints();
				lineGraph.clearRecords();
				mRedLine = lineGraph.addLine(Color.RED);
				mBlueLine = lineGraph.addLine(Color.BLUE);

				for (int i = 0; i < generatedPoint.size(); i++) {
					// add to graph
					lineGraph.addPoint(mBlueLine, i, generatedPoint.get(i)
							.getAnxiety(), "Random string",
							generatedPoint.get(i).getTimestamp());
					lineGraph.addPoint(mRedLine, i, generatedPoint.get(i)
							.getSeriousness(), "Random string", generatedPoint
							.get(i).getTimestamp());
				}
				lineGraph.invalidate();

				lineGraph.setGridSpace(12, position);
				xAxisTitle.setText(R.string.x_axis_title_year);

				lineGraph.invalidate();
				return true;
			case 3:
				//Go to agenda fragment to find all the data you have recorf
				((HomeActivity) getActivity())
						.changeFragment(new AgendaFragment());
				return true;
			case 4:
				// take the user to the sliders page
				((HomeActivity) getActivity())
						.changeFragment(new SeekBarFragment());
			default:
				return false;
			}

		}
	}

	public Date[] generateFirstAndLastDayOfWeek() {
		lineGraph.clearAnxietyPoints();
		lineGraph.clearSeriousnessPoints();
		lineGraph.clearRecords();
		Date d = new Date();
		// make new calender
		Calendar cal = Calendar.getInstance();
		// set the current date
		cal.setTimeInMillis(d.getTime());
		// first day of the current week
		Calendar first = (Calendar) cal.clone();
		first.add(Calendar.DAY_OF_WEEK,
				first.getFirstDayOfWeek() + 1 - first.get(Calendar.DAY_OF_WEEK));
		// and add six days to the end date
		Calendar last = (Calendar) first.clone();
		last.add(Calendar.DAY_OF_YEAR, 6 + 1);
		d.setTime(first.getTimeInMillis());
		Date d2 = new Date();
		d2.setTime(last.getTimeInMillis());
		Date[] dates = { d, d2 };
		return dates;
	}

	private void initialiseGraphs() {
		lineGraph = (LineGraphView) myView.findViewById(R.id.myLineGraph);

		anxiety = (CheckBox) myView.findViewById(R.id.AnxietyCheckBox);

		seriousness = (CheckBox) myView.findViewById(R.id.seriousnessCheckBox);
		// Temporary list to store the line points for the red and blue line
		// clearing them and re-adding them creates illusion of appearing and
		// disappearing

		// setting the click listener on the checkbox
		anxiety.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if anxiety is unchecked remove it and draw only the
				// seriousness
				if (!anxiety.isChecked() && seriousness.isChecked()) {
					lineGraph.setOnDrawStart(blueLine);
					lineGraph.setOnDrawStop(lineGraph.getLineDataSize() - 1);
					lineGraph.invalidate();
				}
				// if both anxiety and seriousness is undrawn then draw
				// no line on the graph
				if (!anxiety.isChecked() && !seriousness.isChecked()) {
					lineGraph.setOnDrawStart(lineGraph.getLineDataSize());
					lineGraph.setOnDrawStop(lineGraph.getLineDataSize());
					lineGraph.invalidate();
				}
				// if anxiety is check and seriousness isnt
				// draw only the anxiety

				if (anxiety.isChecked() && !seriousness.isChecked()) {
					lineGraph.setOnDrawStart(redLine);
					lineGraph.setOnDrawStop(lineGraph.getLineDataSize());
					lineGraph.invalidate();
				}
				if (anxiety.isChecked() && seriousness.isChecked()) {
					lineGraph.setOnDrawStart(blueLine);
					lineGraph.setOnDrawStop(lineGraph.getLineDataSize());
					lineGraph.invalidate();
				}
			}

		});

		seriousness.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if seriousness is not checked and anxiety is
				// draw only the anxiety
				if (!seriousness.isChecked() && anxiety.isChecked()) {
					lineGraph.setOnDrawStart(redLine);
					lineGraph.setOnDrawStop(lineGraph.getLineDataSize());
					lineGraph.invalidate();
				}
				// if both seriousness and anxiety isnt checked
				// draw nothing on the graph
				if (!seriousness.isChecked() && !anxiety.isChecked()) {
					lineGraph.setOnDrawStart(lineGraph.getLineDataSize());
					lineGraph.setOnDrawStop(lineGraph.getLineDataSize());
					lineGraph.invalidate();
					Toast.makeText(getActivity(), "seriousness: am checked",
							Toast.LENGTH_SHORT).show();
				}
				// if seriousness is checked and anxiety isn't
				// draw only the seriousness line
				if (!anxiety.isChecked() && seriousness.isChecked()) {
					lineGraph.setOnDrawStart(blueLine);
					lineGraph.setOnDrawStop(lineGraph.getLineDataSize() - 1);
					lineGraph.invalidate();
				}
				if (anxiety.isChecked() && seriousness.isChecked()) {
					lineGraph.setOnDrawStart(blueLine);
					lineGraph.setOnDrawStop(lineGraph.getLineDataSize());
					lineGraph.invalidate();
				}
			}
		});
	}

	// fills in the missing dates from the database with 0 anxiety and 0
	// seriousness
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

	public List<UserRecord> fillMissingDataByWeek(List<Date> week,
			List<UserRecord> userRecord) {
		List<UserRecord> genRecord = new ArrayList<UserRecord>();
		UserRecord genUser;
		for (int i = 0; i < week.size(); i++) {
			genUser = new UserRecord();

			genUser.setTimestamp(week.get(i).getTime());
			Date my = new Date();
			my.setTime(week.get(i).getTime());
			genRecord.add(genUser);
		}
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		for (int i = 0; i < userRecord.size(); i++) {
			c1.setTimeInMillis(userRecord.get(i).getTimestamp());
			for (int j = 0; j < week.size(); j++) {
				c2.setTimeInMillis(genRecord.get(j).getTimestamp());
				if (c1.get(Calendar.WEEK_OF_YEAR) == c2
						.get(Calendar.WEEK_OF_YEAR)) {
					genRecord.set(j, userRecord.get(i));

				}
			}
		}

		return genRecord;
	}

	public List<UserRecord> fillMissingDataByYear(List<Date> listOfMonth,
			List<UserRecord> dataBaseRecord) {
		// return the filled in data
		List<UserRecord> genRecord = new ArrayList<UserRecord>();
		// generate user data from the pre gen month
		UserRecord genUser;

		for (int i = 0; i < listOfMonth.size(); i++) {
			genUser = new UserRecord();

			genUser.setTimestamp(listOfMonth.get(i).getTime());
			Date my = new Date();
			my.setTime(listOfMonth.get(i).getTime());
			genRecord.add(genUser);
		}
		Date d1 = new Date();
		Date d2 = new Date();
		for (int i = 0; i < dataBaseRecord.size(); i++) {
			d1.setTime(dataBaseRecord.get(i).getTimestamp());
			for (int j = 0; j < genRecord.size(); j++) {
				d2.setTime(genRecord.get(j).getTimestamp());
				if (d1.getMonth() == d2.getMonth()) {
					genRecord.set(j, dataBaseRecord.get(i));
				}
			}
		}
		return genRecord;
	}

	public Fragment define(List<UserRecord> points, int lines) {
		dropDownPosition = lines;
		reflectionPoints = points;
		return this;
	}

}