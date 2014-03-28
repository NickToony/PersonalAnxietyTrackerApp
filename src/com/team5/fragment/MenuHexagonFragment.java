package com.team5.fragment;

import com.team5.menu.HexagonView;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;

/**
 * Experimental menu concept proposed by designers. Dropped.
 * @author Nick
 *
 */
public class MenuHexagonFragment extends Fragment implements OnClickListener {
	private HexagonView myHexagon;
	private static final int FLING_MIN_DISTANCE = 120;
	private static final int FLING_X_TOLERACE = 400;
	private static final int FLING_MIN_SPEED = 150;
	private GestureDetector myDetector;
	private View.OnTouchListener myListener;
	
	private View myView;
	private HomeActivity myActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.hexagon_layout, container,
				false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Wheel Menu");

		// Find the hexagon view
		myHexagon = (HexagonView) myView.findViewById(R.id.myHexagon);

		// Create a gesture detector
		myDetector = new GestureDetector(getActivity(), new MyGestureDetector());
		// Define a listener
		myListener = new View.OnTouchListener() {
			// On a touch event
			public boolean onTouch(View v, MotionEvent event) {
				// Forward the event on to our gesture detector
				return myDetector.onTouchEvent(event);
			}
		};

		// Tell the hexagon to forward click events to this object
		myHexagon.setOnClickListener(this);
		// Forward any touch events on to the listener
		myHexagon.setOnTouchListener(myListener);

		return myView;
	}

	// This is the gesture detector
	class MyGestureDetector extends SimpleOnGestureListener {
		// On the "fling" event
		@Override
		public boolean onFling(MotionEvent eventStart, MotionEvent eventEnd,
				float speedX, float speedY) {
			// If too much movement on y axis
			if (Math.abs(eventStart.getX() - eventEnd.getX()) > FLING_X_TOLERACE)
				// It's not valid, do not handle event
				return false;

			float speed = Math.abs(speedX);
			float distance = eventStart.getX() - eventEnd.getX();

			// If fast enough swipe
			if (speed > FLING_MIN_SPEED) {
				// If left swipe is bigger than min distance
				if (distance > FLING_MIN_DISTANCE) {
					// Rotate wheel clockwise to next position
					myHexagon
							.setTargetSection(myHexagon.getCurrentSection() + 1);
					// Event handled
					return true;
				}
				// else if right swipe is bigger than min distance
				else if (distance < -FLING_MIN_DISTANCE) {
					// Rotate wheel anti-clockwise to previous position
					myHexagon
							.setTargetSection(myHexagon.getCurrentSection() - 1);
					// Event handled
					return true;
				}
			}
			// Did not handle event
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
