package com.team5.user;

import android.graphics.Color;

public class UserRecord {
	private int id = -1;
	private long timestamp;
	private float anxiety;
	private float seriousness;
    private  float x,y;
	private String comments;
    private int color;
    private boolean recorded;
    public UserRecord(){

    }

	
	public UserRecord(long timestamp, float anxiety, float seriousness, String comments)	{
		this.timestamp = timestamp;
		this.anxiety = anxiety;
		this.seriousness = seriousness;
		this.comments = comments;
        if(!comments.isEmpty()){
            recorded=true;
        }
	}
    public UserRecord(long timestamp, float x, float y,float anxiety, float seriousness, String comments)	{
        this.x=x;
        this.y=y;
        this.timestamp = timestamp;
        this.anxiety = anxiety;
        this.seriousness = seriousness;
        this.comments = comments;
        if(!comments.isEmpty()){
            recorded=true;
        }
    }
	public long getTimestamp()	{
		return timestamp;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id)	{
		this.id = id;
	}

	public float getAnxiety() {
		return anxiety;
	}

    public void setAnxiety(float anxiety){
        this.anxiety=anxiety;
    }
    public void setSeriousness(float seriousness){
        this.seriousness=seriousness;
    }


    public float getSeriousness() {
		return seriousness;
	}

	public String getComments() {
		return comments;
	}

    public void setColor(int color){
        this.color =color;
    }
    public int getColor(){
        return color;
    }

    public boolean getRecorded(){
        return recorded;
    }
    public int getTolerance(UserRecord userLineGraphPoint) {
        return (int) Math.sqrt((anxiety - userLineGraphPoint.getAnxiety())
                * (anxiety - userLineGraphPoint.getAnxiety())
                + (seriousness - userLineGraphPoint.getSeriousness())
                * (seriousness- userLineGraphPoint.getSeriousness()));
    }
}
