package com.team5.fragment;

import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

/**
 * Rework of the SeekBarFragment to be more compact and displayed as a dropdown. No long used.
 * @author Milton
 *
 */
public class StatusFragment extends Fragment implements OnSeekBarChangeListener {
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
	private SeekBar seriousnessBar;
	private SeekBar anxietyBar;
	private EditText summaryText;
	private EditText commentText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.status_layout, container, false);
		getActivity().getActionBar().setDisplayShowHomeEnabled(false);
		getActivity().getActionBar().setDisplayShowTitleEnabled(false);
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

	/** Restore the original menu on action bar when this fragment is removed **/
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().getActionBar().setDisplayShowHomeEnabled(true);
		getActivity().getActionBar().setDisplayShowTitleEnabled(true);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.status, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save:
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
		case R.id.action_exit:
			getFragmentManager().beginTransaction().remove(this).commit();
			getFragmentManager().popBackStack();
		}
		return true;
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
		seriousnessBar = (SeekBar) view.findViewById(R.id.newAnxietySeekBar);
		anxietyBar = (SeekBar) view.findViewById(R.id.newSeriousnessSeekBar);
		summaryText = (EditText) view.findViewById(R.id.summaryEditText);
		commentText = (EditText) view.findViewById(R.id.commentEditText);

		seriousnessBar.setOnSeekBarChangeListener(this);
		anxietyBar.setOnSeekBarChangeListener(this);
	}

}
