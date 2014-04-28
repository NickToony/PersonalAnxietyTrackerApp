package com.team5.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.team5.pat.R;
import com.team5.user.UserRecord;

public class UserRecordAdapter extends ArrayAdapter<UserRecord> {
TextView anxiety;
TextView seriousness;
TextView thought;
TextView date;
	public UserRecordAdapter(Context context, int resource,
			List<UserRecord> objects) {
		super(context, R.layout.user_record_list, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		UserRecord use = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_record_list, null);
		}
		anxiety = (TextView) convertView.findViewById(R.id.anxiety_text);
		seriousness = (TextView) convertView.findViewById(R.id.seriousness_text);
		thought = (TextView) convertView.findViewById(R.id.thought_text);
		date = (TextView) convertView.findViewById(R.id.date_text);

		anxiety.setText("Anxiety:   "+use.getAnxiety());
		seriousness.setText("Seriousness:   "+use.getSeriousness());
		thought.setText("Your Thoughts: \n"+use.getComments());
		Date d = new Date();
		d.setTime(use.getTimestamp());
//		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		
		date.setText(""+d);

		return convertView;
	}

}
