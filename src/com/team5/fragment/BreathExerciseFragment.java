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
import android.view.animation.ScaleAnimation;
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
	private Animation expand;
	private int countDownSeconds;
	private EditText secondsEditTextView;
	private CountDownTimer timer;
	private boolean timerToggled;

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
			
			if (!timerToggled)
			{
				
				image.startAnimation(expand);
				timer(countDownSeconds);
			}

			break;
		case R.id.stopButton:
			
			if(timerToggled)
			{
			image.clearAnimation();
			timer.cancel();
			timerToggled = false;
			}
			break;
		}
	}

	private void initialiseComponents()
	{
		secondsEditTextView = (EditText) myView.findViewById(R.id.secondsInput);
		image = (ImageView) myView.findViewById(R.id.dynamic_circle);
		countDownSeconds = Integer.parseInt(secondsEditTextView.getText()
				.toString());
		expand = AnimationUtils.loadAnimation(myActivity,
				R.animator.breath_exercise_anim);

		startButton = (Button) myView.findViewById(R.id.startButton);
		stopButton = (Button) myView.findViewById(R.id.stopButton);
		

	}
	
	private void animation()
	{
		AnimationSet as = new AnimationSet(false); 
		Animation expand;
		Animation shrink;
		int fromDuration;
		int toDuration;
		
		expand = new ScaleAnimation(1.0f, 2.5f, 1.0f, 2.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		expand.setDuration(2000);
		shrink= new ScaleAnimation(1.0f, 2.5f, 1.0f, 2.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	}
	
	private void timer(long remainingTime)
	{
		long secondsTick = 1000;
		timerToggled = true;

		timer = new CountDownTimer(remainingTime * secondsTick, secondsTick) {

			public void onTick(long millisUntilFinished)
			{
				countDownSeconds--;
				secondsEditTextView.setText("" + countDownSeconds);
			}

			public void onFinish()
			{
				image.clearAnimation();
				timer.cancel();
				timerToggled = false;
			}
		}.start();
	}
}
