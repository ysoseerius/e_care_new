package com.example.yuen.e_carei;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yuen.PatientReport;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView;
import com.example.yuen.e_carei_login.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import za.co.neilson.alarm.AlarmActivity;


public class TimeList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

	private DrawerLayout drawerLayout;
	int navItemId;
	private static final String NAV_ITEM_ID = "nav_index";

	// Log tag
	private static final String TAG = TimeList.class.getSimpleName();
	// initially offset will be 0, later will be updated while parsing the json
	private int offSet = 0;

	// Movies json url
	private String time_url = "http://10.89.133.147/test/timelist.php";
	private ProgressDialog pDialog;
	private List<Time> timeList = new ArrayList<Time>();
	private ListView listView;
	private CustomTimeListAdapter adapter;

	private SQLiteHandler db;
	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_listview);


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("E-care");
		setSupportActionBar(toolbar);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
		view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				Toast.makeText(TimeList.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
				Log.d(R.id.nav_1+"", menuItem.getItemId() + " ");
				Intent intent = new Intent();
				switch (menuItem.getItemId())
				{

					case R.id.nav_1:
						break;
					case R.id.nav_2:
						intent.setClass(TimeList.this,queueshow.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_3:
						intent.setClass(TimeList.this,Appointmentcreate.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_4:
						intent.setClass(TimeList.this, AlarmActivity.class);
						startActivity(intent);
						break;
					case R.id.nav_5:
						//用來試appointment list
						intent.setClass(TimeList.this, PatientReport.class);
						startActivity(intent);
						break;
					case R.id.nav_6:

						//logout
						break;

				}
				menuItem.setChecked(true);
				drawerLayout.closeDrawers();
				return true;
			}
		});

		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.drawer_open , R.string.drawer_close){
			@Override
			public void onDrawerClosed(View drawerView) {
				super .onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super .onDrawerOpened(drawerView);
			}
		};

		db = new SQLiteHandler(getApplicationContext());
		HashMap<String, String> dbuser = db.getUserDetails();
		View header = view.getHeaderView(0);
		TextView headerName = (TextView) header.findViewById(R.id.drawer_name);
		String username = dbuser.get("name");
		headerName.setText(username);
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		CirculaireNetworkImageView headerphoto = (CirculaireNetworkImageView) header.findViewById(R.id.drawer_thumbnail);
		headerphoto.setImageUrl("http://10.89.133.147/test/" + dbuser.get("image"), imageLoader);
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();



		listView = (ListView) findViewById(R.id.timelist);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

		adapter = new CustomTimeListAdapter(this, timeList);
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		// changing action bar color
		//getActionBar().setBackgroundDrawable(
		//		new ColorDrawable(Color.parseColor("#00BBD3")));

		swipeRefreshLayout.setOnRefreshListener(this);

		/**
		 * Showing Swipe Refresh animation on activity create
		 * As animation won't start on onCreate, post runnable is used
		 */
		swipeRefreshLayout.post(new Runnable() {
									@Override
									public void run() {
										swipeRefreshLayout.setRefreshing(true);
										fetchTimes();
									}
								}
		);

	}

	@Override
	public void onRefresh() {
		fetchTimes();
	}
	/**
	 * Fetching movies json by making http call
	 */
	private void fetchTimes() {

		// showing refresh animation before making http call
		swipeRefreshLayout.setRefreshing(true);

		// appending offset to url
		//String url = URL_TOP_250 + offSet;
		final int aid_check = 111;
		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(time_url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						hidePDialog();
						//Log.d("id", "hi") ;
						// Parsing json
						// reset the list
						timeList.clear();
						adapter.notifyDataSetChanged();

						for (int i = 0; i < response.length();i++) {
							try {
								//Log.d("id","hi1") ;
								//Log.d("i","i:"+i);

								Time time = new Time();

								//Log.d("length", "length:" + response.length());

								JSONObject objaid= response.getJSONObject(i);
								JSONObject objtime= response.getJSONObject(++i);
								JSONObject objdate= response.getJSONObject(++i);
								//get id
								final int aid_json=objaid.getInt("appointment_id");
								Log.d("#"+aid_check+"#","#"+aid_json+"#");
								if(aid_check==aid_json) {
									Log.d("hi","asd");
									time.setTime(objtime.getString("time"));
									time.setDate(objdate.getString("date"));
									timeList.add(time);
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						adapter.notifyDataSetChanged();

						// stopping swipe refresh
						swipeRefreshLayout.setRefreshing(false);

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				hidePDialog();
				Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

				// stopping swipe refresh
				swipeRefreshLayout.setRefreshing(false);

			}
		});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(movieReq);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	private void navigateTo(MenuItem menuItem){

		navItemId = menuItem.getItemId();
		menuItem.setChecked(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(NAV_ITEM_ID, navItemId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
