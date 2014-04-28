package com.team5.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.user.UserRecord;

public class UserRecordList extends Fragment {
	private View myView;
	ListView list;
	private List<UserRecord> dataPassed;
	private HomeActivity myActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		myView = inflater.inflate(R.layout.agenda_result_list, container, false);
//		dataPassed = new ArrayList<UserRecord>();
//		UserRecord use = new UserRecord();
//		use.setComments("here is a comment");
//		dataPassed.add(use);
		myActivity = (HomeActivity) getActivity();
		list =(ListView) myView.findViewById(R.id.listView);
			UserRecordAdapter adapter = new UserRecordAdapter(getActivity(),R.id.anxiety_text, dataPassed);
			list.setAdapter(adapter);
			//adapter.add(dataPassed.get(0));
		return myView;
		//date_text_view anxiety_text_view seriousness_text_view seriousness_edit_text anxiety_edit_text

	}
	public Fragment passDataAcross(List<UserRecord> data){
		this.dataPassed = data;
		return this;
	}

}
