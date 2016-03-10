package com.example.yuen.info.androidhive.showpatientlist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yuen.e_carei.Case_history_review;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.activity.IconTextTabsActivity;
import com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView;
import com.example.yuen.e_carei_login.LoginActivity;
import com.example.yuen.e_carei_login.SQLiteHandler;
import com.example.yuen.e_carei_login.SessionManager;
import com.example.yuen.e_carei_search.customsearchlistvolley.activity.SearchTabsActivity;
import com.example.yuen.info.androidhive.showpatientlist.adater.CustomListAdapter;
import com.example.yuen.info.androidhive.showpatientlist.model.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PatientList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

	private DrawerLayout drawerLayout;
	int navItemId;
	// Search EditText
	EditText inputSearch;
	private Toolbar toolbar;
	private static final String NAV_ITEM_ID = "nav_index";

	// Log tag
	private static final String TAG = PatientList.class.getSimpleName();
	// initially offset will be 0, later will be updated while parsing the json
	private int offSet = 0;

	// Movies json url
	private String url = "http://10.89.133.147/test/db_patientlist.php";
	private ProgressDialog pDialog;
	private List<Patient> patientList = new ArrayList<Patient>();
	private ListView listView;
	private CustomListAdapter adapter;
	private ArrayList arlist=new ArrayList();

	private SQLiteHandler db;
	private SessionManager session;

	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientlist_listview);


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("E-care");
		setSupportActionBar(toolbar);

		session = new SessionManager(getApplicationContext());
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
		view.getMenu().getItem(0).setChecked(true);
		view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				Toast.makeText(PatientList.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
				Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
				Intent intent = new Intent();
				switch (menuItem.getItemId()) {
					case R.id.nav_p1:
						intent.setClass(PatientList.this, PatientList.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_p2:
						intent.setClass(PatientList.this, IconTextTabsActivity.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_p3:
						intent.setClass(PatientList.this, IconTextTabsActivity.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_p4:
						intent.setClass(PatientList.this, SearchTabsActivity.class);
						startActivity(intent);
						break;
					case R.id.nav_p5:
						//logout
						AlertDialog.Builder builder = new AlertDialog.Builder(PatientList.this);
						//Uncomment the below code to Set the message and title from the strings.xml file
						//builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

						//Setting message manually and performing action on button click
						builder.setMessage("Do you want to close this application ?")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										session.setLogin(false);
										db.deleteUsers();
										final Intent intent_logout = new Intent(PatientList.this, LoginActivity.class);
										startActivity(intent_logout);
										finish();
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										//  Action for 'NO' Button
										dialog.cancel();
									}
								});

						//Creating dialog box
						AlertDialog alert = builder.create();
						//Setting the title manually
						alert.setTitle("AlertDialogExample");
						alert.show();
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

		listView = (ListView) findViewById(R.id.list);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

		adapter = new CustomListAdapter(this, patientList);
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
										fetchPatients();
									}
								}
		);


		listView.setAdapter(adapter);

		// Click event for single list row
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(PatientList.this, "row " + position + " was pressed", Toast.LENGTH_LONG).show();
				Log.d("position","0"+position);
				TextView tv_uid =(TextView) view.findViewById(R.id.uid);
				TextView tv_name =(TextView) view.findViewById(R.id.title);
				String saved_name= tv_name.getText().toString();
				String saved_uid= tv_uid.getText().toString();
				//取得arraylist內容
				String saved_image = arlist.get(position).toString().substring(26);
				Intent i = new Intent();
				i.setClass(PatientList.this, Case_history_review.class);
				i.putExtra("uid", saved_uid);
				i.putExtra("name", saved_name);
				Log.d("name_image",saved_image);
				i.putExtra("image", saved_image);
				startActivity(i);
			}

		});

	}

	@Override
	public void onRefresh() {
		fetchPatients();
	}
	/**
	 * Fetching movies json by making http call
	 */
	private void fetchPatients() {

		// showing refresh animation before making http call
		swipeRefreshLayout.setRefreshing(true);

		// appending offset to url
		//String url = URL_TOP_250 + offSet;

		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						hidePDialog();
						//Log.d("id", "hi") ;
						// Parsing json
						// reset the list
						patientList.clear();
						adapter.notifyDataSetChanged();

						for (int i = 0; i < response.length();i++) {
							try {
								//Log.d("id","hi1") ;
								//Log.d("i","i:"+i);

								Patient patient = new Patient();

								//Log.d("length", "length:" + response.length());
								JSONObject objid= response.getJSONObject(i);
								//get id
								patient.setUid(objid.getString("uid"));


								JSONObject objname= response.getJSONObject(++i);
								//get id
								patient.setTitle(objname.getString("name"));
								//Log.d("i","i:"+i);
								//obj= response.getJSONObject(i+1);
								//get image url second item
								JSONObject objimage= response.getJSONObject(++i);
								patient.setThumbnailUrl(objimage.getString("image"));

								arlist.add(objimage.getString("image"));

								//patient.setTitle(obj.getString("id"));
								//patient.setThumbnailUrl(obj.getString("image"));


								// Log.d("id",obj.getString("id")) ;
								//  Log.d("image",obj.getString("image")) ;
								/*movie.setRating(((Number) obj.get("rating"))
										.doubleValue());
								movie.setYear(obj.getInt("releaseYear"));

								// Genre is json array
								JSONArray genreArry = obj.getJSONArray("genre");
								ArrayList<String> genre = new ArrayList<String>();
								for (int j = 0; j < genreArry.length(); j++) {
									genre.add((String) genreArry.get(j));
								}
								movie.setGenre(genre);
								*/

								// adding movie to movies array
								//if(i%3==1)
								patientList.add(patient);

								// updating offset value to highest value
								//if (i >= offSet)
								//	offSet = i;

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
