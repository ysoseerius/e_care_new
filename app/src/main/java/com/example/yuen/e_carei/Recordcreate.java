package com.example.yuen.e_carei;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.activity.IconTextTabsActivity;
import com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView;
import com.example.yuen.e_carei_login.LoginActivity;
import com.example.yuen.e_carei_login.SQLiteHandler;
import com.example.yuen.e_carei_login.SessionManager;
import com.example.yuen.e_carei_search.customsearchlistvolley.activity.SearchTabsActivity;
import com.example.yuen.info.androidhive.showpatientlist.PatientList;

import java.util.HashMap;
import java.util.Map;

public class Recordcreate extends AppCompatActivity {

    private static final String create_record_url = "http://10.89.133.147/test/create_record.php";
    private DrawerLayout drawerLayout;
    int navItemId;
    private Context mContext;
    private Toolbar toolbar;
    EditText input_uid,input_case_number,input_report,input_medication,input_detail;
    private static final String NAV_ITEM_ID = "nav_index";

    private SQLiteHandler db;
    private TextView uid , name,time_label,date_label;
    private String doctor;
    private Button btnSend;
    private Spinner doctor_spinner;
    private ArrayAdapter<String> doctorList;

    private SessionManager session;

    private String[] em_doctor = {"Dr. Chan", "Dr. Leung", "Dr. Yip"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_create_page);

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
                Toast.makeText(Recordcreate.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                Intent intent = new Intent();
                switch (menuItem.getItemId()) {

                    case R.id.nav_p1:
                        intent.setClass(Recordcreate.this, PatientList.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_p2:
                        intent.setClass(Recordcreate.this, IconTextTabsActivity.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_p3:
                        intent.setClass(Recordcreate.this, IconTextTabsActivity.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_p4:
                        intent.setClass(Recordcreate.this, SearchTabsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_p5:
                        //logout
                        AlertDialog.Builder builder = new AlertDialog.Builder(Recordcreate.this);
                        //Uncomment the below code to Set the message and title from the strings.xml file
                        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to close this application ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        session.setLogin(false);
                                        db.deleteUsers();
                                        final Intent intent_logout = new Intent(Recordcreate.this, LoginActivity.class);
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

        input_uid=(EditText)findViewById(R.id.input_uid);
        Intent i = getIntent();
        input_uid.setText(i.getStringExtra("uid"));// from last page
        input_case_number=(EditText)findViewById(R.id.input_case_num);
        input_case_number.setText("");//from last page

        input_detail=(EditText)findViewById(R.id.input_detail);

        input_medication=(EditText)findViewById(R.id.input_medication);

        input_report = (EditText) findViewById(R.id.input_report);

        db = new SQLiteHandler(getApplicationContext());



        //uid.setText(dbuser.get("uid"));
        //naem.setText(dbuser.get("name"));

        btnSend = (Button) findViewById(R.id.btn_send);
        mContext = this.getApplicationContext();
        doctor_spinner= (Spinner)findViewById(R.id.doctor_spinner);

        doctorList = new ArrayAdapter<String>(Recordcreate.this, android.R.layout.simple_spinner_item, em_doctor);
        doctor_spinner.setAdapter(doctorList);
        doctor_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(mContext, "你選的是" + em_doctor[position], Toast.LENGTH_SHORT).show();
                doctor = em_doctor[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                StringRequest postRequest = new StringRequest(Request.Method.POST, create_record_url,
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
                        jsonParams.put("uid", input_uid.getText().toString());
                        Log.d("uid",input_uid.getText().toString());

                        jsonParams.put("case_number", input_case_number.getText().toString());
                        Log.d("case_number", input_case_number.getText().toString());

                        jsonParams.put("doctor", doctor);
                        Log.d("doctor", doctor);

                        jsonParams.put("medication", input_medication.getText().toString());
                        Log.d("medication", input_medication.getText().toString());

                        jsonParams.put("detail", input_detail.getText().toString());
                        Log.d("detail", input_detail.getText().toString());

                        jsonParams.put("report", input_report.getText().toString());
                        Log.d("report", input_report.getText().toString());

                        return jsonParams;
                    }
                };

                AppController.getInstance().addToRequestQueue(postRequest);
                onBackPressed();
            }

        });

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
