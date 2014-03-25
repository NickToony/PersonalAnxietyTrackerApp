package com.team5.fragment;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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
	private int countDownCycles;
	private SeekBar speedSetSeekBar;
	private EditText secondsEditTextView;
	private TextView countDownMainView;
	private TextView countDownType;
	private TextView speedToggle;
	private TextView secondsLeftText;
	private TextView cyclesLeftText;
	private EditText cyclesInputEditText;
	private CountDownTimer timer;
	private boolean startToggled = false;
	private AnimationSet animSet;
	private Animation expand;
	private Animation shrink;
	private long[] EXPAND_DURATION_TIMES = { 3000, 2000, 1500 };
	private long[] SHRINK_DURATION_TIMES = { 4500, 3000, 2000 };
	private long EXPAND_DURATION = EXPAND_DURATION_TIMES[1];
	private long SHRINK_DURATION = SHRINK_DURATION_TIMES[1];
	private int SEEKBAR_MAX_PROGRESS = 2;
	private int SEEKBAR_CURRENT_PROGRESS = 1;
	private boolean seekBarVisibility = false;
	private boolean countDownTypeCycles = false;
	private boolean animationListenerCalledOnce = true;
	private boolean firstTimerTick = true;
	private boolean animationJustStarted = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		myView = inflater.inflate(R.layout.breath_exercise_layout, container,
				false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Breathing");

		initialiseComponents();
		// Disable screen orientation
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

			switchLayout();

			break;

		case R.id.animation_speed:

			if (!seekBarVisibility)
			{
				speedSetSeekBar.setVisibility(View.VISIBLE);
				seekBarVisibility = !seekBarVisibility;
			} else
			{
				speedSetSeekBar.setVisibility(View.GONE);
				seekBarVisibility = !seekBarVisibility;
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
		// the cycles input field
		cyclesInputEditText = (EditText) myView.findViewById(R.id.cyclesInput);
		// Seconds text view
		secondsLeftText = (TextView) myView.findViewById(R.id.secondsLeftText);
		// Cycles text view
		cyclesLeftText = (TextView) myView.findViewById(R.id.cyclesLeftText);

		countDownMainView = (TextView) myView.findViewById(R.id.mainSeconds);
		// the countDown type button and speed toggle
		countDownType = (TextView) myView.findViewById(R.id.countDownType);
		speedToggle = (TextView) myView.findViewById(R.id.animation_speed);
		// the image that is scaled
		image = (ImageView) myView.findViewById(R.id.dynamic_circle);
		// the integer taken from the seconds input field
		countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
				.toString());
		// the integer taken from the cycles input field
		countDownCycles = Integer.parseInt(cyclesInputEditText.getText()
				.toString());

		// Set the speed seek bar
		speedSetSeekBar = (SeekBar) myView
				.findViewById(R.id.inhale_exhale_speed);
		speedSetSeekBar.setMax(SEEKBAR_MAX_PROGRESS);
		speedSetSeekBar.setProgress(SEEKBAR_CURRENT_PROGRESS);

		// Set up the animation
		setUpAnimation();

		// Set all the listeners for the activity
		setListeners();

		// start and stop buttons and their listeners as well as views
		startButton = (Button) myView.findViewById(R.id.startButton);
		stopButton = (Button) myView.findViewById(R.id.stopButton);
		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		countDownType.setOnClickListener(this);
		speedToggle.setOnClickListener(this);

	}

	private void startAnimation()
	{
		countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
				.toString());
		countDownCycles = Integer.parseInt(cyclesInputEditText.getText()
				.toString());
		animationListenerCalledOnce = true;

		if (!startToggled && countDownSeconds != 0 && !countDownTypeCycles)
		{
			timer(countDownSeconds);
			image.startAnimation(animSet);
			startToggled = true;
		} else if (!startToggled && countDownCycles != 0 && countDownTypeCycles)
		{
			image.startAnimation(animSet);
			startToggled = true;
		}
	}

	private void stopAnimation()
	{

		if (startToggled && !countDownTypeCycles)
		{
			startToggled = false;
			firstTimerTick = true;
			timer.cancel();
			image.clearAnimation();
		} else if (startToggled && countDownTypeCycles)
		{
			startToggled = false;
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
		expand.setDuration((long) EXPAND_DURATION);
		expand.setInterpolator(new LinearInterpolator());
		expand.setFillEnabled(true);
		expand.setFillAfter(true);
		expand.setFillBefore(false);

		shrink = new ScaleAnimation(1.0f, 0.4f, 1.0f, 0.4f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		shrink.setDuration((long) SHRINK_DURATION);
		shrink.setStartOffset((long) EXPAND_DURATION);
		shrink.setInterpolator(new LinearInterpolator());
		shrink.setFillEnabled(true);
		shrink.setFillAfter(true);
		shrink.setFillBefore(false);

		animSet.addAnimation(expand);
		animSet.addAnimation(shrink);
	}

	private void resetDurations(double expandDur, double shrinkDur)
	{
		expand.setDuration((long) expandDur);
		shrink.setStartOffset((long) expandDur);
		shrink.setDuration((long) shrinkDur);
	}

	private void switchLayout()
	{
		if (countDownTypeCycles)
		{
			if (startToggled)
			{
				stopAnimation();
			}
			// Change layout to seconds
			secondsEditTextView.setVisibility(View.VISIBLE);
			secondsLeftText.setVisibility(View.VISIBLE);
			cyclesInputEditText.setVisibility(View.GONE);
			cyclesLeftText.setVisibility(View.GONE);
			countDownType.setText(R.string.secondsLabel);
			countDownMainView.setText("" + countDownSeconds);

			countDownTypeCycles = !countDownTypeCycles;
		} else
		{
			if (startToggled)
			{
				stopAnimation();
			}
			// Change layout to cycles
			secondsEditTextView.setVisibility(View.GONE);
			secondsLeftText.setVisibility(View.GONE);
			cyclesInputEditText.setVisibility(View.VISIBLE);
			cyclesLeftText.setVisibility(View.VISIBLE);
			countDownType.setText(R.string.cyclesLabel);
			countDownMainView.setText("" + countDownCycles);

			countDownTypeCycles = !countDownTypeCycles;
		}
	}

	private void setListeners()
	{

		// Set up a listener on speedSetSeekBar
		speedSetSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{

						if (speedSetSeekBar.getProgress() < SEEKBAR_CURRENT_PROGRESS)
						{

							// If animation started - stop it
							if (startToggled)
							{
								animationJustStarted=true;
								stopAnimation();
							}

							// Calculate new durations for increased speed
							EXPAND_DURATION = EXPAND_DURATION_TIMES[speedSetSeekBar
									.getProgress()];
							SHRINK_DURATION = SHRINK_DURATION_TIMES[speedSetSeekBar
									.getProgress()];
							// Reset animation and start it
							resetDurations(EXPAND_DURATION, SHRINK_DURATION);
							startAnimation();

						} else if (speedSetSeekBar.getProgress() > SEEKBAR_CURRENT_PROGRESS)
						{

							// If animation started - stop it
							if (startToggled)
							{

								animationJustStarted=true;
								stopAnimation();
							}

							// Calculate new durations for increased speed
							EXPAND_DURATION = EXPAND_DURATION_TIMES[speedSetSeekBar
									.getProgress()];
							SHRINK_DURATION = SHRINK_DURATION_TIMES[speedSetSeekBar
									.getProgress()];

							// Reset animation and start it
							resetDurations(EXPAND_DURATION, SHRINK_DURATION);
							startAnimation();

						}
						SEEKBAR_CURRENT_PROGRESS = speedSetSeekBar
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

				// first if statement fixes the bug(onAnimationEnd is called
				// twice instead of once)
				if (animationListenerCalledOnce)
				{
					if (startToggled)
					{
						image.startAnimation(animSet);

					}

					if (startToggled && countDownTypeCycles
							&& countDownCycles != 0)
					{
						if(!animationJustStarted)
						{
							countDownCycles--;
						}
						else
						{
							animationJustStarted=false;
						}
						cyclesInputEditText.setText("" + countDownCycles);
					}

					if (countDownTypeCycles && countDownCycles == 0)
					{
						stopAnimation();
					}
					animationListenerCalledOnce = !animationListenerCalledOnce;
				} else if (!animationListenerCalledOnce)
				{
					if (startToggled)
					{
						image.startAnimation(animSet);

					}

					animationListenerCalledOnce = !animationListenerCalledOnce;
				}

			}
		});

		// Updates views on change of the text of seconds edit text
		secondsEditTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{

				if (secondsEditTextView.getText().length() == 0)
				{
					countDownSeconds = 0;
					countDownMainView.setText("" + 0);
					secondsEditTextView.setText("" + 0);

				} else
				{
					countDownSeconds = Integer.parseInt(secondsEditTextView
							.getText().toString());
					countDownMainView.setText("" + countDownSeconds);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{

			}
		});

		// Updates views on change of the text of cycles edit text
		cyclesInputEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				if (cyclesInputEditText.getText().length() == 0)
				{
					countDownCycles = 0;
					countDownMainView.setText("" + 0);
					cyclesInputEditText.setText("" + 0);
				} else
				{
					countDownCycles = Integer.parseInt(cyclesInputEditText
							.getText().toString());
					countDownMainView.setText("" + countDownCycles);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}
		});

		secondsEditTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// If animation started - stop it
				if (startToggled)
				{
					stopAnimation();
				}

				secondsEditTextView.setSelection(secondsEditTextView.getText()
						.length());

			}
		});

		cyclesInputEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// If animation started - stop it
				if (startToggled)
				{
					stopAnimation();
				}

				cyclesInputEditText.setSelection(cyclesInputEditText.getText()
						.length());

			}
		});

	}

	// the timer
	private void timer(long remainingTime)
	{
		long secondsTick = 1000;

		timer = new CountDownTimer((remainingTime + 2) * secondsTick,
				secondsTick) {

			public void onTick(long millisUntilFinished)
			{
				if (firstTimerTick)
				{
					// do nothing change and boolean to false
					firstTimerTick = !firstTimerTick;
				} else if (!firstTimerTick)
				{
					if (millisUntilFinished < 2001)
					{
						// imitates the stop button click event, stops the timer
						// and
						// clears the animation
						countDownSeconds--;
						secondsEditTextView.setText("" + countDownSeconds);
						countDownMainView.setText("" + countDownSeconds);

						startToggled = false;
						image.clearAnimation();
						firstTimerTick = true;
						timer.cancel();
					}

					else
					{
						// updates the seconds left field
						// on
						// each
						// tick
						countDownSeconds--;
						secondsEditTextView.setText("" + countDownSeconds);
						countDownMainView.setText("" + countDownSeconds);

					}
				}
			}

			public void onFinish()
			{

			}
		}.start();
	}
}
