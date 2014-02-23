package com.team5.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

public class MainMenuFragment extends Fragment {
	private View myView;
	private HomeActivity myActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myView = inflater.inflate(R.layout.home_layout, container, false);
		myActivity = (HomeActivity) getActivity();
		
		// Setup icon grid
		GridView myIconGrid = (GridView) myView.findViewById(R.id.homeGrid);
		HomeScreenIconAdapter myAdapter = new HomeScreenIconAdapter(getActivity());
		myIconGrid.setAdapter(myAdapter);
		myAdapter.addIcon(R.drawable.ic_log, "Log");
		myAdapter.addIcon(R.drawable.ic_tracker, "Tracker");
		myAdapter.addIcon(R.drawable.ic_exercises, "Exercises");
		myAdapter.addIcon(R.drawable.ic_forums2, "Discussion");
		myAdapter.addIcon(R.drawable.ic_my_account, "My Account");
		myAdapter.addIcon(R.drawable.ic_find_help, "Find Help");
		myAdapter.addIcon(R.drawable.ic_report_issue, "Report Issue");
		myAdapter.addIcon(R.drawable.ic_log_off, "Log Off");
		myIconGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	handleIconClick(position);
            }
        });

		return myView;
	}
	
	protected void handleIconClick(int position) {
		switch (position)	{
		case 0: // Log
			myActivity.changeFragment(new SeekBarFragment());
			break;
		case 1: // Tracking
			myActivity.changeFragment(new GraphFragment());
			break;
		case 2: // Exercises
			myActivity.changeFragment(new BreathExerciseFragment());
			break;
		case 3: // Discussion
			myActivity.changeFragment(new SocialFragment());
			break;
		case 4: // My Account
			break;
		case 5: // Find Help
			
			break;
		case 6: // Report issues
			
			break;
		case 7: // Log out
			myActivity.finish();
			break;
		}
		
	}
	
class HomeScreenIconAdapter extends BaseAdapter	{
		
		private Context myContext;
		private List<Icon> myIcons = new ArrayList<Icon>();
		private class Icon	{			
			public int drawable;
			public String text;
			
			public Icon(int drawable, String text)	{
				this.drawable = drawable;
				this.text = text;
			}
		}
		
		public HomeScreenIconAdapter(Context context)	{
			this.myContext = context;
		}
		
		public void addIcon(int drawable, String text)	{
			myIcons.add(new Icon(drawable, text));
		}
		
		@Override
		public int getCount()	{
			return myIcons.size();
		}
		
		@Override
		public Object getItem(int position)	{
			return myIcons.get(position);
		}
		
		@Override
		public long getItemId(int position)	{
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent)	{
			TextView textView;
			if (convertView == null) {
				textView = new TextView(myContext);
				textView.setGravity(Gravity.CENTER);
			} else {
				textView = (TextView) convertView;
			}
			
			Icon theIcon = (Icon) getItem(position);
			
			textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable( theIcon.drawable ), null, null);
			textView.setText(theIcon.text);
			return textView;
	    }
	}
}
