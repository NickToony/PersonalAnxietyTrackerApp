package com.team5.dialog;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team5.pat.R;

/**
 * Learning to use dialogs. Obsolete.
 * @author Milton
 *
 */
public class ScrollableDialogFragment extends Fragment {
	public ScrollableDialogFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_dialog, container, false);

		// Show the "navigate up" button on action bar
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		final DialogFragment dialog = new ScrollableDialog();
		final FragmentManager manager = getFragmentManager();

		dialog.show(manager, "kiss_me");

		return view;
	}

	class DropDownListener implements OnNavigationListener {
		// The same word as SpinerAdapter
		String[] titles = getResources().getStringArray(
				R.array.spinner_list_items);

		@Override
		public boolean onNavigationItemSelected(int position, long id) {
			switch (position) {
			case 0:
				return true;
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				return true;
			case 4:
				return true;
			default:
				return false;
			}
		}
	}
}
