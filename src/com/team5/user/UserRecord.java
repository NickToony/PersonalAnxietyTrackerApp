package com.team5.user;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRecord implements Serializable {
	private int id = -1;
	private long timestamp;

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	private float anxiety;
	private float seriousness;
	Parcelable.Creator<UserRecord> p;

	private float x, y;
	private String comments;

	public void setComments(String comments) {
		this.comments = comments;
	}

	private int color;
	private boolean recorded;

	public UserRecord() {

	}

	UserRecord(Parcel in) {

		this.x = in.readFloat();
		this.y = in.readFloat();
		this.timestamp = in.readLong();
		this.anxiety = in.readFloat();
		this.seriousness = in.readFloat();
		this.comments = in.readString();

	}

	public UserRecord(long timestamp, float anxiety, float seriousness,
			String comments) {
		this.timestamp = timestamp;
		this.anxiety = anxiety;
		this.seriousness = seriousness;
		this.comments = comments;
		if (!comments.isEmpty()) {
			recorded = true;
		}
	}

	public UserRecord(long timestamp, float x, float y, float anxiety,
			float seriousness, String comments, int color) {
		this.x = x;
		this.y = y;
		this.timestamp = timestamp;
		this.anxiety = anxiety;
		this.seriousness = seriousness;
		this.comments = comments;
		if (!comments.isEmpty()) {
			recorded = true;
		}
		this.color = color;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getAnxiety() {
		return anxiety;
	}

	public void setAnxiety(float anxiety) {
		this.anxiety = anxiety;
	}

	public void setSeriousness(float seriousness) {
		this.seriousness = seriousness;
	}

	public float getSeriousness() {
		return seriousness;
	}

	public String getComments() {
		return comments;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public boolean getRecorded() {
		return recorded;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getTolerance(UserRecord userLineGraphPoint) {
		return (int) Math.sqrt((x - userLineGraphPoint.getX())
				* (x - userLineGraphPoint.getX())
				+ (y - userLineGraphPoint.getY())
				* (y - userLineGraphPoint.getY()));
	}

}
