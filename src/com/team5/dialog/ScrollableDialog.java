package com.team5.dialog;

import com.team5.pat.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

/**
 * Learning to use a dialog. Obsolete.
 * @author Milton
 *
 */
public class ScrollableDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Instantiate a builder for the dialog box
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Set up the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Set the title, message and layout of the dialog box
		builder.setView(inflater.inflate(R.layout.dialog_box_layout, null))
				.setTitle(R.string.dialog_title);

		// User accepts the thing in the dialog box
		builder.setPositiveButton(R.string.dialog_accpet,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getActivity(),
								"Thank you for your kiss ^.^",
								Toast.LENGTH_SHORT).show();
					}
				});

		// User cancels the thing in the dialog box
		builder.setNegativeButton(R.string.dialog_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getActivity(), "Refuse my kiss??? >.<",
								Toast.LENGTH_SHORT).show();
					}
				});

		return builder.create(); // return the AlertDialog object
	}
}