package com.team5.navigationlist;

import android.graphics.drawable.Drawable;

public class NavListItem	{
	public int myDrawable;
	public int myLabel;
	
	public NavListItem(int drawable, int label)	{
		this.myDrawable = drawable;
		this.myLabel = label;
	}

	public int getImage() {
		return myDrawable;
	}

	public int getLabel() {
		return myLabel;
	}
	
	
}