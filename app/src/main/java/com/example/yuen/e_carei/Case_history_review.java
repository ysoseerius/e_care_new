package com.example.yuen.e_carei;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yuen.PatientReport;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_app.CaseReviewDetail;
import com.example.yuen.e_carei_app.CustomListAdapter;
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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import za.co.neilson.alarm.AlarmActivity;

public class Case_history_review extends Activity {

    private static final String url = "http://192.168.43.216/test/read_case_history.php";
    private ListView patientList;
    private CustomListAdapter adapter;
    private TextView namet, idt;
    private ProgressDialog PD;
    private DrawerLayout drawerLayout;
    private String uid, name;
    private static final String no_case_history = "no_of_case_history";
    private static final String date_case_history = "date_of_case_history";


    private List<Patientlist> pl = new ArrayList<Patientlist>();
    private ArrayAdapter<String> listadapter;
    private List<Patientlist> patientlists = new ArrayList<Patientlist>();
    private String send_case_number = "1";

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history_review);
        setupWindowAnimations();
        //getWindow().setEnterTransition(new Explode());

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        final TextView no_of_case_history = (TextView) findViewById(R.id.no_of_case_history);
        TextView date_of_case_history = (TextView)findViewById(R.id.date_of_case_history);
        CirculaireNetworkImageView photo = (CirculaireNetworkImageView) findViewById(R.id.photo);

        Animation zoomInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
        photo.startAnimation(zoomInAnimation);

        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        name = i.getStringExtra("name");
        String photo_name = i.getStringExtra("image");

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> dbuser = db.getUserDetails();

        session = new SessionManager(getApplicationContext());

        namet = (TextView)findViewById(R.id.nameResult);
        idt = (TextView)findViewById(R.id.uid_show);

        final String saved_uid;
        final String saved_name;
        final String saved_image;
        if(uid != null && name != null) {
            namet.setText(name);
            idt.setText(uid);
            photo.setImageUrl("http://10.89.133.147/test/" + photo_name, imageLoader);

            saved_name=name;
            saved_uid=uid;
            saved_image=photo_name;

        }
        else
        {
            String username = dbuser.get("name");
            namet.setText(username);
            idt.setText(dbuser.get("uid"));
            photo.setImageUrl("http://10.89.133.147/test/" + dbuser.get("image"), imageLoader);

            saved_name=username;
            saved_uid=dbuser.get("uid");
            saved_image=dbuser.get("image");
            name = username;
            uid=dbuser.get("uid");
        }

        patientList = (ListView)findViewById(R.id.listView_case_history);
        adapter = new CustomListAdapter(this, patientlists);
        patientList.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("E-care");
        PD = new ProgressDialog(this);
        //Showing progress dialog before making http request
        PD.setMessage("Loading...");
        PD.show();

        hi();

        //Set onclick in list items
        patientList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Case_history_review.this, "row " + position + " was pressed", Toast.LENGTH_LONG).show();
                Log.d("position","0"+position);
                        TextView c =(TextView) view.findViewById(R.id.no_of_case_history);
                        String item = c.getText().toString();
                        final String saved_case_number = item.substring(13);
                        Intent i = new Intent();
                        i.setClass(Case_history_review.this, CaseReviewDetail.class);
                        i.putExtra("uid", saved_uid);
                        i.putExtra("name",saved_name);
                        i.putExtra("image",saved_image);
                        i.putExtra("case_number", saved_case_number);
                        startActivity(i);
                        Log.d("id",item);

            }
        });

//account type 2 : patient
        Log.d(dbuser.get("account_type") + "" , dbuser.get("account_type").equals("2")+"");
        if(dbuser.get("account_type").equals("2")) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
            view.getMenu().clear();
            view.inflateMenu(R.menu.drawer);
            view.getMenu().getItem(0).setChecked(true);

            view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Toast.makeText(Case_history_review.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                    Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                    Intent intent = new Intent();
                    switch (menuItem.getItemId()) {
                        case R.id.nav_1:
                            intent.setClass(Case_history_review.this,Case_history_review.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_2:
                            intent.setClass(Case_history_review.this, ShowAppointmentList.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_3:
                            intent.setClass(Case_history_review.this, Appointmentcreate.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_4:
                            intent.setClass(Case_history_review.this, AlarmActivity.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_5:
                            intent.setClass(Case_history_review.this, PatientReport.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_6:
                            //logout
                            AlertDialog.Builder builder = new AlertDialog.Builder(Case_history_review.this);
                            //Uncomment the below code to Set the message and title from the strings.xml file
                            //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                            //Setting message manually and performing action on button click
                            builder.setMessage("Do you want to close this application ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            session.setLogin(false);
                                            db.deleteUsers();
                                            final Intent intent_logout = new Intent(Case_history_review.this, LoginActivity.class);
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
            headerphoto.setImageUrl("http://10.89.133.147/test/" + dbuser.get("image"), imageLoader);
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
                    Toast.makeText(Case_history_review.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                    Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                    Intent intent = new Intent();
                    switch (menuItem.getItemId()) {

                        case R.id.nav_p1:
                            intent.setClass(Case_history_review.this, PatientList.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_p2:
                            intent.setClass(Case_history_review.this, IconTextTabsActivity.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_p3:
                            intent.setClass(Case_history_review.this, IconTextTabsActivity.class);
                            //intent .putExtra("name", "Hello B Activity");
                            startActivity(intent);
                            break;
                        case R.id.nav_p4:
                            intent.setClass(Case_history_review.this, SearchTabsActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_p5:
                            //logout
                            AlertDialog.Builder builder = new AlertDialog.Builder(Case_history_review.this);
                            //Uncomment the below code to Set the message and title from the strings.xml file
                            //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                            //Setting message manually and performing action on button click
                            builder.setMessage("Do you want to close this application ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            session.setLogin(false);
                                            db.deleteUsers();
                                            final Intent intent_logout = new Intent(Case_history_review.this, LoginActivity.class);
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
            headerphoto.setImageUrl("http://10.89.133.147/test/" + dbuser.get("image"), imageLoader);
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }
        //


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        if(dbuser.get("account_type").equals("1")) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent();
                    i.putExtra("uid", saved_uid);
                    i.setClass(Case_history_review.this, Recordcreate.class);
                    startActivity(i);
                }
            });
        }
    }


    public void setupWindowAnimations() {
        getWindow().setEnterTransition(new Explode());
        getWindow().getExitTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
        CirculaireNetworkImageView photo = (CirculaireNetworkImageView) findViewById(R.id.photo);

        Animation zoomInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
        photo.startAnimation(zoomInAnimation);

    }

    private Drawable loadImageFromURL(String url){
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable draw = Drawable.createFromStream(is, "src");
            return draw;
        }catch (Exception e) {
            //TODO handle error
            Log.i("loadingImg", e.toString());
            return null;
        }
    }

    public void hi(){

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("json", response.toString());

                        for (int i = 0; i < response.length();i++) {
                            try {
                                Log.d("length", response.length() + "");
                                Log.d("length i", i + "");


                                Patientlist movie = new Patientlist();

                                JSONObject objuid = response.getJSONObject(i);
                                if(objuid.getString("uid").equals(uid)) {

                                    JSONObject objcase = response.getJSONObject(++i);
                                    movie.setNo_of_case_history(objcase.getString("case_number"));
                                    //send to the create record to setText
                                    send_case_number = objcase.getString("case_number");

                                    JSONObject objdate = response.getJSONObject(++i);
                                    movie.setdate_of_case_history(objdate.getString("datef"));

                                    Log.d(objcase.getString("case_number"),objdate.getString("datef"));

                                    patientlists.add(movie);
                                }
                                else
                                {
                                    i = i+2;
                                }
                                // adding movie to movies array


                                Log.d(i+"",response.length()+"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hidePDialog();
                //TODO: handle failure
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(NAV_ITEM_ID, navItemId);
    }





}
