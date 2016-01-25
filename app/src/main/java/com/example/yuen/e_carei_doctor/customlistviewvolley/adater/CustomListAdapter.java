package com.example.yuen.e_carei_doctor.customlistviewvolley.adater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.customlistviewvolley.model.Appointment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Appointment> appointmentItems;
	private static final String addurl = "http://192.168.43.216/test/add_appointment.php";
	private static final String sendurl = "http://192.168.43.216/test/send_notice.php";
	private static final String queueurl = "http://192.168.43.216/test/requeue.php";

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Appointment> patientItems) {
		this.activity = activity;
		this.appointmentItems = patientItems;
	}

	@Override
	public int getCount() {
		return appointmentItems.size();
	}

	@Override
	public Object getItem(int location) {
		return appointmentItems.get(location);
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
			convertView = inflater.inflate(R.layout.appointment_list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);

		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView uid = (TextView) convertView.findViewById(R.id.uid);
		TextView ctype = (TextView) convertView.findViewById(R.id.ctype);
		TextView atype = (TextView) convertView.findViewById(R.id.atype);
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView queue = (TextView) convertView.findViewById(R.id.queue);
		TextView date = (TextView) convertView.findViewById(R.id.date);

		Button addBtn = (Button) convertView.findViewById(R.id.add_btn);
		ImageButton queueBtn = (ImageButton) convertView.findViewById(R.id.queue_btn);
		//TextView rating = (TextView) convertView.findViewById(R.id.rating);
		//TextView genre = (TextView) convertView.findViewById(R.id.genre);
		//TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

		// getting movie data for the row
		Appointment m = appointmentItems.get(position);

		addBtn.setTag(position);
		addBtn.setOnClickListener(new OnItemClickListener(position));

		queueBtn.setTag(position);
		queueBtn.setElevation(8);
		queueBtn.setFocusable(false);
		int checkqueue = m.getQueue();
		queueBtn.setOnClickListener(new OnItemClickListener2(position));
		if(checkqueue != 0) {
			queueBtn.setVisibility(View.GONE);
		}


		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

		// title
		title.setText(m.getName());
		uid.setText(m.getUid());
		ctype.setText(m.getCtype());
		time.setText(String.valueOf(m.getTime()));
		if("0".equals(String.valueOf(m.getTime())))
		{
			time.setVisibility(convertView.GONE);
		}
		atype.setText(m.getAtype());
		queue.setText(String.valueOf(m.getQueue()));
		queue.setTextColor(Color.parseColor("#125688"));
		date.setText(m.getDate());
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

	//add button
	private class OnItemClickListener extends AppCompatActivity implements View.OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {
			//disable button

			Appointment p = appointmentItems.get(mPosition);
			final String uid = p.getUid().substring(4, p.getUid().length());
			final int aid = p.getAid();
			//final String image=p.getThumbnailUrl();

			StringRequest postRequest = new StringRequest(Request.Method.POST, addurl,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							// response
							Log.d("Response", response.toString());
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// error
							//Log.d("Error.Response", response);
						}
					}
			) {
				@Override
				protected Map<String, String> getParams() {
					HashMap<String, String> jsonParams = new HashMap<String, String>();
					//String imagepost = image.substring(image.lastIndexOf('/')+1,image.length());
					jsonParams.put("uid", uid);
					jsonParams.put("aid", aid + "");
					Log.d("uid", uid);
					//jsonParams.put("image", imagepost);

					return jsonParams;
				}
			};

			AppController.getInstance().addToRequestQueue(postRequest);
			//send notices to queue 2
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
									JSONObject objid= response.getJSONObject(i);
									//get id
									JSONObject objname= response.getJSONObject(++i);
									String messageToSend = "Hi, "+ objid.getString("uid")+ " , "+ objname.getString("name") +",there is one patient in front of you.";

									JSONObject objphone= response.getJSONObject(++i);
									String number = objphone.getInt("phone") + "";
									Log.d("number", number);
									Log.d("phone", objphone.getInt("phone")+"");
									//Log.d("i","i:"+i);
									//obj= response.getJSONObject(i+1);
									//get image url second item

									SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);


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

			Toast.makeText(arg0.getContext(), "Text Message reminder has sent to" + uid, Toast.LENGTH_SHORT).show();
			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(movieReq);

		}

	}

	//queue button
	private class OnItemClickListener2 implements View.OnClickListener {
		private int mPosition;

		OnItemClickListener2(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {
			//disable button
			arg0.setEnabled(false);
			arg0.setVisibility(View.GONE);

			Appointment p=appointmentItems.get(mPosition);
			final String name=p.getName();
			final String uid = p.getUid().substring(4, p.getUid().length());
			//final String image=p.getThumbnailUrl();


			//System.out.println(imagepost);

			StringRequest postRequest = new StringRequest(Request.Method.POST, queueurl,
					new Response.Listener<String>()
					{
						@Override
						public void onResponse(String response) {
							// response
							Log.d("Response", response.toString());
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
					//String imagepost = image.substring(image.lastIndexOf('/')+1,image.length());
					jsonParams.put("name", name);
					jsonParams.put("uid", uid);
					Log.d("name", name);
					//jsonParams.put("image", imagepost);

					return jsonParams;
				}
			};
			Toast.makeText(arg0.getContext(), name + " has added to queue" , Toast.LENGTH_SHORT).show();
			AppController.getInstance().addToRequestQueue(postRequest);

		}

	}

}
