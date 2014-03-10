package com.team5.fragment;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.R.anim;
import android.app.Fragment;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BreathExerciseFragment extends Fragment implements OnClickListener
{
	// Layout components for breath exercise
	private Button startButton;
	private Button stopButton;
	private View myView;
	private HomeActivity myActivity;
	private ImageView image;
	private Animation expand;
	private int countDownSeconds;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		myView = inflater.inflate(R.layout.breath_exercise_layout, container,
				false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Breathing");

		initialiseComponents();

		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);

		return myView;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.startButton:

			image.startAnimation(expand);
			timer(countDownSeconds);

			break;
		case R.id.stopButton:

			break;
		}
	}

	private void initialiseComponents()
	{

		image = (ImageView) myView.findViewById(R.id.asd_id);

		expand = AnimationUtils.loadAnimation(myActivity,
				R.animator.breath_exercise_anim);

		startButton = (Button) myView.findViewById(R.id.startButton);
		stopButton = (Button) myView.findViewById(R.id.stopButton);

	}

	private void timer(long remainingTime)
	{
		long secondsTick = 1000;
		countDownSeconds = Integer.getInteger(((EditText) myView
				.findViewById(R.id.secondsInput)).getText().toString(), 0);

		new CountDownTimer(remainingTime, secondsTick) {

			public void onTick(long millisUntilFinished)
			{
				countDownSeconds--;
				((EditText) myView.findViewById(R.id.secondsInput))
						.setText(String.valueOf(countDownSeconds));
			}

			public void onFinish()
			{

			}
		}.start();
	}
}
