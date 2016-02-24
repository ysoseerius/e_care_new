package com.example.yuen.e_carei;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.yuen.e_carei_app.AppController;

import java.util.List;



public class AppointmentListAdapter extends BaseSwipListAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<AppointmentList> appointmentlistItems;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public AppointmentListAdapter(Activity activity, List<AppointmentList> movieItems) {
		this.activity = activity;
		this.appointmentlistItems = movieItems;
	}

	@Override
	public int getCount() {
		return appointmentlistItems.size();
	}

	@Override
	public Object getItem(int location) {
		return appointmentlistItems.get(location);
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
			convertView = inflater.inflate(R.layout.item_list_app, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		TextView queue_number= (TextView) convertView.findViewById(R.id.queue_number);
		TextView atype= (TextView) convertView.findViewById(R.id.atype_number);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView doctor_name = (TextView) convertView.findViewById(R.id.doctor_name);
		// getting movie data for the row
		AppointmentList appointmentlist = appointmentlistItems.get(position);

		// title
		//name.setText(appointmentlist.getName());
//		uid.setText(appointmentlist.getUid());
		queue_number.setText(appointmentlist.getQueue()+"");
		atype.setText(appointmentlist.getAtype()+"");
		date.setText(appointmentlist.getDate());
		time.setText(appointmentlist.getTime());
		doctor_name.setText(appointmentlist.getDoctor());

		// thumbnail image
		//thumbNail.setImageUrl(appointmentlist.getThumbnailUrl(), imageLoader);

		return convertView;
	}

}
