package com.example.yuen.e_carei_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.yuen.PatientReport;
import com.example.yuen.e_carei.Appointmentcreate;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei.ShowAppointmentList;
import com.example.yuen.e_carei_doctor.activity.IconTextTabsActivity;
import com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView;
import com.example.yuen.e_carei_login.LoginActivity;
import com.example.yuen.e_carei_login.SQLiteHandler;
import com.example.yuen.e_carei_login.SessionManager;
import com.example.yuen.e_carei_search.customsearchlistvolley.activity.SearchTabsActivity;
import com.example.yuen.info.androidhive.showpatientlist.PatientList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import za.co.neilson.alarm.AlarmActivity;

public class CaseReviewDetail extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView contentView;
    private int navItemId;
    private SessionManager session;
    private SQLiteHandler db;

    private static final String NAV_ITEM_ID = "nav_index";

    // Log tag
    private static final String TAG = CaseReviewDetail.class.getSimpleName();
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

    // Movies json url
    private String url = "http://192.168.43.216/test/show_case_review_detail.php";
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history_solo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("E-care");
        setSupportActionBar(toolbar);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> dbuser = db.getUserDetails();

        session = new SessionManager(getApplicationContext());

        if(dbuser.get("account_type").equals("2")) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
            view.getMenu().clear();
            view.inflateMenu(R.menu.drawer);
            view.getMenu().getItem(0).setChecked(true);

            view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Toast.makeText(CaseReviewDetail.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                    Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                    Intent intent = new Intent();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_1:
                            break;
                        case R.id.nav_2:
                            intent.setClass(CaseReviewDetail.this, ShowAppointmentList.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_3:
                            intent.setClass(CaseReviewDetail.this, Appointmentcreate.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_4:
                            intent.setClass(CaseReviewDetail.this, AlarmActivity.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_5:
                            intent.setClass(CaseReviewDetail.this, PatientReport.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_6:
                            //logout
                            AlertDialog.Builder builder = new AlertDialog.Builder(CaseReviewDetail.this);
                            //Uncomment the below code to Set the message and title from the strings.xml file
                            //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                            //Setting message manually and performing action on button click
                            builder.setMessage("Do you want to close this application ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            session.setLogin(false);
                                            db.deleteUsers();
                                            final Intent intent_logout = new Intent(CaseReviewDetail.this, LoginActivity.class);
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

            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            View header = view.getHeaderView(0);
            TextView headerName = (TextView) header.findViewById(R.id.drawer_name);
            String username = dbuser.get("name");
            headerName.setText(username);

            CirculaireNetworkImageView headerphoto = (CirculaireNetworkImageView) header.findViewById(R.id.drawer_thumbnail);
            headerphoto.setImageUrl("http://192.168.43.216/test/" + dbuser.get("image"), imageLoader);
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }
        else if(dbuser.get("account_type").equals("1"))
        {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
            view.getMenu().clear();
            view.inflateMenu(R.menu.drawer2);
            view.getMenu().getItem(0).setChecked(true);
            view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Toast.makeText(CaseReviewDetail.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                    Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                    Intent intent = new Intent();
                    switch (menuItem.getItemId()) {

                        case R.id.nav_p1:
                            intent.setClass(CaseReviewDetail.this, PatientList.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_p2:
                            intent.setClass(CaseReviewDetail.this, IconTextTabsActivity.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_p3:
                            intent.setClass(CaseReviewDetail.this, IconTextTabsActivity.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_p4:
                            intent.setClass(CaseReviewDetail.this, SearchTabsActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_p5:
                            //logout
                            AlertDialog.Builder builder = new AlertDialog.Builder(CaseReviewDetail.this);
                            //Uncomment the below code to Set the message and title from the strings.xml file
                            //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                            //Setting message manually and performing action on button click
                            builder.setMessage("Do you want to close this application ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            session.setLogin(false);
                                            db.deleteUsers();
                                            final Intent intent_logout = new Intent(CaseReviewDetail.this, LoginActivity.class);
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
            View header = view.getHeaderView(0);
            TextView headerName = (TextView) header.findViewById(R.id.drawer_name);
            String username = dbuser.get("name");
            headerName.setText(username);
            CirculaireNetworkImageView headerphoto = (CirculaireNetworkImageView) header.findViewById(R.id.drawer_thumbnail);
            headerphoto.setImageUrl("http://192.168.43.216/test/" + dbuser.get("image"), imageLoader);
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }
        fetchCase();

        Button back_button = (Button) findViewById(R.id.button);

        back_button.setOnClickListener(new Button.OnClickListener() {

            @Override

            public void onClick(View v) {
                onBackPressed();
            }

        });

    }

    /**
     * Fetching movies json by making http call
     */
    private void fetchCase() {
        // Creating volley request obj
        Intent i = getIntent();
        final String case_number = i.getStringExtra("case_number");
        final String check_uid =i.getStringExtra("uid");
        Log.d(case_number,check_uid);

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        TextView name_show = (TextView) findViewById(R.id.name_show);
                        TextView uid_show = (TextView) findViewById(R.id.uid_show);
                        TextView case_number_show = (TextView) findViewById(R.id.solo_case_history);
                        TextView doctor_name = (TextView) findViewById(R.id.name_doctor_case_history);
                        TextView medication = (TextView) findViewById(R.id.name_medication_case_history);
                        TextView detail = (TextView) findViewById(R.id.name_detail_case_history);
                        TextView report = (TextView) findViewById(R.id.name_report_case_history);
                        NetworkImageView photo= (NetworkImageView) findViewById(R.id.photo);

                for (int i = 0; i < response.length();i++) {
                    try {
                        Log.d("length", response.length() + "");
                        uid_show.setText(check_uid);

                        Log.d("length i", i + "");

                        JSONObject objuid= response.getJSONObject(i);
                        final String uid_get = objuid.getString("uid");
                        Log.d(check_uid,"!"+objuid.getString("uid"));

                        Log.d("length i 2", i + "");

                        JSONObject objcase= response.getJSONObject(++i);
                        final String case_get = objcase.getString("case_number");
                        Log.d("case",objcase.getString("case_number"));
                        Log.d("------linebreak","--------");
                        Log.d(case_get.equals(case_number)+"",uid_get.equals(check_uid)+"");
                        Log.d(case_get,case_number);
                        if(case_number.equals(case_get) && uid_get.equals(check_uid)) {
                            JSONObject objname = response.getJSONObject(++i);
                            name_show.setText(objname.getString("name"));

                            JSONObject objimage = response.getJSONObject(++i);
                            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                            photo.setImageUrl(objimage.getString("image"), imageLoader);

                            case_number_show.setText("Case Number : " + case_number);

                            JSONObject objdcname = response.getJSONObject(++i);
                            doctor_name.setText(objdcname.getString("doctor"));
                            Log.d("doctor", objdcname.getString("doctor"));

                            JSONObject objmedication = response.getJSONObject(++i);
                            medication.setText(objmedication.getString("medication"));
                            Log.d("medication", objmedication.getString("medication"));

                            JSONObject objdetail = response.getJSONObject(++i);
                            detail.setText(objdetail.getString("detail"));
                            Log.d("detail", objdetail.getString("detail"));

                            JSONObject objreport = response.getJSONObject(++i);
                            report.setText(objreport.getString("report"));
                            Log.d("report", objreport.getString("report"));
                        }
                    else
                        {
                            i=i+6;
                        }
                        Log.d(i+"",response.length()+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
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
        //outState.putInt(NAV_ITEM_ID, navItemId);
    }




}
