package com.team5.exercise;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.team5.pat.R;

public class animationCanvas extends View
{
	public animationCanvas(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	public animationCanvas(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public animationCanvas(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	private Paint centerCirclePaint = new Paint();
	private Paint backgroundCirclePaint = new Paint();
	private Paint dynamicCirclePaint = new Paint();

	public float backgroundCircleRadius = 500.0f;
	public float centerCircleRadius = 150.0f;
	public float dynamicCircleRadius = 150.0f;

	public void onDraw(Canvas canvas)
	{
		centerCirclePaint.setColor(getResources().getColor(
				R.color.centerCircleColour));
		centerCirclePaint.setAntiAlias(true);

		dynamicCirclePaint.setColor(getResources().getColor(
				R.color.dynamicCircleColour));
		dynamicCirclePaint.setAntiAlias(true);

		backgroundCirclePaint.setColor(getResources().getColor(
				R.color.backgroundCircleColour));
		backgroundCirclePaint.setAntiAlias(true);

		float canvasWidth = (float) getWidth() / 2;
		float canvasHeight = (float) getHeight() / 2;

		canvas.drawCircle(canvasWidth, canvasHeight, backgroundCircleRadius,
				backgroundCirclePaint);
		canvas.drawCircle(canvasWidth, canvasHeight, dynamicCircleRadius,
				dynamicCirclePaint);
		canvas.drawCircle(canvasWidth, canvasHeight, centerCircleRadius,
				centerCirclePaint);

	}
}
