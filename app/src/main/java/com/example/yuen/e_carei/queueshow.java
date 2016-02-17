package com.example.yuen.e_carei;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.yuen.e_carei_login.LoginActivity;
import com.example.yuen.e_carei_login.SQLiteHandler;
import com.example.yuen.e_carei_login.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import za.co.neilson.alarm.AlarmActivity;

public class queueshow extends AppCompatActivity {

    private static final String queue_show_url = "http://192.168.43.216/test/queue_show.php";
    private DrawerLayout drawerLayout;
    int navItemId;
    private Toolbar toolbar;
    private static final String NAV_ITEM_ID = "nav_index";

    private SQLiteHandler db;
    private SessionManager session;

    private TextView uid_show , name ,queue_show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_show);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("E-care");
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().getItem(1).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(queueshow.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                Intent intent = new Intent();
                switch (menuItem.getItemId()) {

                    case R.id.nav_1:
                        intent.setClass(queueshow.this,Case_history_review.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_2:
                        intent.setClass(queueshow.this, queueshow.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_3:
                        intent.setClass(queueshow.this, Appointmentcreate.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_4:
                        intent.setClass(queueshow.this, AlarmActivity.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_5:
                        intent.setClass(queueshow.this, PatientReport.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_6:
                        //logout
                        AlertDialog.Builder builder = new AlertDialog.Builder(queueshow.this);
                        //Uncomment the below code to Set the message and title from the strings.xml file
                        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to close this application ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        session.setLogin(false);
                                        db.deleteUsers();
                                        final Intent intent_logout = new Intent(queueshow.this, LoginActivity.class);
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
        headerphoto.setImageUrl("http://192.168.43.216/test/" + dbuser.get("image"), imageLoader);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        uid_show = (TextView)findViewById(R.id.uid_show);
        name = (TextView)findViewById(R.id.Case_number);
        queue_show= (TextView)findViewById(R.id.queue_show);
        db = new SQLiteHandler(getApplicationContext());

        uid_show.setText(dbuser.get("uid"));
        name.setText(dbuser.get("name"));

        //check to see the slot is full or not.1. add data to db and calculate the no of data in the db 2. If >5 , delete it
        JsonArrayRequest movieReq = new JsonArrayRequest(queue_show_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Response", response.toString());
                        //Log.d("id", "hi") ;
                        // Parsing json
                        // reset the list
                        String uid="P001";
                        queue_show.setText("0");
                        uid_show.setText(uid);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                //Log.d("id","hi1") ;
                                //Log.d("i","i:"+i);
                                //get id
                                //Log.d("length", "length:" + response.length());
                                JSONObject objuid = response.getJSONObject(i);
                                String uid_json = objuid.getString("uid");
                                //get id
                                if(uid.equals(uid_json))
                                uid_show.setText(objuid.getString("uid"));

                                JSONObject objqueue= response.getJSONObject(++i);
                                Log.d(uid,uid_json);
                                Log.d("True",uid.equals(uid_json) + "");
                                if(uid.equals(uid_json)) {
                                    queue_show.setText(objqueue.getString("queue"));
                                }
                                else
                                {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_case_history_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
