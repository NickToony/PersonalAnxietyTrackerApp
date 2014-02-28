package com.team5.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.team5.pat.R;
import com.team5.pat.Session;
import com.team5.user.UserRecord;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
	private SeekBar seriousnessBar;
	private SeekBar anxietyBar;
	private EditText summaryText;
	private EditText commentText;
	private ImageView audioButton;

	// Used for record audio
	private MediaRecorder recorder;
	private String rootPath;
	private String mFileName;
	private List<String> audioFilePaths;
	private boolean startRecord = true;

	// Used for play audio
	private MediaPlayer player;
	private ImageView playButton;
	private boolean startPlay = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		createFolderOnExternalStorage();
		scanForRecordedAudioFiles();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (recorder != null) {
			recorder.release();
			recorder = null;
		}

		if (player != null) {
			player.release();
			player = null;
		}
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
	public void onClick(View v) {
		if (v.getId() == R.id.audioButton) {
			if (startRecord) {
				audioButton.setImageDrawable(getResources().getDrawable(
						R.drawable.stop_record));
				startRecord();
				Toast.makeText(getActivity(), "Recording", Toast.LENGTH_SHORT)
						.show();
			} else {
				audioButton.setImageDrawable(getResources().getDrawable(
						R.drawable.start_record));
				stopRecord();
			}
			startRecord = !startRecord;
		} else if (v.getId() == R.id.playButton) {
			if (startPlay && audioFilePaths.size() > 0) {
				try {
					player = new MediaPlayer();
					player.setDataSource(audioFilePaths.get(audioFilePaths
							.size()-1));
					player.prepare();
					player.start();
					playButton.setImageDrawable(getResources().getDrawable(
							R.drawable.stop));
				} catch (IOException e) {
					Toast.makeText(getActivity(), "IO Exception",
							Toast.LENGTH_SHORT).show();
				}
			} else if (!startPlay && audioFilePaths.size() > 0) {
				player.release();
				player = null;
			}
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

	private void createFolderOnExternalStorage() {
		// Create a folder on external storage
		rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File directory = new File(rootPath, "PAT");
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

	private void scanForRecordedAudioFiles() {
		audioFilePaths = new ArrayList<String>();
		File audioDirectory = new File(rootPath + "/PAT");
		File[] files = audioDirectory.listFiles();
		for (int i = 0; i < files.length; i++) {
			audioFilePaths.add(files[i].getAbsolutePath());
		}
	}

	private void initialiseComponentsAndSetListeners() {
		seriousnessBar = (SeekBar) view.findViewById(R.id.newAnxietySeekBar);
		anxietyBar = (SeekBar) view.findViewById(R.id.newSeriousnessSeekBar);
		summaryText = (EditText) view.findViewById(R.id.summaryEditText);
		commentText = (EditText) view.findViewById(R.id.commentEditText);
		audioButton = (ImageView) view.findViewById(R.id.audioButton);
		playButton = (ImageView) view.findViewById(R.id.playButton);

		seriousnessBar.setOnSeekBarChangeListener(this);
		anxietyBar.setOnSeekBarChangeListener(this);
		audioButton.setOnClickListener(this);
		playButton.setOnClickListener(this);
	}

	private void startRecord() {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

		mFileName = rootPath + "/PAT/audiorecordtest"
				+ new Random().nextInt(1000) + ".3gp";
		recorder.setOutputFile(mFileName);
		audioFilePaths.add(mFileName);

		try {
			recorder.prepare();
			recorder.start();
		} catch (IOException e) {
			Toast.makeText(getActivity(), "IO Exception", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void stopRecord() {
		recorder.stop();
		recorder.release();
		recorder = null;
		Toast.makeText(getActivity(), "Done recording", Toast.LENGTH_SHORT)
				.show();
	}

}
