package com.team5.social;

import java.util.Map;

import com.team5.pat.HomeActivity;

import android.app.Fragment;

public interface SocialFragmentInterface {
	public void setParentFragment(SocialFragmentInterface frag);
	public void setCookies(Map<String, String> cookieMap);
	public Map<String, String> getCookies();
	public void eventChild(int eventID);
}
