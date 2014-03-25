package com.team5.social;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RatingBar;

import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;

class RatingTouchListener implements OnTouchListener, NetworkInterface, OnClickListener {
	private RatingBar starRatingBelow;
	private RatingBar starRatingAbove;
	private RatingBar starRatingUser;
	private boolean ratingUserSet = false;
	private Post post;
	SocialAccount mySocialAccount;
	
	public void setRatingBars(Post post, RatingBar starRatingBelow, RatingBar starRatingAbove, RatingBar starRatingUser, SocialAccount socialAccount)	{
		this.post = post;
		this.starRatingBelow = starRatingBelow;
		this.starRatingAbove = starRatingAbove;
		this.starRatingUser = starRatingUser;
		this.mySocialAccount = socialAccount;
		
		starRatingAbove.setOnTouchListener(this);
		starRatingBelow.setOnTouchListener(this);
		starRatingUser.setOnTouchListener(this);
	}
	
    @Override
    public boolean onTouch(View v, MotionEvent event) {
    	if (ratingUserSet)
    		return true;
    	
        if (event.getAction() == MotionEvent.ACTION_UP) {
             float stars = (event.getX() / starRatingUser.getWidth()) * 5.0f;
             setMyRating(stars);
             
             Request r = new Request(this, "http://nick-hope.co.uk/PAT/android/rate.php", mySocialAccount.getCookies());
             r.addParameter("post", post.id + "");
             r.addParameter("rating", stars + "");
             r.start();
             
             v.setPressed(false);
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.setPressed(true);
        }

        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.setPressed(false);
        }
        return true;
    }
    
    public void setOthersRating(float value)	{
		starRatingBelow.setRating(value);
		starRatingAbove.setRating(value);
		
		fixRating();
	}
	
	void setMyRating(float value)	{
		starRatingUser.setRating(value);
		ratingUserSet = true;
		
		fixRating();
	}
	
	private void fixRating()	{
		 if (starRatingUser.getRating() <= starRatingAbove.getRating())	{
        	 starRatingAbove.setVisibility(View.GONE);
        	 starRatingBelow.setVisibility(View.VISIBLE);
         }	else	{
        	 starRatingBelow.setVisibility(View.GONE);
        	 starRatingAbove.setVisibility(View.VISIBLE);
         }
	}

	@Override
	public void eventNetworkResponse(Request from, Response response) {
		
	}

	public void resetMyRating() {
		starRatingUser.setRating(0);
		fixRating();
	}

	@Override
	public void onClick( View v) {
		mySocialAccount.changeFragment(new AddTopicFragment().defineParent(post));
	}

};