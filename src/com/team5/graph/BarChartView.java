package com.team5.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BarChartView extends View {
    private List<Bar> myData = new ArrayList<Bar>();
    private float maxValue = 0;
    private Paint myPaint = new Paint();

    // Required
    public BarChartView(Context context) {
        super(context);
    }

    // Required
    public BarChartView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    // Required
    public BarChartView(Context context, AttributeSet attributes, int defStyle) {
        super(context, attributes, defStyle);
    }

    public void addBar(int colour, float value) {
        myData.add(new Bar(colour, value));

        if (value > maxValue)
            maxValue = value;
    }

    public void onDraw(Canvas canvas) {
        // Collect values
        int totalBars = myData.size();
        float x = 0;
        float y = -1;
        float width = getWidth();
        float height = getHeight();
        float barWidth = 20;
        float xSpace = (width) / totalBars;
        float yScale = height / maxValue;

        // set axis colour
        myPaint.setColor(Color.BLACK);
        myPaint.setAntiAlias(true);
        myPaint.setStrokeWidth(1);

        // Draw x and y axis
        canvas.drawLine(x, y, x, y + height, myPaint);
        canvas.drawLine(x, y + height, x + width, y + height, myPaint);

        x += (xSpace / 4);
        // For each bar
        for (int i = 0; i < totalBars; i++) {
            // Get the bar
            Bar bar = myData.get(i);
            // set the colour
            myPaint.setColor(bar.getColour());

            // move to start of next bar
            x += (xSpace / 2) - (barWidth / 2);

            // draw the bar
            canvas.drawRect(x, y + height, x + barWidth, y + height - (bar.getValue() * yScale), myPaint);

            // move to end of bar
            x += (xSpace / 2) + (barWidth);
        }
    }
}

class Bar {
    private int myColour;
    private float myValue;

    public Bar(int colour, float value) {
        this.myColour = colour;
        this.myValue = value;
    }

    public int getColour() {
        return myColour;
    }

    public float getValue() {
        return myValue;
    }
}

