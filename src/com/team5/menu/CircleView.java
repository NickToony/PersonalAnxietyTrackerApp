package com.team5.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Experimental menu concept as proposed by design team.
 * @author Nick
 *
 */
public class CircleView extends View {
	// Variables
	private List<MenuItem> mySections = new ArrayList<MenuItem>();
	private Paint myPaint = new Paint();
	private RectF myRect;
	private int targetSection = 0;
	private double currentSection = 0;
	private double rotateSpeed = 0.15;
	Handler myHandler = new Handler();
	Runnable myTick = new Runnable() {
		public void run() {
			handleRotate();
			myHandler.postDelayed(this, 20); // 60 fps
		}
	};
	boolean myAnimation = false;

	/** Constructor **/
	public CircleView(Context context) {
		super(context);
	}

	/** Constructor **/
	public CircleView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	/** Constructor **/
	public CircleView(Context context, AttributeSet attributes, int defStyle) {
		super(context, attributes, defStyle);
	}

	public int getTargetSection() {
		return targetSection;
	}

	public void setTargetSection(int target) {
		targetSection = target;
		startRotation();
	}

	/** @return x mod y for both positive and negative numbers **/
	private int mod(int x, int y) {

		return (x % y + y) % y;
	}

	private void handleRotate() {
		boolean updated = false;

		// If exact
		if (targetSection == currentSection) {
			// Stop rotation
			stopRotation();
			return;
		} else if (Math.abs(targetSection - currentSection) < rotateSpeed) {
			// If two sessions are close enough, set the current to the target
			currentSection = targetSection;
			updated = true;
		} else if (targetSection > currentSection) { // rotate clockwise
			currentSection += rotateSpeed;
			updated = true;
		} else if (targetSection < currentSection) { // rotate anti-clockwise
			currentSection -= rotateSpeed;
			updated = true;
		}

		// If there was a change, redraw the view
		if (updated) {
			invalidate();
		}
	}

	/** Start animation if it has not started **/
	void startRotation() {
		if (myAnimation == false) {
			myHandler.removeCallbacks(myTick); // reset the handler
			myHandler.post(myTick); // give it the task
			myAnimation = true; // start animation
		}
	}

	void stopRotation() {
		myHandler.removeCallbacks(myTick); // reset the handler
		myAnimation = false; // stop animation
	}

	public void addItem(String myTitle, int myColour) {
		MenuItem item = new MenuItem(myTitle, myColour); // create the menu item
		mySections.add(item); // add the item to our render list
		invalidate(); // redraw the wheel
	}

	// Returns a specific item, in case its needed for some reason
	public MenuItem getItem(int i) {
		return mySections.get(i);
	}

	// Temporary?
	public List<MenuItem> getItemList() {
		return mySections;
	}

	public void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();

		double scale = .94; // scale variable

		// Define the draw area for the arcs
		if (myRect == null)
			myRect = new RectF((int) ((1 - scale) * width * 2),
					(int) ((1 - scale) * height), (int) (width * 2 * scale),
					(int) (height * scale));

		double sectionAngle = 90; // section angles
		double currentAngle = 45 + (90 * (1 - (currentSection - Math
				.round(currentSection)))); // current angle of the wheel

		int i = (int) Math.round(currentSection); // get the current segment

		// While we can still fit more
		while (currentAngle > 90) {
			i--; // add another i to render
			currentAngle -= sectionAngle; // increment
		}

		myPaint.setAntiAlias(true); // makes lines smoother
		myPaint.setStrokeWidth(1); // makes lines more visible

		// Do until off screen
		while (currentAngle < 270) {
			MenuItem item = mySections.get(mod(i, mySections.size()));

			// Draw segment with fill style and speified colour
			myPaint.setStyle(Style.FILL);
			myPaint.setColor(item.getColour());
			canvas.drawArc(myRect, (float) currentAngle, (float) sectionAngle,
					true, myPaint);

			// Draw segment outline with stroke style and black in colour
			myPaint.setStyle(Style.STROKE);
			myPaint.setColor(Color.BLACK);
			canvas.drawArc(myRect, (float) currentAngle, (float) sectionAngle,
					true, myPaint);

			// Increment to next segment
			currentAngle += sectionAngle;
			i++;
		}

		// Draw the final line with stroke style and black in colour
		myPaint.setStyle(Style.STROKE);
		myPaint.setColor(Color.BLACK);
		canvas.drawLine(width - 1, (int) ((1 - scale) * height), width - 1,
				(int) (height * scale), myPaint);
	}
}

class MenuItem {
	private String myTitle;
	private int myColour;

	public MenuItem(String myTitle, int myColour) {
		this.myTitle = myTitle;
		this.myColour = myColour;
	}

	public String getTitle() {
		return myTitle;
	}

	public int getColour() {
		return myColour;
	}
}
