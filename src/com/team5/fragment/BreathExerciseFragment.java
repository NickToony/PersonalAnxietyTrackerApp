package com.team5.fragment;

import com.team5.exercise.AnimationCanvas;
import com.team5.pat.R;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class BreathExerciseFragment extends Fragment implements OnClickListener {
	// Layout components for breath exercise
	private AnimationCanvas animCanvas;
	private Button startButton;
	private Button stopButton;
	private View view;
	private Handler mHandler;
	private Runnable reDrawInhale;
	private Runnable reDrawExhale;

	// Constants that set frames per second, inhale and exhale values
	private final int FPS = 50;
	private final int INHALE_SECONDS = 1;
	private final int EXHALE_SECONDS = 4;
	private final int DRAW_DELAY = 1000 / FPS;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.breath_exercise_layout, container,
				false);

		initialiseComponents();
		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startButton:
			mHandler.post(reDrawInhale);
			mHandler.post(reDrawExhale);
			break;
		case R.id.stopButton:
			mHandler.removeCallbacks(reDrawInhale);
			mHandler.removeCallbacks(reDrawExhale);
			break;
		}
	}

	private void initialiseComponents() {
		startButton = (Button) view.findViewById(R.id.startButton);
		stopButton = (Button) view.findViewById(R.id.stopButton);
		animCanvas = (AnimationCanvas) view.findViewById(R.id.animationCanvas);
		mHandler = new Handler();
		reDrawInhale = new Runnable() {
			public void run() {
				if (animCanvas.backgroundCircleRadius <= animCanvas.dynamicCircleRadius) {
					mHandler.removeCallbacks(this);
				} else {
					animCanvas.dynamicCircleRadius = animCanvas.dynamicCircleRadius
							+ (animCanvas.backgroundCircleRadius - animCanvas.centerCircleRadius)
							/ (INHALE_SECONDS * FPS);
					animCanvas.invalidate();

					mHandler.postDelayed(this, DRAW_DELAY);
				}
			}

		};
		reDrawExhale = new Runnable() {
			public void run() {
				if (animCanvas.centerCircleRadius >= animCanvas.dynamicCircleRadius) {
					mHandler.removeCallbacks(this);
				} else {
					animCanvas.dynamicCircleRadius = animCanvas.dynamicCircleRadius
							- (animCanvas.backgroundCircleRadius - animCanvas.centerCircleRadius)
							/ (EXHALE_SECONDS * FPS);

					animCanvas.invalidate();

					mHandler.postDelayed(this, DRAW_DELAY);
				}
			}
		};
	}
}
