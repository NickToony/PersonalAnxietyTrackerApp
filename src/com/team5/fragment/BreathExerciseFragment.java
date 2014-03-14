package com.team5.fragment;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class BreathExerciseFragment extends Fragment implements OnClickListener
{
	// Layout components for breath exercise
	private Button startButton;
	private Button stopButton;
	private View myView;
	private HomeActivity myActivity;
	private ImageView image;
	private int countDownSeconds;
	private EditText secondsEditTextView;
	private CountDownTimer timer;
	private boolean timerToggled = false;
	private Animation expand;
	private boolean stopButtonToggled = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		myView = inflater.inflate(R.layout.breath_exercise_layout, container,
				false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Breathing");

		initialiseComponents();

		return myView;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.startButton:
			//get the set time for the animation to run
			countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
					.toString());
			// if the timer is not started and the seconds differ from 0 start the animation
			if (!timerToggled && countDownSeconds != 0)
			{

				image.startAnimation(expand);
				timer(countDownSeconds);
				timerToggled = true;

			}

			break;
		case R.id.stopButton:

			if (timerToggled)
			{
				// when the stop button is pressed - stop the timer, indicate
				// that the stop button is pressed and stop the animation, then
				// indicate the timer has been stopped
				timer.cancel();
				stopButtonToggled = true;
				image.clearAnimation();
				timerToggled = false;
			}
			break;

		default:

			break;
		}
	}

	private void initialiseComponents()
	{
		// the seconds input field
		secondsEditTextView = (EditText) myView.findViewById(R.id.secondsInput);
		// the image that is scaled
		image = (ImageView) myView.findViewById(R.id.dynamic_circle);
		// the integer taken from the seconds input field
		countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
				.toString());
		// the animation that uses XML
		expand = AnimationUtils.loadAnimation(
				myActivity.getApplicationContext(),
				R.animator.breath_exercise_anim);

		// start and stop buttons and their listeners
		startButton = (Button) myView.findViewById(R.id.startButton);
		stopButton = (Button) myView.findViewById(R.id.stopButton);
		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);


		// the animation listener to help with the repeat count
		expand.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				// when the animation ends, restart it
				image.startAnimation(expand);
				// if the timer has finished and stop button is automatically
				// clicked, stop the animation entirely
				if (stopButtonToggled)
				{
					stopButtonToggled = false;
					image.clearAnimation();
					timerToggled = false;
				}
			}
		});

	}

	// the timer
	private void timer(long remainingTime)
	{
		long secondsTick = 1000;
		timerToggled = true;

		timer = new CountDownTimer((remainingTime + 1) * secondsTick,
				secondsTick) {

			public void onTick(long millisUntilFinished)
			{

				if (millisUntilFinished < 2001)
				{
					// imitates the stop button click event, stops the timer and
					// clears the animation
					countDownSeconds--;
					secondsEditTextView.setText("" + countDownSeconds);

					stopButtonToggled = true;
					timerToggled = false;
					image.clearAnimation();
					timer.cancel();
				}

				else
				{
					// updates the seconds left field and the progress bar on
					// each
					// tick and
					countDownSeconds--;
					secondsEditTextView.setText("" + countDownSeconds);
				}

			}

			public void onFinish()
			{

			}
		}.start();
	}
}
