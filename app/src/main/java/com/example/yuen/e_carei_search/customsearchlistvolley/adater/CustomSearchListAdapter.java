package com.example.yuen.e_carei_search.customsearchlistvolley.adater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_search.customsearchlistvolley.model.Search_list;

import java.util.List;



public class CustomSearchListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Search_list> searchlistItems;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomSearchListAdapter(Activity activity, List<Search_list> movieItems) {
		this.activity = activity;
		this.searchlistItems = movieItems;
	}

	@Override
	public int getCount() {
		return searchlistItems.size();
	}

	@Override
	public Object getItem(int location) {
		return searchlistItems.get(location);
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
			convertView = inflater.inflate(R.layout.search_result_list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView take_title = (TextView) convertView.findViewById(R.id.title);
		TextView uid = (TextView) convertView.findViewById(R.id.uid);

		// getting movie data for the row
		Search_list searchlist = searchlistItems.get(position);

		// title
		take_title.setText(searchlist.getName());
		uid.setText(searchlist.getUid());

		// thumbnail image
		thumbNail.setImageUrl(searchlist.getThumbnailUrl(), imageLoader);

		return convertView;
	}


}
