package com.team5.fragment;

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
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.team5.dialog.ScrollableDialog;
import com.team5.graph.LineGraphView;
import com.team5.graph.ReflectionFragment;
import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GraphFragment extends Fragment implements OnTouchListener {
	private LineGraphView lineGraph;
	private CheckBox seriousness;
	private CheckBox anxiety;
	private CheckBox ref_anxiety;
	private CheckBox ref_seriousness;
	private final int month = 7;
	private String[] titles;

	private View myView;
	// private HomeActivity myActivity;
	private ActionBar actionBar;
	private TextView xAxisTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.graph_layout, container, false);
		getActivity().setTitle(
				getResources().getString(R.string.navigation_tracker));
		xAxisTitle = (TextView) myView.findViewById(R.id.x_axis_title);

		SpinnerAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.spinner_list_items, R.layout.spinner_list);
		// The same word as SpinerAdapter
		titles = getResources().getStringArray(R.array.spinner_list_items);

		// Action bar settings
		actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(adapter, new DropDownListener());

		initialiseGraphs();
		addPointsToGraphs();

		// Setting up checkBox and adding listener to them!
		lineGraph.setOnTouchListener(this);
		lineGraph.setVisibility(View.VISIBLE);

		return myView;
	}

	/**
	 * Remove drop down list when this fragment is removed *
	 */
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
			userTouch.setAnxiety(event.getX());
			userTouch.setSeriousness(event.getY());

			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			for (UserRecord p : lineGraph.getLineGraphPointCordinates()) {
				int distance = userTouch.getTolerance(p);

				// If a point is touched in the graph, either pop up a box
				// showing the related figures or navigate to SeekBarFragment
				if (distance < lineGraph.getTouchTolerance()) {

					if (p.getRecorded()) {
						ReflectionFragment fragment = new ReflectionFragment();
						fragment.setSer(p.getSeriousness());
						fragment.setAnx(p.getAnxiety());
						fragment.setThought(p.getComments());

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

			titles = getResources().getStringArray(R.array.x_axis_grid_week);
			List<UserRecord> points;
			int mRedLine;
			int mBlueLine;

			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			switch (position) {
			case 0:
				lineGraph.setGridSpace(7, position);
				xAxisTitle.setText(R.string.x_axis_title_days);
				// make new date
				Date d = new Date();
				// make new calender
				Calendar cal = Calendar.getInstance();
				// set the current date
				cal.setTimeInMillis(d.getTime());
				// first day of the current week
				Calendar first = (Calendar) cal.clone();
				first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() + 1
						- first.get(Calendar.DAY_OF_WEEK));
				// and add six days to the end date
				Calendar last = (Calendar) first.clone();
				last.add(Calendar.DAY_OF_YEAR, 6);
				lineGraph.clearRecords();
				points = ((Session) getActivity().getApplication())
						.getUserAccount()
						.getRecordByDayAverage(first.getTimeInMillis(),
								last.getTimeInMillis());
				mRedLine = lineGraph.addLine(Color.RED);
				mBlueLine = lineGraph.addLine(Color.BLUE);

				for (int i = 0; i < points.size(); i++) {
					// add to graph
					lineGraph.addPoint(mBlueLine, i,
							points.get(i).getAnxiety(), "Random string", points
									.get(i).getTimestamp());
					lineGraph.addPoint(mRedLine, i, points.get(i)
							.getSeriousness(), "Random string", points.get(i)
							.getTimestamp());
				}

				lineGraph.invalidate();
				return true;
			case 1:
				Calendar c7 = GregorianCalendar.getInstance();
				c7.set(Calendar.DAY_OF_MONTH, 1);
				lineGraph.clearRecords();
				lineGraph.setGridSpace(5, position);
				xAxisTitle.setText(R.string.x_axis_title_month);

				Calendar c8 = GregorianCalendar.getInstance();
				c8.set(Calendar.DAY_OF_MONTH, 30);
				points = ((Session) getActivity().getApplication())
						.getUserAccount().getRecordByMonthAverage(
								c7.getTimeInMillis(), c8.getTimeInMillis());

				mRedLine = lineGraph.addLine(Color.RED);
				mBlueLine = lineGraph.addLine(Color.BLUE);

				for (int i = 0; i < points.size(); i++) {
					// add to graph
					lineGraph.addPoint(mBlueLine, i,
							points.get(i).getAnxiety(), "Random string", points
									.get(i).getTimestamp());
					lineGraph.addPoint(mRedLine, i, points.get(i)
							.getSeriousness(), "Random string", points.get(i)
							.getTimestamp());
				}

				lineGraph.invalidate();

				return true;
			case 2:
				Calendar c = GregorianCalendar.getInstance();
				c.set(Calendar.YEAR, 2014);
				c.set(Calendar.MONTH, Calendar.JANUARY);
				c.set(Calendar.DATE, 1);

				Calendar c2 = GregorianCalendar.getInstance();
				c2.set(Calendar.YEAR, 2014);
				c2.set(Calendar.MONTH, Calendar.DECEMBER);
				c2.set(Calendar.DATE, 31);
				points = ((Session) getActivity().getApplication())
						.getUserAccount().getRecordByYearAverage(
								c.getTimeInMillis(), c2.getTimeInMillis());
				lineGraph.clearRecords();
				mRedLine = lineGraph.addLine(Color.RED);
				mBlueLine = lineGraph.addLine(Color.BLUE);

				for (int i = 0; i < points.size(); i++) {
					// add to graph
					lineGraph.addPoint(mBlueLine, i,
							points.get(i).getAnxiety(), "Random string", points
									.get(i).getTimestamp());
					lineGraph.addPoint(mRedLine, i, points.get(i)
							.getSeriousness(), "Random string", points.get(i)
							.getTimestamp());
				}
				lineGraph.invalidate();

				lineGraph.setGridSpace(12, position);
				xAxisTitle.setText(R.string.x_axis_title_year);

				lineGraph.invalidate();
				return true;
			case 3:
				// DatePickerFragment fragment = new DatePickerFragment();
				ScrollableDialog fragment = new ScrollableDialog();
				transaction.add(android.R.id.content, fragment);
				transaction.commit();
				Toast.makeText(getActivity(), "heey", Toast.LENGTH_SHORT)
						.show();

				return true;

			default:
				return false;
			}
		}
	}

	private void initialiseGraphs() {
		lineGraph = (LineGraphView) myView.findViewById(R.id.myLineGraph);
		lineGraph.setGridSpace(7, 0);

	}

	private void addPointsToGraphs() {
		addPointsToLineGraph();

	}

	private void addPointsToLineGraph() {

		//
		// Calendar cal1=Calendar.getInstance();
		// Calendar cal2=Calendar.getInstance();
		// cal1.set(2014,Calendar.MARCH,1);
		// cal1.set(2014,Calendar.MARCH,31);

		// make new date
		Date d = new Date();
		// make new calender
		Calendar cal = Calendar.getInstance();
		// set the current date
		cal.setTimeInMillis(d.getTime());
		// first day of the current week
		Calendar first = (Calendar) cal.clone();
		first.add(Calendar.DAY_OF_WEEK,
				first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
		// and add six days to the end date
		Calendar last = (Calendar) first.clone();
		last.add(Calendar.DAY_OF_WEEK, 7);

		Calendar c7 = GregorianCalendar.getInstance();
		c7.set(Calendar.DAY_OF_MONTH, 1);

		Calendar c8 = GregorianCalendar.getInstance();
		c8.set(Calendar.DAY_OF_MONTH, 30);

		List<UserRecord> points = ((Session) getActivity().getApplication())
				.getUserAccount().getRecordByDayAverage(
						first.getTimeInMillis(), last.getTimeInMillis());

		int mRedLine = lineGraph.addLine(Color.RED);
		int mBlueLine = lineGraph.addLine(Color.BLUE);

		for (int i = 0; i < points.size(); i++) {
			// add to graph
			lineGraph.addPoint(mBlueLine, i, points.get(i).getAnxiety(),
					"Random string", points.get(i).getTimestamp());
			lineGraph.addPoint(mRedLine, i, points.get(i).getSeriousness(),
					"Random string", points.get(i).getTimestamp());
		}
		// lineGraph.fillMissingData();

	}

}