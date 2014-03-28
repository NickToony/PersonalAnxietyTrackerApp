package com.team5.fragment;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.team5.navigationlist.NavListAdapter;
import com.team5.network.NetworkInterface;
import com.team5.network.Request;
import com.team5.network.Response;
import com.team5.pat.HomeActivity;
import com.team5.pat.R;
import com.team5.social.SocialAccount;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Provides the interface to send feedback on the app
 * @author Aleks
 *
 */
public class FeedbackFragment extends Fragment implements NetworkInterface
{

	private View myView;
	private HomeActivity myActivity;
	private EditText titleEditText;
	private EditText emailEditText;
	private EditText commentEditText;
	private SeekBar ratingSeekBar;
	private Button submitButton;
	private TextView ratingValue;
	private TextView errorType;
	private TextView titleError;
	private TextView commentError;
	private TextView emailError;
	private TextView ratingError;

	private final int MAX_RATING = 50;
	private final double RATING_ROUNDING_VALUE = 10;
	private final int INITIAL_PROGRESS = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		myView = inflater.inflate(R.layout.feedback_fragment, container, false);
		myActivity = (HomeActivity) getActivity();
		myActivity.setTitle("Feedback");

		titleEditText = (EditText) myView.findViewById(R.id.titleEditText);
		commentEditText = (EditText) myView.findViewById(R.id.commentEditText);
		ratingSeekBar = (SeekBar) myView.findViewById(R.id.ratingSeekBar);
		submitButton = (Button) myView.findViewById(R.id.submitButton);
		ratingValue = (TextView) myView.findViewById(R.id.ratingValue);
		errorType = (TextView) myView.findViewById(R.id.errorType);
		titleError = (TextView) myView.findViewById(R.id.titleError);
		commentError = (TextView) myView.findViewById(R.id.commentError);
		emailEditText = (EditText) myView.findViewById(R.id.emailEditText);
		emailError = (TextView) myView.findViewById(R.id.emailError);
		ratingError = (TextView) myView.findViewById(R.id.ratingError);

		// Set values
		ratingSeekBar.setMax(MAX_RATING);
		ratingSeekBar.setProgress(INITIAL_PROGRESS);

		ratingValue.setText("" + INITIAL_PROGRESS);

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
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{

				ratingValue.setText("" + Math.round((double) ratingSeekBar.getProgress() / RATING_ROUNDING_VALUE));
			}
		});

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{

				Request r = new Request(FeedbackFragment.this, "feedback.php");
				// possibility for adding feedback, not really sure
				r.addParameter("content", commentEditText.getText().toString());
				r.addParameter("title", titleEditText.getText().toString());
				r.addParameter("email", emailEditText.getText().toString());
				r.addParameter("rating", ratingValue.getText().toString());
				r.start();

			}
		});

	}

	/**
	 * Handles the request response.
	 * 
	 * Networking code template by Nick
	 * @author Aleks, Nick
	 *
	 */
	@Override
	public void eventNetworkResponse(Request from, Response response)
	{
		errorType.setVisibility(View.GONE);
		if (!response.isSuccess())
		{
			errorType.setText("Failure: " + response.getMessage());
			errorType.setVisibility(View.VISIBLE);
			return;
		}

		// Get the request element
		Element eleRequest = response.getRequest();

		// Get script status
		int scriptStatus = Integer.parseInt(eleRequest.getElementsByTagName("status").item(0).getTextContent());
		// script failure
		if (scriptStatus != 0)
		{
			String errorTypeString = eleRequest.getElementsByTagName("error").item(0).getTextContent();
			String errorMessage = eleRequest.getElementsByTagName("message").item(0).getTextContent();
			errorType.setText("Error: " + errorTypeString + ": " + errorMessage);
			errorType.setVisibility(View.VISIBLE);
			return;
		}

		// Get the request element
		Element eleData = response.getData();

		// Get the section elements
		TextView outputViews[] = { errorType, commentError, titleError, emailError, ratingError };
		NodeList sectionList = eleData.getChildNodes();
		Log.i("sectionList LENGTH", "" + sectionList.getLength());

		boolean success = true;
		for (int i = 0; i < sectionList.getLength(); i++)
		{
			// Get the section element
			Element sectionElement = (Element) sectionList.item(i);
			Log.i("ELEMENT " + i, "" + sectionElement);
			// Fetch the section data
			int sectionResponse = Integer.parseInt(sectionElement.getElementsByTagName("response").item(0).getTextContent());
			String sectionMessage = sectionElement.getElementsByTagName("message").item(0).getTextContent();
			Log.i("SECTION RESPONSE IS  ", "" + sectionResponse);
			// Output into correct position
			if (sectionResponse != 0)
			{

				outputViews[i].setText(sectionElement.getNodeName() + ": " + sectionMessage);
				success = false;
				Log.i("OUTPUT VIEW PASSED:  ", "" + outputViews[i]);

				errorType.setVisibility(View.VISIBLE);

			} else
			{
				outputViews[i].setText("");
				outputViews[i].setVisibility(View.GONE);
			}
		}

		if (success)
		{

			AlertDialog.Builder successAlert = new AlertDialog.Builder(myActivity);
			successAlert.setMessage("Feedback submitted!\nThank You!");
			successAlert.setCancelable(true);
			successAlert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();
					myActivity.doNavigation(NavListAdapter.navigationHome);
				}
			});

			AlertDialog alert11 = successAlert.create();
			alert11.show();
		}

	}

}
