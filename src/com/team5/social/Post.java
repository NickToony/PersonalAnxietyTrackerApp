package com.team5.social;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * ListItem
 * 
 * Contains data of each item in the list
 */
class Post implements Parcelable {
	public String name;
	public String date;
	public String content;
	public int replies;
	public float rating;
	public float myRating;
	
	public int id;
	public int ownerId;
	
	public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
	    public Post createFromParcel(Parcel in) {
	        return new Post(in);
	    }
	
	    public Post[] newArray(int size) {
	        return new Post[size];
	    }
	};
	
	private Post(Parcel parcIn)	{
		this.name = parcIn.readString();
		this.date = parcIn.readString();
		this.content = parcIn.readString();
		this.replies = parcIn.readInt();
		this.rating = parcIn.readFloat();
		this.myRating = parcIn.readFloat();
		this.id = parcIn.readInt();
		this.ownerId = parcIn.readInt();
	}
	
	public Post(int id, String name, String date, String content, int replies, float rating, int ownerId, float postMyRating)	{
		this.id = id;
		this.name = name;
		this.date = date;
		this.content = content;
		this.replies = replies;
		this.rating = rating;
		this.ownerId = ownerId;
		this.myRating = postMyRating;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcOut, int arg1) {
		parcOut.writeString(name);
		parcOut.writeString(date);
		parcOut.writeString(content);
		parcOut.writeInt(replies);
		parcOut.writeFloat(rating);
		parcOut.writeFloat(myRating);
		parcOut.writeInt(id);
		parcOut.writeInt(ownerId);
	}
}