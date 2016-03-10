package com.example.yuen.e_carei;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.yuen.e_carei_app.AppController;

import java.util.List;


public class CustomTimeListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Time> timeItems;
	private Notification notification;
	//private String url = "http://10.89.133.147/test/db_update.php";
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomTimeListAdapter(Activity activity, List<Time> timeItems) {
		this.activity = activity;
		this.timeItems = timeItems;
	}

	@Override
	public int getCount() {
		return timeItems.size();
	}

	@Override
	public Object getItem(int location) {
		return timeItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.time_list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView time = (TextView) convertView.findViewById(R.id.time_show);
		TextView date = (TextView) convertView.findViewById(R.id.date_show);
		//TextView genre = (TextView) convertView.findViewById(R.id.genre);
		//TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
		// getting movie data for the row
		Time m = timeItems.get(position);

		// title
		time.setText(m.getTime());
		date.setText(m.getDate());

		return convertView;
	}

}