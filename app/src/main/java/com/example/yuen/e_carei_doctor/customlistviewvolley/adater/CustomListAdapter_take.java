package com.example.yuen.e_carei_doctor.customlistviewvolley.adater;

import android.app.Activity;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.customlistviewvolley.model.Take_med_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class CustomListAdapter_take extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Take_med_list> takemedlistItems;
	private static final String sendurl = "http://192.168.43.216/test/take_notice.php";
	public boolean check =false;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter_take(Activity activity, List<Take_med_list> movieItems) {
		this.activity = activity;
		this.takemedlistItems = movieItems;
	}

	@Override
	public int getCount() {
		return takemedlistItems.size();
	}

	@Override
	public Object getItem(int location) {
		return takemedlistItems.get(location);
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
			convertView = inflater.inflate(R.layout.take_med_list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView take_title = (TextView) convertView.findViewById(R.id.take_title);
		TextView uid = (TextView) convertView.findViewById(R.id.uid);
		TextView aid = (TextView) convertView.findViewById(R.id.aid);
		ImageButton takeBtn = (ImageButton)convertView.findViewById(R.id.take_btn);
		//TextView rating = (TextView) convertView.findViewById(R.id.rating);
		//TextView genre = (TextView) convertView.findViewById(R.id.genre);
		//TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

		// getting movie data for the row
		Take_med_list m = takemedlistItems.get(position);
		takeBtn.setTag(position);
		takeBtn.setOnClickListener(new OnItemClickListener(position));

		// title
		take_title.setText(m.getName());
		aid.setText(String.valueOf(m.getAid()));
		uid.setText(m.getUid());

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

		return convertView;
	}
	private class OnItemClickListener implements View.OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(final View arg0) {

			Take_med_list p= takemedlistItems.get(mPosition);
			final String check_uid = p.getUid().substring(5, p.getUid().length());
			final int aid = p.getAid();
			//final String image=p.getThumbnailUrl();

			System.out.println(check_uid);
			//System.out.println(imagepost);
			JsonArrayRequest movieReq = new JsonArrayRequest(sendurl,
					new Response.Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							Log.d("Response", response.toString());
							//Log.d("id", "hi") ;
							// Parsing json
							// reset the list


							for (int i = 0; i < response.length();i++) {
								try {
									//Log.d("id","hi1") ;
									//Log.d("i","i:"+i);

									//Log.d("length", "length:" + response.length());

									JSONObject objuid = response.getJSONObject(i);
									//get id
									final String get_uid = objuid.getString("uid");
									System.out.println("0"+get_uid+"0");
									System.out.println("0" + check_uid + "0" + (get_uid.equals(check_uid)));

									if(get_uid.equals(check_uid))
									{
									JSONObject objname = response.getJSONObject(++i);
									String messageToSend = "Hi, " + get_uid + " , " + objname.getString("name") + ", Your medication is ready and please come to take it as soon as possible!!.";

									JSONObject objphone = response.getJSONObject(++i);
									String number = objphone.getInt("phone") + "";
									Log.d("number", number);
									Log.d("phone", objphone.getInt("phone") + "");
									Log.d("uid", objuid.getString("uid") + "");
									//Log.d("i","i:"+i);
									//obj= response.getJSONObject(i+1);
									//get image url second item
									Log.d(check_uid, objuid.getString("uid"));

										Log.d(check_uid, objuid.getString("uid") + "");
										SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
										Toast.makeText(arg0.getContext(), "Message sent to " + objphone.getInt("phone") , Toast.LENGTH_SHORT).show();

									}
									else
									{
										i=i+2;
										continue;
									}

								} catch (JSONException e) {
									e.printStackTrace();
								}

							}

						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.d("Error", "Error: " + error.getMessage());

				}
			});

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(movieReq);
		}

	}
}
