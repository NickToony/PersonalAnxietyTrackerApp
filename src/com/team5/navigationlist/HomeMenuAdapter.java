package com.team5.navigationlist;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeMenuAdapter extends NavListAdapter {
	private Context context;
	
	public HomeMenuAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)	{
		TextView textView;
		if (convertView == null) {
			textView = new TextView(context);
			textView.setGravity(Gravity.CENTER);
		} else {
			textView = (TextView) convertView;
		}
		
		NavListItem myItem = getItem(position);
		textView.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable( myItem.getImage() ), null, null);
		textView.setText(myItem.getLabel());
		return textView;
    }
}
