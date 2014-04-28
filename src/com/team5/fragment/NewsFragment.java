package com.team5.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

/**
 * Fragment that displays a website, and manages a small in-app web browser
 * @author Nick
 *
 */
public class NewsFragment extends Fragment {
	private View myView;
	private HomeActivity myActivity;
	private ActionBar actionBar;
	private WebView myWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.news_fragment, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle(getResources().getString(R.string.navigation_news));

		myWebView = (WebView) myView.findViewById(R.id.news_fragment_webview);
		myWebView.loadUrl("http://nick-hope.co.uk/PAT/android/news.html");
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setSupportZoom(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		myWebView.setWebViewClient(new WebViewClient());

		return myView;
	}

	/**
	 * Handles backpress so that can use backbutton to go to previous website
	 * @author Nick
	 *
	 */
	public boolean doBackPress() {
		// If can go back in web browser
		if (myWebView.canGoBack()) {
			myWebView.goBack();
			return true;
		}
		// Otherwise, default back behaviour
		return false;
	}
}
