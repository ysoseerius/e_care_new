package com.example.yuen.info.androidhive.showpatientlist.adater;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.info.androidhive.showpatientlist.CirculaireNetworkImageView;
import com.example.yuen.info.androidhive.showpatientlist.model.Patient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Patient> patientItems;
	private String url = "http://192.168.0.100/test/db_update.php";
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
		//TextView rating = (TextView) convertView.findViewById(R.id.rating);
		//TextView genre = (TextView) convertView.findViewById(R.id.genre);
		//TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
		// getting movie data for the row
		Patient m = patientItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		// title
		title.setText(m.getTitle());
		/*
		// rating
		rating.setText("Rating: " + String.valueOf(m.getRating()));
		
		// genre
		String genreStr = "";
		for (String str : m.getGenre()) {
			genreStr += str + ", ";
		}
		genreStr = genreStr.length() > 0 ? genreStr.substring(0,
				genreStr.length() - 2) : genreStr;
		genre.setText(genreStr);
		
		// release year
		year.setText(String.valueOf(m.getYear()));
		*/
		return convertView;
	}



	private class OnItemClickListener implements View.OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			Patient p=patientItems.get(mPosition);
			final String id=p.getTitle();
			final String image=p.getThumbnailUrl();



			System.out.println(id);
			//System.out.println(imagepost);

			StringRequest postRequest = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>()
					{
						@Override
						public void onResponse(String response) {
							// response
							Log.d("Response", response);
						}
					},
					new Response.ErrorListener()
					{
						@Override
						public void onErrorResponse(VolleyError error) {
							// error
							//Log.d("Error.Response", response);
						}
					}
			) {
				@Override
				protected Map<String, String> getParams()
				{
					HashMap<String, String> jsonParams = new HashMap<String, String>();
					String imagepost = image.substring(image.lastIndexOf('/')+1,image.length());
					jsonParams.put("id", id);
					Log.d("id", id);
					jsonParams.put("image", imagepost);;

					return jsonParams;
				}
			};

			AppController.getInstance().addToRequestQueue(postRequest);
		}

	}
}