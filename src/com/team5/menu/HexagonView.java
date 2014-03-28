package com.team5.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Experimental menu concept as proposed by designers. Dropped due to impracticality.
 * NOTE: CircleView is FAR more optimised, and can do exactly the same task!
 * @author Nick
 *
 */
public class HexagonView extends View {
	// Class variables
	private Paint myPaint = new Paint();
	private Path myPath = new Path();
	private double currentSection = 0;
	private int targetSection = 0;
	private double rotateSpeed = 0.1;
	private List<Integer> mySections = new ArrayList<Integer>();
	Handler myHandler = new Handler();
	Runnable myTick = new Runnable() {
	    public void run() {
	    	handleRotate();
	        myHandler.postDelayed(this, 20); // 60fps
	    }
	};
	boolean myAnimation = false;
	
	// Required
	public HexagonView(Context context) {
		super(context);
	}
	
	// Required
	public HexagonView(Context context, AttributeSet attributes){
	    super(context, attributes);
	}

	// Required
	public HexagonView(Context context, AttributeSet attributes, int defStyle){
	    super(context, attributes, defStyle);
	}
	
	private int mod(int x, int y)	{
		// Java's mod does not work well with negatives. This fixes that problem.
		return (x % y + y) % y;
	}
	
	public void addSection(int colour)	{
		mySections.add(colour);
	}
	
	public void setTargetSection(int section)	{
		// Set the intended section
		targetSection = section;
		startRotation();
	}
	
	public int getCurrentSection()	{
		// Returns the currentsection (roughly)
		return (int) Math.round(currentSection);
	}
	
	private void handleRotate()	{		
		boolean updated = false;
		
		// If exact
		if (targetSection == currentSection)	{
			// Stop rotation
			stopRotation();
			return;
		}	else if (Math.abs(targetSection-currentSection) < rotateSpeed)	{ // If close enough
			// Set the current to the target, done
			currentSection = targetSection;
			updated = true;
		}	else if (targetSection > currentSection)	{ // if need to rotate clockwise
			currentSection += rotateSpeed;
			updated = true;
		}	else if (targetSection < currentSection)	{ // if need to rotate anti-clockwise
			currentSection -= rotateSpeed;
			updated = true;
		}
		
		// If there was a change
		if (updated)	{
			// Redraw the view
			invalidate();
		}
	}
	
	void startRotation() {
		// if not already animating
		if (myAnimation == false)	{
			// Reset the handler
			myHandler.removeCallbacks(myTick);
			// Give it the task
			myHandler.post(myTick);
			// animating
			myAnimation = true;
		}
	}

	void stopRotation() {
		// Reset the handler
	    myHandler.removeCallbacks(myTick);
	    // not animating
	    myAnimation = false;
	}
	
	public void onDraw(Canvas canvas)	{
		int width = getWidth();
		int height = getHeight();
		int midX = width/2;
		int midY = height;
		
		// Test data
		addSection(Color.rgb(74, 184, 85));
		addSection(Color.rgb(143, 76, 152));
		addSection(Color.rgb(83, 89, 163));
		addSection(Color.rgb(246, 58, 58));
		addSection(Color.rgb(233, 224, 73));
		addSection(Color.CYAN);
		
		// Count sections
		int sections = mySections.size();
		// No sections? Don't draw anything
		if (sections < 1)	{
			return;
		}
		
		// Calculate radius (pythagoras)
		double radius = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
		// Calculate angle of each section
		double x = Math.PI - 2*(Math.atan((double) height / (width/2)));
		// Calculate current
		int current = getCurrentSection();
		// Calculate starting angle
		double angle = -(x/2)	-((x)*(currentSection-current));
		
		// Set line settings
		myPaint.setAntiAlias(true);
		myPaint.setStrokeWidth(1);
		
		// Start i at 0
		int i = 0;
		// Move around until we are -90
		while (angle > -(Math.PI/2))	{
			// Shift the angle around
			angle -= x;
			// Add another section to draw
			i -= 1;
		}
		
		// While we still have space to fill
		while (angle < Math.PI/2)	{
			// Define colours
			int pos = mod(current + i, sections);
			
			// Define paint settings
			myPaint.setColor(mySections.get(pos));
			myPaint.setStyle(Style.FILL);
			
			// Reset the path variable
			myPath.reset();
			// Define first point
			myPath.moveTo(midX, midY);
			// Add x1, y1 of the section
			float x1 = (float) (midX + (radius * Math.sin(angle)));
			float y1 = (float) (midY - (radius * Math.cos(angle)));
			myPath.lineTo(x1, y1);
			// Increment angle
			angle += x;
			// Add x2, y2 of the section
			myPath.lineTo((float) (midX + (radius * Math.sin(angle))), (float) (midY - (radius * Math.cos(angle))));
			
			// Define last point
			myPath.lineTo(midX, midY);
			
			// Draw
			canvas.drawPath(myPath, myPaint);
			
			// Switch paint to black line
			myPaint.setColor(Color.BLACK);
			myPaint.setStyle(Style.STROKE);
			
			// Draw
			canvas.drawLine(midX, midY, x1, y1, myPaint);
			
			// Next section
			i ++;
		}
		
		// Switch paint to black line
		myPaint.setColor(Color.BLACK);
		myPaint.setStyle(Style.STROKE);
		
		// Draw bottom line
		canvas.drawLine(0, height-1, width, height-1, myPaint);
	}
}
