package com.team5.graph;

public class Point {
    private float x;
    private float y;
    private boolean recorded;
    private int seriousness;
    private String thought;

    private int anxiety;

    public Point(float x, float y, String thought) {
        this.x = x;
        this.y = y;
        this.thought = thought;
        if (!thought.trim().isEmpty()) {
            recorded = true;
        } else {
            recorded = false;
        }

    }

    public Point(float x, float y, String thought, int ser, int anx) {
        this.x = x;
        this.y = y;
        this.thought = thought;
        seriousness = ser;
        anxiety = anx;
        if (!thought.trim().isEmpty()) {
            recorded = true;
        } else {
            recorded = false;
        }

    }

    public String getInformation() {
        return thought;
    }

    public void setInformation(String thought) {
        this.thought = thought;
    }

    public int getSeriousness() {
        return seriousness;
    }

    public void setSeriousness(int seriousness) {
        this.seriousness = seriousness;
    }

    public int getAnxiety() {
        return anxiety;

    }

    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }

    public Point() {
        recorded = false;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getTolerance(Point userLineGraphPoint) {
        return (int) Math.sqrt((x - userLineGraphPoint.getX())
                * (x - userLineGraphPoint.getX())
                + (y - userLineGraphPoint.getY())
                * (y - userLineGraphPoint.getY()));
    }

    public void setRecorded(boolean bol) {
        this.recorded = bol;
    }

    public boolean getRecorded() {
        return recorded;
    }

}
