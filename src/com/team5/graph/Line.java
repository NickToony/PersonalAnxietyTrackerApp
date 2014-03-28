package com.team5.graph;

import com.team5.user.UserRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * An object representation of a line for a LineGraph
 * @author Nick
 *
 */
public class Line {
    private List<UserRecord> points = new ArrayList<UserRecord>();
    private int colour;

    public Line(int colour) {
        this.colour = colour;
    }

    public int getColour() {
        return colour;
    }

    public void addPoint(float x, float y, String thought, long time) {

        UserRecord userRecord = new UserRecord(time, (int) x, (int) y, thought);
        points.add(userRecord);
    }

    public float getX(int i) {
        return points.get(i).getAnxiety();
    }

    public float getY(int i) {
        return points.get(i).getSeriousness();
    }

    public long getTimeStamp(int i) {
        return points.get(i).getTimestamp();
    }

    public int getTotal() {
        // Collections.reverse(points);
        return points.size();
    }

    public String getThought(int i) {
        return points.get(i).getComments();
    }
    public void addBlankSpace(UserRecord userRecord, int index){
        points.add(index,userRecord);
    }
    public List<UserRecord> returnPointList() {

        return points;
    }
}