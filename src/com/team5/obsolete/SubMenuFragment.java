package com.team5.obsolete;

import java.util.ArrayList;
import java.util.List;

import com.team5.pat.R;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SubMenuFragment extends ListFragment {
	List<SubMenuItem> list; // A list of items for the sub menu list

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.sub_menu_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		addItemsToSubMenuList();

		SubMenuListAdapter adapter = new SubMenuListAdapter(getActivity(),
				R.layout.sub_menu_row, list);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(getActivity(),
					com.team5.pat.SettingsActivity.class);
			startActivity(intent);
			break;
//		case 1:
//			intent = new Intent(getActivity(),
//					com.team5.obsolete.GraphActivity.class);
//			intent.putExtra("graphType", 0);
//			startActivity(intent);
//			break;
//
//		case 2:
//			intent = new Intent(getActivity(),
//					com.team5.obsolete.GraphActivity.class);
//			intent.putExtra("graphType", 1);
//			startActivity(intent);
//			break;

		// case 3:
		// intent = new Intent(getActivity(), com.team5.pat.MenuActivity.class);
		// intent.putExtra("graphType", 2);
		// startActivity(intent);
		// break;
		// case 4:
		// intent = new Intent(getActivity(), com.team5.pat.MenuActivity.class);
		// startActivity(intent);
		// break;
		// case 5:
		// // Create an intent to go from THIS, to TestActivity CLASS
		// intent = new Intent(getActivity(),
		// com.team5.pat.MenuHexagonActivity.class);
		// startActivity(intent);
		// break;
		//
		// case 6:
		// intent = new Intent(getActivity(),
		// com.team5.pat.SeekBarActivity.class);
		// startActivity(intent);
		// break;
		//
		// case 7:
		// intent = new Intent(getActivity(),
		// com.team5.pat.ScrollableDialogActivity.class);
		// startActivity(intent);
		// break;

		case 8:
			intent = new Intent(getActivity(), com.team5.pat.RecordGraphActivity.class);
			startActivity(intent);
			break;

		case 9:
			intent = new Intent(getActivity(), com.team5.pat.LockScreenActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void addItemsToSubMenuList() {
		int[] images = { R.drawable.ic_day, R.drawable.ic_week,
				R.drawable.ic_month, R.drawable.ic_year,
				R.drawable.ic_launcher, R.drawable.ic_day, R.drawable.ic_week,
				R.drawable.ic_month, R.drawable.ic_year, R.drawable.ic_launcher };
		String[] titles = getResources()
				.getStringArray(R.array.nav_list_titles);
		list = new ArrayList<SubMenuItem>();

		int size = titles.length;
		for (int i = 0; i < size; i++) {
			list.add(new SubMenuItem(images[i], titles[i]));
		}
	}
}

class SubMenuListAdapter extends ArrayAdapter<SubMenuItem> {
	private Context context;
	private ImageView image;
	private TextView title;
	private List<SubMenuItem> items;

	public SubMenuListAdapter(Context context, int resource,
			List<SubMenuItem> items) {
		super(context, resource, items);
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (row == null) {
			row = inflater.inflate(R.layout.sub_menu_row, parent, false);
		}

		image = (ImageView) row.findViewById(R.id.rowImage);
		title = (TextView) row.findViewById(R.id.rowTitle);
		image.setImageResource(items.get(position).getImage());
		// image.setImageResource(R.drawable.ic_launcher);
		title.setText(items.get(position).getTitle());
		return row;

	}

}

/** Objects contained in each row of the sub menu **/
class SubMenuItem {
	private int image;
	private String title;

	public SubMenuItem(int image, String title) {
		this.title = title;
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public int getImage() {
		return image;
	}
}