/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 baoyongzhang <baoyz94@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.yuen.e_carei;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.yuen.PatientReport;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_login.LoginActivity;
import com.example.yuen.e_carei_login.SQLiteHandler;
import com.example.yuen.e_carei_login.SessionManager;
import com.example.yuen.info.androidhive.showpatientlist.PatientList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.neilson.alarm.AlarmActivity;

/**
 * SwipeMenuListView
 * Created by baoyz on 15/6/29.
 */
public class ShowAppointmentList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

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
    private String url = "http://10.89.133.147/test/apointmentlist_patient.php";
    private String deleteurl = "http://10.89.133.147/test/delete_appointment.php";
    private ProgressDialog PD;
    private ArrayList arlist=new ArrayList();

    private ArrayList aidlist=new ArrayList();

    private SQLiteHandler db;
    private SessionManager session;
    private HashMap<String, String> dbuser;
    private String username;
    private String uid;

    private String aid;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeMenuListView mListView;
    private List<AppointmentList> appointmentList = new ArrayList<AppointmentList>();
    private AppointmentListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("E-care");
        setSupportActionBar(toolbar);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().getItem(1).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(ShowAppointmentList.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                switch (menuItem.getItemId()) {
                    case R.id.nav_1:
                        intent.setClass(ShowAppointmentList.this,Case_history_review.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_2:
                        intent.setClass(ShowAppointmentList.this, ShowAppointmentList.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_3:
                        intent.setClass(ShowAppointmentList.this, Appointmentcreate.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_4:
                        intent.setClass(ShowAppointmentList.this, AlarmActivity.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_5:
                        intent.setClass(ShowAppointmentList.this, PatientReport.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_6:
                        //logout
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowAppointmentList.this);
                        //Uncomment the below code to Set the message and title from the strings.xml file
                        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to close this application ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        session.setLogin(false);
                                        db.deleteUsers();
                                        final Intent intent_logout = new Intent(ShowAppointmentList.this, LoginActivity.class);
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
        dbuser = db.getUserDetails();
        View header = view.getHeaderView(0);
        TextView headerName = (TextView) header.findViewById(R.id.drawer_name);
        username = dbuser.get("name");
        uid = dbuser.get("uid");
        headerName.setText(username);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView headerphoto = (com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView) header.findViewById(R.id.drawer_thumbnail);
        headerphoto.setImageUrl("http://10.89.133.147/test/" + dbuser.get("image"), imageLoader);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);


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

        PD = new ProgressDialog(this);
        //Showing progress dialog before making http request
        PD.setMessage("Loading...");
        PD.show();
        mAdapter = new AppointmentListAdapter(this, appointmentList);
        mListView.setAdapter(mAdapter);
        //fetchPatients();
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AppointmentList item = appointmentList.get(position);
                switch (index) {
                    case 0:
                        // delete
                        //delete(item);
                       final String row_aid = aidlist.get(position).toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowAppointmentList.this);

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to delete this row ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        delete(uid, row_aid);
                                        fetchPatients();
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
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });



    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        fetchPatients();
    }

    /**
     * Fetching movies json by making http call
     */
    private void fetchPatients() {
        swipeRefreshLayout.setRefreshing(true);
        appointmentList.clear();
        // Creating volley request obj
        final HashMap<String, String> params= new HashMap<String, String>();
        params.put("uid", "P001");

        final JSONObject jsonObject = new JSONObject(params);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        for (int i = 0; i < response.length();i++) {
                            try {

                                AppointmentList appointment = new AppointmentList();

                                //Log.d("length", "length:" + response.length());

                                JSONObject objuid= response.getJSONObject(++i);

                                JSONObject objaid= response.getJSONObject(i);
                                aidlist.add(objaid.getString("appointment_id"));
                                JSONObject objatype= response.getJSONObject(++i);
                                appointment.setAtype("Atype: " + objatype.getString("appointment_type"));

                                JSONObject objdoctor= response.getJSONObject(++i);
                                appointment.setDoctor("Doctor: " + objdoctor.getString("doctor"));

                                JSONObject objdate= response.getJSONObject(++i);
                                appointment.setDate("Date: "+objdate.getString("date"));

                                JSONObject objtime= response.getJSONObject(++i);
                                appointment.setTime("Time: "+objtime.getString("time"));


                                JSONObject objqueue= response.getJSONObject(++i);
                                appointment.setQueue(objqueue.getInt("queue"));

                                appointmentList.add(appointment);

                                // updating offset value to highest value
                                //if (i >= offSet)
                                //	offSet = i;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mAdapter.notifyDataSetChanged();
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }

        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
        hidePDialog();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (PD != null) {
            PD.dismiss();
            PD = null;
        }
    }

    public void delete(String uid,String aid){
        final String send_uid=uid;
        final String send_aid=aid;
        StringRequest postRequest = new StringRequest(Request.Method.POST, deleteurl,
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
                        hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                //String imagepost = image.substring(image.lastIndexOf('/')+1,image.length());
                params.put("uid", send_uid);
                Log.d("uid", send_uid);

                params.put("aid",send_aid);
                Log.d("report", send_aid);

                return params;
            }
        };
        mAdapter.notifyDataSetChanged();
        AppController.getInstance().addToRequestQueue(postRequest);
    }


    private void open(ApplicationInfo item) {
        // open app
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(item.packageName);
        List<ResolveInfo> resolveInfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(
                    activityPackageName, className);

            intent.setComponent(componentName);
            startActivity(intent);
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
