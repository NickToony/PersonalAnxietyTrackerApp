package com.team5.navigationlist;

import android.graphics.drawable.Drawable;

/** Objects shown every row **/
public class NavListItem {
	private String title;
	private Drawable icon;
	

	public NavListItem(String title, Drawable image) {
		this.title = title;
		this.icon = image;
	}

	public String getTitle() {
		return title;
	}

	public Drawable getImage() {
		return icon;
	}
}