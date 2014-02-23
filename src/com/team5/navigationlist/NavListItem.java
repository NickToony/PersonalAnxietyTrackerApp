package com.team5.navigationlist;

import android.graphics.drawable.Drawable;

public class NavListItem	{
	public Drawable myDrawable;
	public String myLabel;
	
	public NavListItem(Drawable drawable, String label)	{
		this.myDrawable = drawable;
		this.myLabel = label;
	}

	public Drawable getImage() {
		return myDrawable;
	}

	public String getLabel() {
		return myLabel;
	}
	
	
}