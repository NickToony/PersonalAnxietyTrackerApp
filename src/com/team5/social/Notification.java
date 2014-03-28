package com.team5.social;

public class Notification {
	public int notificationID;
	public int postID;
	public String date;
	public String content;
	
	public Notification(int notificationID, int postID, String date, String content) {
		this.notificationID = notificationID;
		this.postID = postID;
		this.date = date;
		this.content = content;
	}
}
