package com.team5.fragment;

import com.team5.pat.HomeActivity;
import com.team5.pat.R;

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
import android.widget.TextView;

public class FeedbackFragment extends Fragment
{

	private View myView;
	private HomeActivity myActivity;
	private EditText titleEditText;
	private EditText commentEditText;
	private SeekBar ratingSeekBar;
	private Button submitButton;
	private TextView ratingValue;

	private final int MAX_RATING = 10;
	private final int INITIAL_PROGRESS = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		myView = inflater.inflate(R.layout.feedback_fragment, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Feedback");

		titleEditText = (EditText) myView.findViewById(R.id.titleEditText);
		commentEditText = (EditText) myView.findViewById(R.id.commentEditText);
		ratingSeekBar = (SeekBar) myView.findViewById(R.id.ratingSeekBar);
		submitButton = (Button) myView.findViewById(R.id.submitButton);
		ratingValue = (TextView) myView.findViewById(R.id.ratingValue);

		// Set values
		ratingSeekBar.setMax(MAX_RATING);
		ratingSeekBar.setProgress(INITIAL_PROGRESS);
		
		ratingValue.setText(""+INITIAL_PROGRESS);

		setListeners();

		return myView;
	}

	private void setListeners()
	{
		ratingSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{

				ratingValue.setText(""+ratingSeekBar.getProgress());
			}
		});

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				String title;
				String comment;
				int rating;

				title = titleEditText.getText().toString();
				comment = commentEditText.getText().toString();
				rating = Integer.parseInt(ratingValue.getText().toString());

				// FOR NICK TO DO THE PARSING OF DATA

			}
		});

	}
}
