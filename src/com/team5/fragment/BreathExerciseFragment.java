package com.team5.fragment;

import com.team5.fragment.GraphFragment.DropDownListener;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;

import android.R.anim;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ActionBar.OnNavigationListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SpinnerAdapter;

public class BreathExerciseFragment extends Fragment implements OnClickListener {
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
	private boolean stopButtonToggled = false;
	private AnimationSet animSet;
	private Animation expand;
	private Animation shrink;
	private long defaultExpandDuration = 2000;
	private long defaultShrinkDuration = 3000;
	private long expandDuration = defaultExpandDuration;
	private long shrinkDuration = defaultShrinkDuration;
	private int durationRateOfChange = 2;
	private ActionBar actionBar;
	private SeekBar speedSetSeekBar;
	private int speedSetSeekBarMaxProgress = 2;
	private int speedSetSeekBarCurrentProgress = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.breath_exercise_layout, container,
				false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Breathing");

		initialiseComponents();

		return myView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startButton:
			// get the set time for the animation to run
			countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
					.toString());
			// if the timer is not started and the seconds differ from 0 start
			// the animation
			if (!timerToggled && countDownSeconds != 0) {

				image.startAnimation(animSet);
				timer(countDownSeconds);
				timerToggled = true;

			}

			break;
		case R.id.stopButton:

			if (timerToggled) {
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

	private void initialiseComponents() {

		// the seconds input field
		secondsEditTextView = (EditText) myView.findViewById(R.id.secondsInput);
		// the image that is scaled
		image = (ImageView) myView.findViewById(R.id.dynamic_circle);
		// the integer taken from the seconds input field
		countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
				.toString());
		// Set the action bar
		SpinnerAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.countdown_options, R.layout.spinner_list);
		// Action bar settings
		actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(adapter, new DropDownListener());

		// Set the speed seek bar
		speedSetSeekBar = (SeekBar) myView
				.findViewById(R.id.inhale_exhale_speed);
		speedSetSeekBar.setMax(speedSetSeekBarMaxProgress);
		speedSetSeekBar.setProgress(speedSetSeekBarCurrentProgress);
		// Change the parameters when seekBar is initialized
		expandDuration = expandDuration + expandDuration
				* speedSetSeekBar.getProgress();
		shrinkDuration = shrinkDuration + shrinkDuration
				* speedSetSeekBar.getProgress();

		// Set up a listener on speedSetSeekBar
		speedSetSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

						if (speedSetSeekBar.getProgress() > speedSetSeekBarCurrentProgress) {

							expandDuration = expandDuration
									* ((speedSetSeekBar.getProgress() - speedSetSeekBarCurrentProgress) * durationRateOfChange);
							shrinkDuration = shrinkDuration
									* ((speedSetSeekBar.getProgress() - speedSetSeekBarCurrentProgress) * durationRateOfChange);

						} else if (speedSetSeekBar.getProgress() < speedSetSeekBarCurrentProgress) {
							expandDuration = expandDuration
									/ ((speedSetSeekBarCurrentProgress - speedSetSeekBar
											.getProgress()) * durationRateOfChange);
							shrinkDuration = shrinkDuration
									/ ((speedSetSeekBarCurrentProgress - speedSetSeekBar
											.getProgress()) * durationRateOfChange);
						}
						speedSetSeekBarCurrentProgress = speedSetSeekBar
								.getProgress();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub

					}
				});

		// Set up the animation
		setUpAnimation();

		// start and stop buttons and their listeners
		startButton = (Button) myView.findViewById(R.id.startButton);
		stopButton = (Button) myView.findViewById(R.id.stopButton);
		startButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);

		// the animation listener to help with the repeat count
		animSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// when the animation ends, restart it
				image.startAnimation(animSet);
				// if the timer has finished and stop button is automatically
				// clicked, stop the animation entirely
				if (stopButtonToggled) {
					stopButtonToggled = false;
					image.clearAnimation();
					timerToggled = false;
				}
			}
		});

	}

	private void setUpAnimation() {
		image.clearAnimation();
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

	class DropDownListener implements OnNavigationListener {
		// The same word as SpinerAdapter
		String[] titles = getResources().getStringArray(
				R.array.countdown_options);

		@Override
		public boolean onNavigationItemSelected(int position, long id) {
			switch (position) {
			case 0:
				return true;
			case 1:
				return true;
			default:
				return false;
			}
		}
	}

	// the timer
	private void timer(long remainingTime) {
		long secondsTick = 1000;
		timerToggled = true;

		timer = new CountDownTimer((remainingTime + 1) * secondsTick,
				secondsTick) {

			public void onTick(long millisUntilFinished) {

				if (millisUntilFinished < 2001) {
					// imitates the stop button click event, stops the timer and
					// clears the animation
					countDownSeconds--;
					secondsEditTextView.setText("" + countDownSeconds);

					stopButtonToggled = true;
					timerToggled = false;
					image.clearAnimation();
					timer.cancel();
				}

				else {
					// updates the seconds left field and the progress bar on
					// each
					// tick and
					countDownSeconds--;
					secondsEditTextView.setText("" + countDownSeconds);
				}

			}

			public void onFinish() {

			}
		}.start();
	}
}
