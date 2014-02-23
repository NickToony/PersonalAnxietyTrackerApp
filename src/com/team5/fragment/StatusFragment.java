package com.team5.fragment;

import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class StatusFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {
	// Constants that save the state if this application is closed
	private final String SERIOUSNESS_STATE = "serious";
	private final String ANXIETY_STATE = "anxiety";
	private final String SUMMARY_STATE = "summary";
	private final String COMMENT_STATE = "comment";

	// User input values
	private int seriousness;
	private int anxiety;
	private String summary;
	private String comment;

	// Widgets
	private View view;
	private Button save;
	private Button exit;
	private SeekBar seriousnessBar;
	private SeekBar anxietyBar;
	private EditText summaryText;
	private EditText commentText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.status_layout, container, false);

		initialiseComponentsAndSetListeners();

		if (savedInstanceState == null) {
			seriousness = 0;
			anxiety = 0;
			summary = null;
			comment = null;
		} else { // Initialized to saved variables
			seriousness = savedInstanceState.getInt(SERIOUSNESS_STATE);
			anxiety = savedInstanceState.getInt(ANXIETY_STATE);
			comment = savedInstanceState.getString(COMMENT_STATE);
		}
		
		return view;
	}

	/** Saves all data on a device state change **/
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(SERIOUSNESS_STATE, seriousness);
		outState.putInt(ANXIETY_STATE, anxiety);
		outState.putString(SUMMARY_STATE, summary);
		outState.putString(COMMENT_STATE, comment);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.statusSaveButton:
			UserRecord currentUR = new UserRecord(100, anxiety, seriousness,
					comment);

			if (((Session) getActivity().getApplication()).getUserAccount()
					.addRecord(currentUR)) {
				Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getActivity(), "Failure to store record",
						Toast.LENGTH_LONG).show();
			}

		case R.id.statusExitButton:
			getActivity().getActionBar().show();
			getFragmentManager().beginTransaction().remove(this).commit();
		}
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		switch (seekBar.getId()) {
		case R.id.newSeriousnessSeekBar:
			seriousness = seekBar.getProgress();
			break;
		case R.id.newAnxietySeekBar:
			anxiety = seekBar.getProgress();
			break;
		}
	}

	private void initialiseComponentsAndSetListeners() {
		save = (Button) view.findViewById(R.id.statusSaveButton);
		exit = (Button) view.findViewById(R.id.statusExitButton);
		seriousnessBar = (SeekBar) view.findViewById(R.id.newAnxietySeekBar);
		anxietyBar = (SeekBar) view.findViewById(R.id.newSeriousnessSeekBar);
		summaryText = (EditText) view.findViewById(R.id.summaryEditText);
		commentText = (EditText) view.findViewById(R.id.commentEditText);

		save.setOnClickListener(this);
		exit.setOnClickListener(this);
		seriousnessBar.setOnSeekBarChangeListener(this);
		anxietyBar.setOnSeekBarChangeListener(this);
	}

}
