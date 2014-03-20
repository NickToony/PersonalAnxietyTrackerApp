package com.team5.pat;

import java.util.List;

import com.team5.graph.LineGraphView;
import com.team5.pat.R;
import com.team5.user.UserRecord;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class RecordGraphActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_graph_layout);

		// Show the "navigate up" button on action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Fill the line graph
		LineGraphView mLineGraph = (LineGraphView) findViewById(R.id.myLineGraph);
		// Add a line
		int myRedLine = mLineGraph.addLine(Color.RED);
		int myBlueLine = mLineGraph.addLine(Color.BLUE);

		// Get all records between certain point
		// new java.util.Date().getTime() - (60*60*24*5)
		List<UserRecord> points = ((Session) getApplication()).getUserAccount()
				.getRecordByTime(0, new java.util.Date().getTime());

		// For each record
		for (int i = 0; i < points.size(); i++) {
			// add to graph
//			mLineGraph.addPoint(myRedLine, i, points.get(i).getAnxiety(),
//					"Random string");
//			mLineGraph.addPoint(myBlueLine, i, points.get(i).getSeriousness(),
//					"Random string");
		}

		// Make it visible
		mLineGraph.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
