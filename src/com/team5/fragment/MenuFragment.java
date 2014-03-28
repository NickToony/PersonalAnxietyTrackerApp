package com.team5.fragment;

import com.team5.menu.CircleView;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Experimental menu concept proposed by designers. Dropped.
 * @author Nick
 *
 */
public class MenuFragment extends Fragment implements OnClickListener {
	private CircleView mCircle;
	private static final int FLING_MIN_DISTANCE = 120;
	private static final int FLING_Y_TOLERACE = 400;
	private static final int FLING_MIN_SPEED = 200;
	private GestureDetector mDetector;
	private View.OnTouchListener mListener;
	
	private View myView;
	private HomeActivity myActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.menu_layout, container, false);

		mCircle = (CircleView) myView.findViewById(R.id.myHexagon);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Rainbow Menu");

		// Create a gesture detector
		mDetector = new GestureDetector(getActivity(), new LongPressDetector());
		// Define a listener
		mListener = new View.OnTouchListener() {
			// On a touch event
			public boolean onTouch(View v, MotionEvent event) {
				// Forward the event on to our gesture detector
				return mDetector.onTouchEvent(event);
			}
		};

		mCircle.setOnClickListener(this);
		mCircle.setOnTouchListener(mListener);

		// Populate links
		mCircle.addItem("Test 1", Color.rgb(74, 184, 85));
		mCircle.addItem("Test 2", Color.rgb(143, 76, 152));
		mCircle.addItem("Test 3", Color.rgb(83, 89, 163));
		mCircle.addItem("Test 4", Color.rgb(246, 58, 58));
		mCircle.addItem("Test 5", Color.rgb(233, 224, 73));

		return myView;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	class LongPressDetector extends SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			Context context = getActivity().getApplicationContext();
			Toast.makeText(context, "Already pressed", Toast.LENGTH_SHORT)
					.show();
		}

		// On the "fling" event
		@Override
		public boolean onFling(MotionEvent eventStart, MotionEvent eventEnd,
				float speedX, float speedY) {
			// If too much movement on y axis
			if (Math.abs(eventStart.getY() - eventEnd.getY()) > FLING_Y_TOLERACE)
				// It's not valid, do not handle event
				return false;

			float speed = Math.abs(speedY);
			float distance = eventEnd.getY() - eventStart.getY();

			// If fast enough swipe
			if (speed > FLING_MIN_SPEED) {
				// If left swipe is bigger than min distance
				if (distance > FLING_MIN_DISTANCE) {
					// Rotate wheel clockwise to next position
					mCircle.setTargetSection(mCircle.getTargetSection() + 1);
					// Event handled
					return true;
				}
				// else if right swipe is bigger than min distance
				else if (distance < -FLING_MIN_DISTANCE) {
					// Rotate wheel anti-clockwise to previous position
					mCircle.setTargetSection(mCircle.getTargetSection() - 1);
					// Event handled
					return true;
				}
			}
			// Did not handle event
			return false;
		}
	}
}
