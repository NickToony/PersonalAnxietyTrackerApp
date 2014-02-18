package com.team5.graph;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
	private List<Section> myData = new ArrayList<Section>();
	private float totalValue = 0;
	private Paint myPaint = new Paint();
	private RectF myRect;
	
	
	// Required
	public PieChartView(Context context) {
		super(context);
	}
	
	// Required
	public PieChartView(Context context, AttributeSet attributes){
	    super(context, attributes);
	}

	// Required
	public PieChartView(Context context, AttributeSet attributes, int defStyle){
	    super(context, attributes, defStyle);
	}
		
	// Add a value
	public void addSection(int colour, float value)	{
		myData.add(new Section(colour, value));
		
		totalValue += value;
	}
	
	@Override
	public void onDraw(Canvas canvas)	{
		// Gather values
		int totalSections = myData.size();
		float x = 0;
		float y = 0;
		float width = getWidth();
		float height = getHeight();
		
		if (width > height)	{
			//y -= (height - width)/2;
			x += (width - height)/2;
			width += (height - width)/2;
		}	else	{
			y += (height - width)/2;
			height -= (height - width)/2;
			
		}
		
		// Basic stuff
		myPaint.setAntiAlias(true);
		if (myRect == null)
			myRect = new RectF(x, y, width, height);
		
		float currentAngle = 0;
		for (int i = 0; i < totalSections; i ++){
			Section section = myData.get(i);
			
			myPaint.setColor(section.getColour());
			
			float angle = 360 * (section.getValue()/totalValue);
			canvas.drawArc(myRect, currentAngle, angle, true, myPaint);
			
			currentAngle += angle;
		}
	}
}

class Section {
	private int myColour;
	private float myValue;
	
	public Section(int colour, float value)	{
		this.myColour = colour;
		this.myValue = value;
	}
	
	public int getColour()	{
		return myColour;
	}
	
	public float getValue()	{
		return myValue;
	}
}

