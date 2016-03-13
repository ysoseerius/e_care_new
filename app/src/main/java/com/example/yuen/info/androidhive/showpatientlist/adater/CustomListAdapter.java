package com.example.yuen.info.androidhive.showpatientlist.adater;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView;
import com.example.yuen.info.androidhive.showpatientlist.model.Patient;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Patient> patientItems;
	private Notification notification;
	//private String url = "http://10.89.133.147/test/db_update.php";
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Patient> movieItems) {
		this.activity = activity;
		this.patientItems = movieItems;
	}

	@Override
	public int getCount() {
		return patientItems.size();
	}

	@Override
	public Object getItem(int location) {
		return patientItems.get(location);
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
			convertView = inflater.inflate(R.layout.patient_list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		CirculaireNetworkImageView thumbNail = (CirculaireNetworkImageView) convertView
				.findViewById(R.id.thumbnail);

		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView uid = (TextView) convertView.findViewById(R.id.uid);
		//TextView genre = (TextView) convertView.findViewById(R.id.genre);
		//TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
		// getting movie data for the row
		Patient m = patientItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		// title
		title.setText(m.getTitle());
		uid.setText(m.getUid());

		return convertView;
	}

}