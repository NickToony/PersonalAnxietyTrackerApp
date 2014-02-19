package com.team5.fragment;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ActionBar.OnNavigationListener;
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

import com.team5.graph.BarChartView;
import com.team5.graph.LineGraphView;
import com.team5.graph.PieChartView;
import com.team5.graph.Point;
import com.team5.graph.ReflectionFragment;
import com.team5.pat.R;

public class GraphFragment extends Fragment implements OnTouchListener {
	private LineGraphView lineGraph;
	private PieChartView pieChart;
	private BarChartView barChart;
	private CheckBox seriousness;
	private CheckBox anxiety;
	private View view;
	private CheckBox ref_anxiety;
	private CheckBox ref_seriousness;
	private Random randNumbers = new Random();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.graph_layout, container, false);

		SpinnerAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.spinner_list_items, R.layout.spinner_list);

		// Action bar settings
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(adapter, new DropDownListener());

		initialiseGraphs();
		addPointsToGraphs();
		setGraphsInvisible();

		// Setting up checkBox and adding listener to them!
		initializeAndAddListnerCheckBoxes();
		lineGraph.setOnTouchListener(this);
		lineGraph.setVisibility(View.VISIBLE);

		return view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// int scaleDownX = 90;
		// int scaleDownY = 279;

		// Create point object to store coordinates
		Point userTouch = new Point();
		switch (event.getAction()) {
		// when the user presses down
		case MotionEvent.ACTION_DOWN: {// stores the co-ordinates that they
										// press down on
			userTouch.setX((float) event.getX());
			userTouch.setY((float) event.getY());

			// lineGraph.getLocationOnScreen();
			// x90, y279

			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			for (Point p : lineGraph.getLineGraphPointCordinates()) {
				int distance = userTouch.getTolerance(p);

				// If a point is touched in the graph, either pop up a box
				// showing the related figures or navigate to SeekBarFragment
				if (distance < lineGraph.getTouchTolerance()) {
					if (p.getRecorded()) {
						ReflectionFragment frag = new ReflectionFragment();
						frag.setSer(p.getX());
						frag.setAnx(p.getY());
						frag.setThought(p.getInformation());
						transaction.add(android.R.id.content, frag).commit();
						break;
					} else {
						transaction.remove(this);
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
		// The same word as SpinerAdapter
		String[] titles = getResources().getStringArray(
				R.array.spinner_list_items);

		@Override
		public boolean onNavigationItemSelected(int position, long id) {
			switch (position) {
			case 0:
				return true;
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				return true;
			case 4:
				return true;
			default:
				return false;
			}
		}
	}

	private void initialiseGraphs() {
		lineGraph = (LineGraphView) view.findViewById(R.id.myLineGraph);
		pieChart = (PieChartView) view.findViewById(R.id.myPieChart);
		barChart = (BarChartView) view.findViewById(R.id.myBarChart);
	}

	private void addPointsToGraphs() {
		addPointsToLineGraph();
		addPointsToPieChart();
		addPointsToBarChart();
	}

	private void addPointsToLineGraph() {
		if (lineGraph != null) {
			int length = 0;
			if (lineGraph != null) {

				int myLine = lineGraph.addLine(Color.rgb(255, 204, 2));
				for (int i = 0; i < 8; i++) {
					if (i % 2 == 1) {
						length = randNumbers.nextInt(100 - 0 + 1) + 0;

						lineGraph.addPoint(myLine, i, length, randomString());
					} else {
						lineGraph.addPoint(myLine, i, length, "");
					}

				}

				myLine = lineGraph.addLine(Color.BLUE);
				for (int i = 0; i < 8; i++) {
					if (i % 2 == 1) {
						length = randNumbers.nextInt(100 - 0 + 1) + 0;

						lineGraph.addPoint(myLine, i, length, randomString());
					} else {
						lineGraph.addPoint(myLine, i, length, "");
					}

				}

				myLine = lineGraph.addLine(Color.GREEN);
				for (int i = 0; i < 8; i++) {
					if (i % 2 == 1) {
						length = randNumbers.nextInt(100 - 0 + 1) + 0;

						lineGraph.addPoint(myLine, i, length, randomString());
					} else {
						lineGraph.addPoint(myLine, i, length, "");
					}

				}

				myLine = lineGraph.addLine(Color.RED);
				for (int i = 0; i < 8; i++) {
					if (i % 2 == 1) {
						length = randNumbers.nextInt(100 - 0 + 1) + 0;

						lineGraph.addPoint(myLine, i, length, randomString());
					} else {
						lineGraph.addPoint(myLine, i, length, "");
					}

				}
			}
		}
	}

	private void addPointsToPieChart() {
		if (pieChart != null) {
			pieChart.addSection(Color.BLUE, 100);
			pieChart.addSection(Color.YELLOW, 20);
			pieChart.addSection(Color.RED, 40);
		}
	}

	private void addPointsToBarChart() {
		if (barChart != null) {
			barChart.addBar(Color.RED, 50);
			barChart.addBar(Color.BLUE, 75);
			barChart.addBar(Color.GREEN, 25);
			barChart.addBar(Color.YELLOW, 50);
			barChart.addBar(Color.CYAN, 100);
			barChart.addBar(Color.RED, 50);
			barChart.addBar(Color.BLUE, 75);
			barChart.addBar(Color.GREEN, 25);
			barChart.addBar(Color.YELLOW, 50);
			barChart.addBar(Color.CYAN, 100);
		}
	}

	private void setGraphsInvisible() {
		pieChart.setVisibility(View.GONE);
		lineGraph.setVisibility(View.GONE);
		barChart.setVisibility(View.GONE);
	}

	// use the list to check which is checked on un-checked
	List<CheckBox> nodes = new ArrayList<CheckBox>();

	// Initialize checkboxes
	private void initializeAndAddListnerCheckBoxes() {

		seriousness = (CheckBox) view.findViewById(R.id.seriousnessCheckBox);
		anxiety = (CheckBox) view.findViewById(R.id.AnxietyCheckBox);
		ref_anxiety = (CheckBox) view.findViewById(R.id.ref_anxietyCheckBox);
		ref_seriousness = (CheckBox) view
				.findViewById(R.id.ref_seriousnessCheckBox);
		// adding the checkboxes to the list
		nodes.add(seriousness);
		nodes.add(anxiety);
		nodes.add(ref_seriousness);
		nodes.add(ref_anxiety);

		seriousness.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!seriousness.isChecked()) {
					// check which lines not to render
					for (int i = 0; i < nodes.size(); i++) {
						if (!nodes.get(i).isChecked()) {
							lineGraph.clearIndex(i);
							nodes.remove(i);

						}

					}
					lineGraph.invalidate();

				}

				if (seriousness.isChecked()) {
					nodes.add(seriousness);
					int myLine = lineGraph.addLine(Color.rgb(255, 204, 2));
					for (int i = 0; i < 8; i++) {
						int length = randNumbers.nextInt(100 - 0 + 1) + 0;
						lineGraph.addPoint(myLine, i, length, randomString());

					}
					lineGraph.invalidate();

				}

			}
		});

		anxiety.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!anxiety.isChecked()) {
					// check which lines not to render

					for (int i = 0; i < nodes.size(); i++) {
						if (!nodes.get(i).isChecked()) {
							lineGraph.clearIndex(i);
							nodes.remove(i);

						}

					}
					lineGraph.invalidate();

				}
				if (anxiety.isChecked()) {
					int myLine = lineGraph.addLine(Color.BLUE);
					nodes.add(anxiety);
					for (int i = 0; i < 8; i++) {
						int length = randNumbers.nextInt(100 - 0 + 1) + 0;
						lineGraph.addPoint(myLine, i, length, randomString());

					}

					lineGraph.invalidate();
				}

			}
		});

		ref_anxiety.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!ref_anxiety.isChecked()) {
					// check which lines not to render

					for (int i = 0; i < nodes.size(); i++) {
						if (!nodes.get(i).isChecked()) {
							lineGraph.clearIndex(i);
							nodes.remove(i);

						}

					}
					lineGraph.invalidate();

				}
				if (ref_anxiety.isChecked()) {
					nodes.add(ref_anxiety);
					int myLine = lineGraph.addLine(Color.RED);
					for (int i = 0; i < 8; i++) {
						int length = randNumbers.nextInt(100 - 0 + 1) + 0;
						lineGraph.addPoint(myLine, i, length, randomString());

					}

					lineGraph.invalidate();
				}

			}
		});

		ref_seriousness.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!ref_seriousness.isChecked()) {
					// check which lines not to render

					for (int i = 0; i < nodes.size(); i++) {
						if (!nodes.get(i).isChecked()) {
							lineGraph.clearIndex(i);
							nodes.remove(i);

						}

					}
					lineGraph.invalidate();

				}
				if (ref_seriousness.isChecked()) {
					nodes.add(ref_seriousness);
					int myLine = lineGraph.addLine(Color.GREEN);
					for (int i = 0; i < 8; i++) {
						int length = randNumbers.nextInt(100 - 0 + 1) + 0;
						lineGraph.addPoint(myLine, i, length, randomString());

					}

					lineGraph.invalidate();
				}

			}
		});
	}

	private String randomString() {
		int length = 15;
		char[] A_Z = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		char[] result = new char[length];
		Random rand = new SecureRandom();
		for (int i = 0; i < result.length; i++) {
			int randomCharIndex = rand.nextInt(A_Z.length);
			result[i] = A_Z[randomCharIndex];
		}
		return new String(result);
	}
}