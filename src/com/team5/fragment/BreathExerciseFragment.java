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
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import android.widget.TextView;

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
	private TextView secondsMainView;
	private TextView countDownType;
	private TextView speedToggle;
	private CountDownTimer timer;
	private boolean startToggled = false;
	private AnimationSet animSet;
	private Animation expand;
	private Animation shrink;
	private long expandDuration = 2000;
	private long shrinkDuration = 3000;
	private int durationRateOfChange = 2;
	private SeekBar speedSetSeekBar;
	private int speedSetSeekBarMaxProgress = 2;
	private int speedSetSeekBarCurrentProgress = 1;
	private boolean seekBarVisibility= false;
	private boolean countDownTypeUsedSecondsDefault = true;
	

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

			startAnimation();

			break;
		case R.id.stopButton:

			stopAnimation();

			break;
	
		case R.id.countDownType:
			
			if(countDownTypeUsedSecondsDefault)
			{
				//left to IMPLEMENT
			}
			
			break;
			
		case R.id.animation_speed:
			
			if(!seekBarVisibility)
			{
				speedSetSeekBar.setVisibility(View.VISIBLE);
				seekBarVisibility=!seekBarVisibility;
			}
			else
			{
				speedSetSeekBar.setVisibility(View.GONE);
				seekBarVisibility=!seekBarVisibility;
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
		secondsMainView = (TextView) myView.findViewById(R.id.mainSeconds);
		// the countDown type button and speed toggle
		countDownType= (TextView) myView.findViewById(R.id.countDownType);
		speedToggle=(TextView) myView.findViewById(R.id.animation_speed);
		// the image that is scaled
		image = (ImageView) myView.findViewById(R.id.dynamic_circle);
		// the integer taken from the seconds input field
		countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
				.toString());

		// Set the speed seek bar
		speedSetSeekBar = (SeekBar) myView
				.findViewById(R.id.inhale_exhale_speed);
		speedSetSeekBar.setMax(speedSetSeekBarMaxProgress);
		speedSetSeekBar.setProgress(speedSetSeekBarCurrentProgress);

		// Set up a listener on speedSetSeekBar
		speedSetSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (speedSetSeekBar.getProgress() < speedSetSeekBarCurrentProgress)
						{

							// If animation started - stop it
							if (startToggled)
							{
								stopAnimation();
							}

							// Calculate new durations for increased speed
							expandDuration = expandDuration
									* ((speedSetSeekBarCurrentProgress - speedSetSeekBar
											.getProgress()) * durationRateOfChange);
							shrinkDuration = shrinkDuration
									* ((speedSetSeekBarCurrentProgress - speedSetSeekBar
											.getProgress()) * durationRateOfChange);

							// Reset animation and start it
							resetDurations(expandDuration, shrinkDuration);
							startAnimation();

						} else if (speedSetSeekBar.getProgress() > speedSetSeekBarCurrentProgress)
						{
							// If animation started - stop it
							if (startToggled)
							{
								stopAnimation();
							}

							// Calculate new durations for increased speed
							expandDuration = expandDuration
									/ ((speedSetSeekBar.getProgress() - speedSetSeekBarCurrentProgress) * durationRateOfChange);
							shrinkDuration = shrinkDuration
									/ ((speedSetSeekBar.getProgress() - speedSetSeekBarCurrentProgress) * durationRateOfChange);

							// Reset animation and start it
							resetDurations(expandDuration, shrinkDuration);
							startAnimation();
						}
						speedSetSeekBarCurrentProgress = speedSetSeekBar
								.getProgress();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser)
					{
						// TODO Auto-generated method stub

					}
				});

		// Set up the animation
		setUpAnimation();

		// start and stop buttons and their listeners as well as views
		startButton = (Button) myView.findViewById(R.id.startButton);
		stopButton = (Button) myView.findViewById(R.id.stopButton);
		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		countDownType.setOnClickListener(this);
		speedToggle.setOnClickListener(this);

		// the animation listener to help with the repeat count
		animSet.setAnimationListener(new AnimationListener() {

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

				if (startToggled)
				{
					image.startAnimation(animSet);
				}
			}
		});

	}

	private void startAnimation()
	{
		countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
				.toString());

		if (!startToggled && countDownSeconds != 0)
		{
			timer(countDownSeconds);
			image.startAnimation(animSet);
			startToggled = true;
		}
	}

	private void stopAnimation()
	{
		if (startToggled)
		{
			startToggled = false;
			timer.cancel();
			image.clearAnimation();

		}
	}

	private void setUpAnimation()
	{
		// Set the animations
		animSet = new AnimationSet(false);

		expand = new ScaleAnimation(1.0f, 2.5f, 1.0f, 2.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		expand.setDuration(expandDuration);
		expand.setInterpolator(new LinearInterpolator());
		expand.setFillEnabled(true);
		expand.setFillAfter(true);
		expand.setFillBefore(false);

		shrink = new ScaleAnimation(1.0f, 0.4f, 1.0f, 0.4f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		shrink.setDuration(shrinkDuration);
		shrink.setStartOffset(expandDuration);
		shrink.setInterpolator(new LinearInterpolator());
		shrink.setFillEnabled(true);
		shrink.setFillAfter(true);
		shrink.setFillBefore(false);

		animSet.addAnimation(expand);
		animSet.addAnimation(shrink);
	}

	private void resetDurations(long expandDur, long shrinkDur)
	{
		expand.setDuration(expandDur);
		shrink.setStartOffset(expandDur);
		shrink.setDuration(shrinkDur);
	}


	// the timer
	private void timer(long remainingTime)
	{
		long secondsTick = 1000;

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
					secondsMainView.setText("" + countDownSeconds);
					
					startToggled = false;
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
					secondsMainView.setText("" + countDownSeconds);
					
				}

			}

			public void onFinish()
			{

			}
		}.start();
	}
}
