package com.team5.social;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains all data of a post
 * 
 * All social fragments responsibility of Nick, do not modify
 * @author Nick
 *
 */
class Post implements Parcelable {
	public String name;
	public String date;
	public String content;
	public int replies;
	public float rating;
	public float myRating;
	public boolean mine;
	public int favourites;
	public boolean favourited;
	
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
		this.favourites = parcIn.readInt();
		this.favourited = parcIn.readByte() != 0;
		this.mine = parcIn.readByte() != 0;
	}
	
	public Post(int id, String name, String date, String content, int replies, float rating, int ownerId, float postMyRating, int mine, int favourites, int favourited)	{
		this.id = id;
		this.name = name;
		this.date = date;
		this.content = content;
		this.replies = replies;
		this.rating = rating;
		this.ownerId = ownerId;
		this.myRating = postMyRating;
		this.mine = false;
		if (mine == 1)
			this.mine = true;
		this.favourites = favourites;
		this.favourited  = false;
		if (favourited == 1)
			this.favourited = true;
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
		parcOut.writeInt(favourites);
		parcOut.writeByte((byte) (favourited ? 1 : 0));
		parcOut.writeByte((byte) (mine ? 1 : 0));
		
	}
}