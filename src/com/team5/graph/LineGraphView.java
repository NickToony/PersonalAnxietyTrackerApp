package com.team5.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineGraphView extends View {
	private List<Line> myData = new ArrayList<Line>();
	// stores the cordinates for each line
	private List<Point> pointCordinates = new ArrayList<Point>();
	private Paint myPaint = new Paint();
	private float maxX = 0;
	private float maxY = 0;
	private final int gridSpace = 70;

	// touch tolerance from the user touch on screen and point on graph
	final int touchTolerance = 60;
	// Canvas
	private Canvas lineGraphCanvas;

	// Required
	public LineGraphView(Context context) {
		super(context);
	}

	// Required
	public LineGraphView(Context context, AttributeSet attributes) {
		super(context, attributes);
	}

	// Required
	public LineGraphView(Context context, AttributeSet attributes, int defStyle) {
		super(context, attributes, defStyle);
	}

	// Creates a line and returns it
	public int addLine(int colour) {
		// Create the line
		Line newLine = new Line(colour);

		// Add the line to the arraylist, so we know to render it
		myData.add(newLine);

		// Return the new line
		return myData.size() - 1;
	}

	public void clearIndex(int index) {
		myData.remove(index);
	}

	public List<Point> getLineGraphPointCordinates() {
		return pointCordinates;
	}

	public int getTouchTolerance() {
		return touchTolerance;
	}

	public void addPoint(int line, float x, float y, String thought) {
		Line theLine = myData.get(line);

		// Add the point to the line
		theLine.addPoint(x, y, thought);
		// Calculate if its a new maxX
		if (x > maxX)
			maxX = x;
		// and a new max Y?
		if (y > maxY)
			maxY = y;
	}

	@Override
	public void onDraw(Canvas canvas) {

		// Collect values
		int totalLines = myData.size();
		float x = 0;
		// dunno why you set it to -1 but i commented it out incase
		// something gets broke you will easily find where it went wrong
		// but everything seems to work fine
		// float y = -1;
		float y = 0;

		float width = getWidth();
		float height = getHeight();
		float xScale = width / maxX;
		float yScale = height / maxY;
		final int circleRadius = 10;

		// set axis colour
		myPaint.setColor(Color.BLACK);
		myPaint.setAntiAlias(true);
		myPaint.setStrokeWidth(1);

		// // Draw y axis
		// canvas.drawLine(x, y, x, y + height, myPaint);
		// //draw x axis
		// canvas.drawLine(x, y + height, x + width, y + height, myPaint);
		int mydistance = (int) Math.sqrt((x - x + width) * (x - x + width)
				+ (y + height - y + height) * (y + height - y + height));
		for (int i = 0; i < mydistance; i = i + gridSpace) {
			// x axis horizontal grid lines
			canvas.drawLine(x + i, y + height, x + i, y, myPaint);
			// y axis horizontal grid lines
			canvas.drawLine(x, y + i, x + width, y + i, myPaint);

			// Grid scale
			// canvas.drawText(String.format("%d",(int)x + i),x + i,y +
			// height,myPaint);
			// canvas.drawText(String.format("%d",(int)y+i),x, y + i,myPaint);

		}
		myPaint.setStrokeWidth(6);

		// For each line
		for (int lineNum = 0; lineNum < totalLines; lineNum++) {
			// Get Line
			Line line = myData.get(lineNum);
			// Change colour
			myPaint.setColor(line.getColour());
			
			// Calculate first points
			float lastX = x + (line.getX(0) * xScale);
			float lastY = y + height - (line.getY(0) * yScale);
			myPaint.setTextSize(20);

			String thought="";
			// For each point on the line
			int size = line.getTotal();
			for (int i = 0; i < size; i++) {
				thought= line.getThought(i);
				
				// Calculate next position
				float nextX = x + (line.getX(i) * xScale);
				float nextY = y + height - (line.getY(i) * yScale);

				// Draw line from last to next
				canvas.drawLine(lastX, lastY, nextX, nextY, myPaint);
				// Draws circle from the lastX and lastY with radius of 15
				canvas.drawCircle(lastX, lastY, circleRadius, myPaint);
				// display the values
				myPaint.setTextSize(20);
				canvas.drawText(String.format("%d", (int) line.getY(i)),
						lastX + 20, lastY - 30, myPaint);

				// add the co ordinate to the array
				pointCordinates.add(new Point(lastX, lastY, thought));

				// the next point is now the last
				lastX = nextX;
				lastY = nextY;
			}

			// Draw final points
			// Draws circle from the lastX and lastY with radius of 15
			canvas.drawCircle(lastX, lastY, circleRadius, myPaint);
			myPaint.setTextSize(20);
			lineGraphCanvas = canvas;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		// Create point object to store coordinates
		Point userTouch = new Point();
		switch (event.getAction()) {
		// when the user presses down
		case MotionEvent.ACTION_DOWN: {// stores the co-ordinates that they
										// press down on
			userTouch.setX((float) event.getX());
			userTouch.setY((float) event.getY());

			for (Point p : pointCordinates) {
				int distance = userTouch.getTolerance(p);
				if (distance < touchTolerance) {

//					Toast.makeText(getContext(),
//							String.format("%d , %d", x, y), Toast.LENGTH_SHORT)
//							.show();

				}
			}
		}

		}

		return true;
	}

}