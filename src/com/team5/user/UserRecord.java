package com.team5.user;

public class UserRecord {
	private int id = -1;
	private long timestamp;
	private int anxiety;
	private int seriousness;
	private String comments;
	
	public UserRecord(long timestamp, int anxiety, int seriousness, String comments)	{
		this.timestamp = timestamp;
		this.anxiety = anxiety;
		this.seriousness = seriousness;
		this.comments = comments;
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

	public int getAnxiety() {
		return anxiety;
	}

	public int getSeriousness() {
		return seriousness;
	}

	public String getComments() {
		return comments;
	}
}
