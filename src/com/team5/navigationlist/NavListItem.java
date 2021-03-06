package com.team5.navigationlist;

import android.graphics.drawable.Drawable;

/**
 * Basic object representation of an navigation item
 * @author Nick
 *
 */
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