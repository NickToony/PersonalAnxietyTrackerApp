package com.team5.fragment;

import java.util.List;

import com.team5.graph.LineGraphView;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecordGraphFragment extends Fragment {
	private View myView;
	private HomeActivity myActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.record_graph_layout, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Graph Test");

		// Fill the line graph
		LineGraphView mLineGraph = (LineGraphView) myView
				.findViewById(R.id.myLineGraph);
		// Add a line
		int mRedLine = mLineGraph.addLine(Color.RED);
		int mBlueLine = mLineGraph.addLine(Color.BLUE);

		// Get all records between certain point
		List<UserRecord> points = ((Session) getActivity().getApplication())
				.getUserAccount().getRecordByTime(0,
						new java.util.Date().getTime());
		//
		// // For each record
		for (int i = 0; i < points.size(); i++) {
			// add to graph
//			mLineGraph.addPoint(mRedLine, i, points.get(i).getAnxiety(), "Random string");
//			mLineGraph.addPoint(mBlueLine, i, points.get(i).getSeriousness(), "Random string");
		}

		// Make it visible
		mLineGraph.setVisibility(View.VISIBLE);

		return myView;
	}
}
