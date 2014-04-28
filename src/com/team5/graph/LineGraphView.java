package com.team5.graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.team5.pat.R;
import com.team5.user.UserRecord;

public class LineGraphView extends View {
    private List<Line> myData = new ArrayList<Line>();
    // stores the co-ordinates for each line
    private List<UserRecord> anxietyCordinates = new ArrayList<UserRecord>();
    private List<UserRecord> seriousnessCordinates = new ArrayList<UserRecord>();

    private Paint myPaint = new Paint();
    private float maxX = 0;
    private float maxY = 0;
    private int onDrawStart=0;
    private int onDrawStop=myData.size();
    private float xScale, yScale;
    private final int rating = 10;
    private int maxXLines = 0;
    private int canvasPadding = 50;
    private int spinnerPosition = 0;
    private String[] titles;

    // touch tolerance from the user touch on screen and point on graph
    final int touchTolerance = 60; 

    public void setGridSpace(int xlines, int position) {
        maxXLines = xlines;
        spinnerPosition = position;
        //set the x axis labels to weeks e.g. mon, tue, wed
        if (position == 0) {

            titles = getResources().getStringArray(
                    R.array.x_axis_grid_week);
        }
        //set the x axis to month e.g. week 1, week2 etc
        if (position == 1) {
            titles = getResources().getStringArray(
                    R.array.x_axis_grid_month);
        }
        if (position == 2) {
            titles = getResources().getStringArray(
                    R.array.x_axis_grid_year);
        }

    }

    public int getTitlesLength() {
        return titles.length;
    }
    public void setOnDrawStart(int start){
    	onDrawStart=start;
    }
    public void setOnDrawStop(int stop){
    	onDrawStop=stop;
    }
    public float getxScale() {
        return xScale;
    }
    public int getMaxXLines(){
    	return maxXLines;
    }

    public float getyScale() {
        return yScale;
    }

    public List<Line> getMyData() {
        return myData;
    }

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
    public int getLineDataSize(){
    	return myData.size();
    }

    public void clearLineDataIndex(int index) {
        myData.remove(index);
    }

    public void clearRecords() {
        myData.clear();
    }
    public void clearAnxietyPoints(){
    	anxietyCordinates.clear();
    }
    
    //clear the seriousness points
    public void clearSeriousnessPoints(){
    	seriousnessCordinates.clear();
    }

    public List<UserRecord> getAnxietyCordinates() {
        return anxietyCordinates;
    }
    public List<UserRecord> getSeriousCordinates() {
        return seriousnessCordinates;
    }

    public int getTouchTolerance() {
        return touchTolerance;
    }

    public void addPoint(int line, float x, float y, String thought, long date) {
        Line theLine = myData.get(line);


        // Add the point to the line
        theLine.addPoint(x, y, thought, date);
        // Calculate if its a new maxX
        if (x > maxX)
            maxX = x;
        // and a new max Y?
        if (y > maxY)
            maxY = y;
    }

    @Override
    public void onDraw(Canvas canvas) {
//        int totalLines = myData.size();
        float x = 0;
        float y = 0;


        float width = canvas.getWidth() - canvasPadding;
        //create space at the bottom for scales
        float height = getHeight() - canvasPadding;
        xScale = ((width) / maxXLines);
        yScale = height / rating;
        final int circleRadius = 10;
        myPaint.setColor(Color.rgb(223, 240, 255));
        int scale = 1;

        //use canvasPadding to create space at the left for scales
        //use gridspace to make sure the space between grids is drawn evenly
        canvas.drawRect(canvasPadding, 0, width+canvasPadding, height, myPaint);
        myPaint.setStrokeWidth(6);
        myPaint.setColor(Color.BLACK);
        canvas.drawLine(0, height, width+canvasPadding, height, myPaint);
        //vertical
        canvas.drawLine(canvasPadding, y, canvasPadding, height + canvasPadding, myPaint);


        // set axis colour
        myPaint.setColor(Color.MAGENTA);
        myPaint.setAntiAlias(true);
        myPaint.setStrokeWidth(3);

        //draw the vertical grid lines
        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(30);
        Line line = null;
        // For each line
        for (int lineNum = onDrawStart; lineNum < onDrawStop; lineNum++) {
            // Get Line
            line = myData.get(lineNum);
            // Change colour
            myPaint.setColor(line.getColour());
            // For each point on the line
            int size = line.getTotal();
            // Calculate first points
//            float start = whereToStart(line,0);
//            float startPoint = xScale*start;

//            float lastX = (x + (line.getX(0) * xScale)+canvasPadding )+startPoint;
            float lastX = (x + (line.getX(0) * xScale)+canvasPadding );

            float lastY = y + height - (line.getY(0) * yScale);
            UserRecord use;
            //store the lines in 2 different list
            if(line.getColour()==Color.BLUE)
            {
               use = new UserRecord(line.getTimeStamp(0),lastX,lastY,(int)line.getY(0),(int)line.getX(0),line.getThought(0),line.getColour());

            anxietyCordinates.add(use);
            }
            
            else{
            use = new UserRecord(line.getTimeStamp(0),lastX,lastY,(int)line.getX(0),(int)line.getY(0),line.getThought(0),line.getColour());

            seriousnessCordinates.add(use);
            }


            String thought = "";
            myPaint.setStrokeWidth(4);
            myPaint.setTextSize(20);

            for (int i = 1; i < size; i++) {



                thought = line.getThought(i);

                // Calculate next position
//                float nextX = x + ((line.getX(i) * xScale)+canvasPadding)+startPoint;
                float nextX = x + ((line.getX(i) * xScale)+canvasPadding);

                float nextY = y + height - (line.getY(i) * yScale);
                myPaint.setTextSize(25);

                canvas.drawText(String.format("%d", (int) line.getY(i)),
                        nextX + 20, nextY + 30, myPaint);

                // Draw line from last to next

                canvas.drawLine(lastX, lastY, nextX, nextY, myPaint);
                // Draws circle from the lastX and lastY with radius of 15
              

                if(line.getColour()==Color.BLUE)
                {
                    canvas.drawCircle(lastX, lastY, circleRadius, myPaint);
                    use = new UserRecord(line.getTimeStamp(i),nextX,nextY,(int)line.getY(i),(int)line.getX(i),line.getThought(i),line.getColour());
                anxietyCordinates.add(use);
                }
                else{
                	canvas.drawRect(lastX-20, lastY-10, lastX+10, lastY+20, myPaint);
                	use= new UserRecord(line.getTimeStamp(i),nextX,nextY,(int)line.getX(i),(int)line.getY(i),line.getThought(i),line.getColour());

                seriousnessCordinates.add(use);
                }                // the next point is now the last
                lastX = nextX;
                lastY = nextY;
            }

            // Draw final points
            // Draws circle from the lastX and lastY with radius of 15
            if(line.getColour()==Color.RED){
            	canvas.drawRect(lastX-20, lastY-10, lastX+10, lastY+20, myPaint);
            }else{
                canvas.drawCircle(lastX, lastY, circleRadius, myPaint);
            }

        }


        int tempWidth = (int) width;
        myPaint.setStrokeWidth(1);

        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(30);
        int count = 0;
        int i;
        //x axis vertical lines
        // |
        // |
        // |
        // |
        // |
        
        for (i = canvasPadding; i <= tempWidth; i = (int) (i + xScale)) {


            canvas.drawLine(x + i, y, x + i, y + height + (canvasPadding / 2), myPaint);
            if (titles!=null && count< titles.length) {
                canvas.drawText(titles[count], x + i, (y + height) + (canvasPadding), myPaint);
                count++;
            }


        }
        //The y axis scale
        scale = 10;
        int tempHeight = (int) height;
        myPaint.setStrokeWidth(1);

        // y axis horizontal lines grid and y axis labelling ---------------
        myPaint.setColor(Color.BLACK);
        for (i = 0; i <= tempHeight; i = (int) (i + yScale)) {
        	//draw line and format the text
            canvas.drawLine(x + (canvasPadding / 2), y + i, x + canvas.getWidth(), y + i, myPaint);
            canvas.drawText(String.format("%d", (int) scale), x + 8, y + i + 10, myPaint);
            scale--;


        }
    }

//    private float whereToStart(Line line,int index) {
//        int dayOfWeek = 0;
//        int weekOfMonth=0;
//        int monthOfYear=0;
//        if (spinnerPosition == 0) {
//            long time = line.getTimeStamp(index);
//            Calendar cal = Calendar.getInstance();
//            cal.setFirstDayOfWeek(Calendar.SUNDAY);
//            cal.setTimeInMillis(time);
//            Date d = new Date();
//            d.setTime(cal.getTime().getTime());
//            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-Calendar.SUNDAY;
//            return  dayOfWeek-1;
//        }
//        if(spinnerPosition==1){
//            long time = line.getTimeStamp(index);
//            Calendar cal = Calendar.getInstance();
//            cal.setTimeInMillis(time);
//            Date d = new Date();
//            d.setTime(cal.getTime().getTime());
//            weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
//   
//            return weekOfMonth-weekOfMonth;
//
//        }
//        if(spinnerPosition==2){
//            long time = line.getTimeStamp(index);
//            Calendar cal = Calendar.getInstance();
//            cal.setTimeInMillis(time);
//            Date d = new Date();
//            d.setTime(cal.getTime().getTime());
//            monthOfYear = cal.get(Calendar.MONTH);
//            return monthOfYear;
//        }
//        return 0;
//    }
 
    //makes a date from 2 periods of time
    public List<Date> generateDateListBetween(Date startDate, Date endDate)
	{
	    //Flip the input if necessary, to prevent infinite loop
	    if(startDate.after(endDate))
	    {
	        Date temp = startDate;
	        startDate = endDate;
	        endDate = temp;
	    }

	    List<Date> resultList = new ArrayList<Date>();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(startDate);

	    do
	    {
	        resultList.add(cal.getTime());
	        cal.roll(Calendar.DAY_OF_YEAR, true);  //Roll one day forwards         
	    }
	    while(cal.getTime().before(endDate));

	    return resultList;
	}
    //takes 2 dates and generates months between them
    //e.g. jan and april, it will generate jan, feb,, march, april
    //then returns it
    public  List<Date> generateMonthBetween(Date from, Date to){
	    List<Date> resultList = new ArrayList<Date>();
	    //take the date from and generate a month of the year from their onwards to december
	    int monthFrom = from.getMonth();
	    int	monthTo =to.getMonth();
	    for(int i=monthFrom;i<=monthTo;i++){
	    	Date newDate = new Date();
	    	newDate.setMonth(i);
	    	newDate.setYear(from.getYear());
	    	resultList.add(newDate);
	    }
		return resultList;

    	
    }
    //Generates weeks for a month
    public List<Date> generateWeeksForMonth(Date from, Date to){
    	//generate a whole months date
    	List<Date> dates = generateDateListBetween(from, to);
    	List<Date> weeks = new ArrayList<Date>();
    	for(int i=0;i<dates.size();i++){
    		
    		if(weeks.isEmpty()){
    			weeks.add(dates.get(i));
    		}else {
    			Calendar c1 = Calendar.getInstance();
    			c1.setTimeInMillis(weeks.get(weeks.size()-1).getTime());
    			Calendar c2 = Calendar.getInstance();

    			c2.setTimeInMillis(dates.get(i).getTime());
    			if(c1.get(Calendar.WEEK_OF_YEAR)!=c2.get(Calendar.WEEK_OF_YEAR)){
    				weeks.add(dates.get(i));
    			}
    		}
    		
    	}
    	//remove the first one because its a duplicate
    	weeks.remove(0);
    	return weeks;
    }
    


    	
}
