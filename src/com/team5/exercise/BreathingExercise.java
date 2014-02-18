package com.team5.exercise;

import java.util.Timer;
import java.util.TimerTask;

import com.team5.mentalhealthapp.R;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BreathingExercise extends Activity
{

	private Button startButton;
	private Button stopButton;
//	private ProgressBar progress= new ProgressBar(findViewById(R.id.progressBarBreathExercise))

	// set frames per second, inhale and exhale values
	final int fps = 50;
	final int secondsInhale = 1;
	final int secondsExhale = 4;
	// calculate the delay for each redrawn
	final int drawDelay = 1000 / fps;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_breath_exercise);

		// get the seconds and store them in a counter
		TextView t = (TextView) findViewById(R.id.secondsInput);
		final com.team5.exercise.animationCanvas animCanvas = (com.team5.exercise.animationCanvas) findViewById(R.id.animationCanvas);
		// the handler to schedule the redraws
		final Handler myHandler = new Handler();
		final Runnable reDrawInhale = new Runnable() {
			public void run()
			{
				if (animCanvas.backgroundCircleRadius <= animCanvas.dynamicCircleRadius)
				{
					myHandler.removeCallbacks(this);
				} else
				{
					animCanvas.dynamicCircleRadius = animCanvas.dynamicCircleRadius
							+ (animCanvas.backgroundCircleRadius - animCanvas.centerCircleRadius)
							/ (secondsInhale * fps);
					animCanvas.invalidate();
					
					myHandler.postDelayed(this, drawDelay);
				}
			}
			
		};

		final Runnable reDrawExhale = new Runnable() {
			public void run()
			{
				if (animCanvas.centerCircleRadius >= animCanvas.dynamicCircleRadius)
				{
					myHandler.removeCallbacks(this);
				}
				else
				{
				animCanvas.dynamicCircleRadius = animCanvas.dynamicCircleRadius
						- (animCanvas.backgroundCircleRadius - animCanvas.centerCircleRadius)
						/ (secondsExhale * fps);
			
				animCanvas.invalidate();

				myHandler.postDelayed(this, drawDelay);
				}
			}
		};

		startButton = (Button) findViewById(R.id.startButton);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				myHandler.post(reDrawInhale);
				myHandler.post(reDrawExhale);

			}
		});
	}

}
