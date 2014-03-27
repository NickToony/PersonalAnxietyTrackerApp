package com.team5.fragment;

import java.util.Date;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
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

public class SeekBarFragment extends Fragment implements OnClickListener {
	// Constants that save the state if the app is closed
	private static final String SERIOUSNESS_STATE = "";
	private static final String ANXIETY_STATE = "";
	private static final String COMMENTS = "";

	// Variables
	private int seriousnessState = 5;
	private int anxietyState = 5;
	private String comments = "";

	// Initialize the objects
	private Button seekBarSubmitButton;
	private SeekBar anxietySeekBar;
	private SeekBar seriousnessSeekBar;
	private EditText commentsTextField;

	private View myView;
	private HomeActivity myActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.seek_bar_layout, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle(getResources().getString(R.string.navigation_log));
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// If just started initialize to 0
		if (savedInstanceState == null) {
			seriousnessState = 0;
			anxietyState = 0;
			comments = "";
		} else { // Initialized to saved variables
			seriousnessState = savedInstanceState.getInt(SERIOUSNESS_STATE);
			anxietyState = savedInstanceState.getInt(ANXIETY_STATE);
			comments = savedInstanceState.getString(COMMENTS);
		}

		initialiseSeekBarComponents();

		seekBarSubmitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get the date
				long theDate = new Date().getTime();

				// Create the record
				UserRecord currentUR = new UserRecord(theDate, anxietyState,
						seriousnessState, comments);

				if (((Session) getActivity().getApplication()).getUserAccount()
						.addRecord(currentUR)) {
					Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(getActivity(), "Failure to store record",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// sideMenuButton.setOnClickListener(this);
		seriousnessSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						seriousnessState = seriousnessSeekBar.getProgress();
					}

				});
		anxietySeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						anxietyState = anxietySeekBar.getProgress();
					}
				});
		return myView;

	}

	/** Saves all data on a device state change **/
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(SERIOUSNESS_STATE, seriousnessState);
		outState.putInt(ANXIETY_STATE, anxietyState);
		outState.putString(COMMENTS, comments);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	private void initialiseSeekBarComponents() {

		anxietySeekBar = (SeekBar) myView.findViewById(R.id.anxietySeekBar);
		anxietySeekBar.setProgress(anxietyState);

		seriousnessSeekBar = (SeekBar) myView
				.findViewById(R.id.seriousnessSeekBar);
		seriousnessSeekBar.setProgress(seriousnessState);

		commentsTextField = (EditText) myView
				.findViewById(R.id.commentsEditText);
		commentsTextField.setText(comments);

		seekBarSubmitButton = (Button) myView
				.findViewById(R.id.seekBarSubmitButton);
	}
}
