/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package za.co.neilson.alarm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.yuen.PatientReport;
import com.example.yuen.e_carei.Appointmentcreate;
import com.example.yuen.e_carei.Case_history_review;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei.queueshow;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView;
import com.example.yuen.e_carei_login.LoginActivity;
import com.example.yuen.e_carei_login.SQLiteHandler;
import com.example.yuen.e_carei_login.SessionManager;

import java.util.HashMap;
import java.util.List;

import za.co.neilson.alarm.database.Database;
import za.co.neilson.alarm.preferences.AlarmPreferencesActivity;

public class AlarmActivity extends BaseActivity {

	ImageButton newButton;
	ListView mathAlarmListView;
	AlarmListAdapter alarmListAdapter;
	private DrawerLayout drawerLayout;
	int navItemId;
	private Toolbar toolbar;
	private SQLiteHandler db;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarm_activity);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("E-care");
		setSupportActionBar(toolbar);

		db = new SQLiteHandler(getApplicationContext());
		session = new SessionManager(getApplicationContext());

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
		view.getMenu().getItem(3).setChecked(true);
		view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				Toast.makeText(AlarmActivity.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
				Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
				Intent intent = new Intent();
				switch (menuItem.getItemId()) {
					case R.id.nav_1:
						intent.setClass(AlarmActivity.this,Case_history_review.class);
						startActivity(intent);
						break;
					case R.id.nav_2:
						intent.setClass(AlarmActivity.this, queueshow.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_3:
						intent.setClass(AlarmActivity.this, Appointmentcreate.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_4:
						intent.setClass(AlarmActivity.this, AlarmActivity.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_5:
						intent.setClass(AlarmActivity.this, PatientReport.class);
						//intent .putExtra("name", "Hello B Activity");
						startActivity(intent);
						break;
					case R.id.nav_6:
						//logout
						AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
						//Uncomment the below code to Set the message and title from the strings.xml file
						//builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

						//Setting message manually and performing action on button click
						builder.setMessage("Do you want to close this application ?")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										session.setLogin(false);
										db.deleteUsers();
										final Intent intent_logout = new Intent(AlarmActivity.this, LoginActivity.class);
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

		HashMap<String, String> dbuser = db.getUserDetails();
		View header = view.getHeaderView(0);
		TextView headerName = (TextView) header.findViewById(R.id.drawer_name);
		String username = dbuser.get("name");
		headerName.setText(username);
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		CirculaireNetworkImageView headerphoto = (CirculaireNetworkImageView) header.findViewById(R.id.drawer_thumbnail);
		headerphoto.setImageUrl("http://192.168.43.216/test/" + dbuser.get("image"), imageLoader);
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();

		mathAlarmListView = (ListView) findViewById(android.R.id.list);
		mathAlarmListView.setLongClickable(true);
		mathAlarmListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
				final Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
				Builder dialog = new AlertDialog.Builder(AlarmActivity.this);
				dialog.setTitle("Delete");
				dialog.setMessage("Delete this alarm?");
				dialog.setPositiveButton("Ok", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Database.init(AlarmActivity.this);
						Database.deleteEntry(alarm);
						AlarmActivity.this.callMathAlarmScheduleService();

						updateAlarmList();
					}
				});
				dialog.setNegativeButton("Cancel", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialog.show();

				return true;
			}
		});

		callMathAlarmScheduleService();

		alarmListAdapter = new AlarmListAdapter(this);
		this.mathAlarmListView.setAdapter(alarmListAdapter);
		mathAlarmListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
				Intent intent = new Intent(AlarmActivity.this, AlarmPreferencesActivity.class);
				intent.putExtra("alarm", alarm);
				startActivity(intent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);		
		menu.findItem(R.id.menu_item_save).setVisible(false);
		menu.findItem(R.id.menu_item_delete).setVisible(false);
	    return result;
	}
		
	@Override
	protected void onPause() {
		// setListAdapter(null);
		Database.deactivate();
		super.onPause();
	}

	private void navigateTo(MenuItem menuItem){

		navItemId = menuItem.getItemId();
		menuItem.setChecked(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putInt(NAV_ITEM_ID, navItemId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateAlarmList();
	}
	
	public void updateAlarmList(){
		Database.init(AlarmActivity.this);
		final List<Alarm> alarms = Database.getAll();
		alarmListAdapter.setMathAlarms(alarms);
		
		runOnUiThread(new Runnable() {
			public void run() {
				// reload content			
				AlarmActivity.this.alarmListAdapter.notifyDataSetChanged();				
				if(alarms.size() > 0){
					findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
				}else{
					findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.checkBox_alarm_active) {
			CheckBox checkBox = (CheckBox) v;
			Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox.getTag());
			alarm.setAlarmActive(checkBox.isChecked());
			Database.update(alarm);
			AlarmActivity.this.callMathAlarmScheduleService();
			if (checkBox.isChecked()) {
				Toast.makeText(AlarmActivity.this, alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
			}
		}

	}

}