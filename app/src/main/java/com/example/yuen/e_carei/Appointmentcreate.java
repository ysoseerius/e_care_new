package com.example.yuen.e_carei;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_login.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Appointmentcreate extends AppCompatActivity {

    private static final String create_appointment_url = "http://192.168.43.216/test/create_appointment.php";
    private static final String check_full_url = "http://192.168.43.216/test/check_full.php";
    private DrawerLayout drawerLayout;
    int navItemId;
    private Toolbar toolbar;
    private static final String NAV_ITEM_ID = "nav_index";

    private SQLiteHandler db;
    private TextView uid , name,time_label,date_label;
    private Button btnSend;
    private Spinner spinner1,spinner2,spinner3;
    private TextView tvInvisibleError_time;
    private ArrayAdapter<String> dateList,typeList,timeList;
    private Context mContext;
    private String atype_get;
    private String time_get="0";

    Date today = new Date();

    Date increment0 = addDay(today, 0);
    Date increment1 = addDay(today, 1);
    Date increment2 = addDay(today, 2);
    Date increment3 = addDay(today, 3);
    Date increment4 = addDay(today, 4);
    Date increment5 = addDay(today, 5);
    Date increment6 = addDay(today, 6);
    Date increment7 = addDay(today, 7);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    String date1 = formatter.format(increment1);
    String date2 = formatter.format(increment2);
    String date3 = formatter.format(increment3);
    String date4 = formatter.format(increment4);
    String date5 = formatter.format(increment5);
    String date6 = formatter.format(increment6);
    String date7 = formatter.format(increment7);
    String date_get = formatter.format(increment0);

    private String[] em_date = {date1,date2,date3,date4,date5,date6,date7};
    private String[] em_type = {"Select appointment type","A","B"};
    private String[] em_time = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_create_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("E-care");
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().getItem(1).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(Appointmentcreate.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                switch (menuItem.getItemId()) {


                    case R.id.nav_1:
                        Intent intent = new Intent();
                        intent.setClass(Appointmentcreate.this, Case_history_review.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_2:

                        break;
                    case R.id.nav_3:
                        break;
                    case R.id.nav_4:
                        break;
                    case R.id.nav_5:
                        break;
                    case R.id.nav_6:
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

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        tvInvisibleError_time= (TextView) findViewById(R.id.tvInvisibleError_time);

        uid = (TextView)findViewById(R.id.uid_show);
        name = (TextView)findViewById(R.id.name_show);
        time_label  = (TextView)findViewById(R.id.time_label);
        date_label  = (TextView)findViewById(R.id.date_label);

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> dbuser = db.getUserDetails();

        //uid.setText(dbuser.get("uid"));
        //naem.setText(dbuser.get("name"));

        btnSend = (Button) findViewById(R.id.btn_send);
        mContext = this.getApplicationContext();
        spinner1 = (Spinner)findViewById(R.id.atype_spinner);
        spinner2 = (Spinner)findViewById(R.id.date_spinner);
        spinner3 = (Spinner)findViewById(R.id.time_spinner);

        time_label.setVisibility(view.GONE);
        date_label.setVisibility(view.GONE);
        spinner2.setVisibility(view.GONE);
        spinner3.setVisibility(view.GONE);

        typeList = new ArrayAdapter<String>(Appointmentcreate.this, android.R.layout.simple_spinner_item, em_type);
        spinner1.setAdapter(typeList);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(mContext, "你選的是" + em_type[position], Toast.LENGTH_SHORT).show();
                atype_get=em_type[position];
                if (em_type[position] == "A") {
                    time_label.setVisibility(arg1.VISIBLE);
                    date_label.setVisibility(arg1.VISIBLE);
                    spinner2.setVisibility(arg1.VISIBLE);
                    spinner3.setVisibility(arg1.VISIBLE);
                }
                if (em_type[position] == "B") {
                    time_label.setVisibility(arg1.GONE);
                    date_label.setVisibility(arg1.GONE);
                    spinner2.setVisibility(arg1.GONE);
                    spinner3.setVisibility(arg1.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

            dateList = new ArrayAdapter<String>(Appointmentcreate.this, android.R.layout.simple_spinner_item, em_date);
            spinner2.setAdapter(dateList);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Toast.makeText(mContext, "你選的是" + em_date[position], Toast.LENGTH_SHORT).show();
                    date_get = em_date[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });



            timeList = new ArrayAdapter<String>(Appointmentcreate.this, android.R.layout.simple_spinner_item, em_time);
            spinner3.setAdapter(timeList);
            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    //Toast.makeText(mContext, "你選的是" + em_time[position], Toast.LENGTH_SHORT).show();
                    time_get = em_time[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String uid = "P001";
                final String name = "Chan Tai Man";

                StringRequest postRequest = new StringRequest(Request.Method.POST, create_appointment_url,
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
                        Log.d("uid", uid);
                        jsonParams.put("appointment_type", atype_get);
                        Log.d("atype", atype_get);
                        jsonParams.put("time", time_get);
                        Log.d("time", time_get);
                        jsonParams.put("doctor", "Dr. Chan");
                        jsonParams.put("date", date_get);
                        Log.d("date", date_get);

                        return jsonParams;
                    }
                };

                AppController.getInstance().addToRequestQueue(postRequest);

            if(!atype_get.equals("B"))
                {
                    //check to see the slot is full or not.1. add data to db and calculate the no of data in the db 2. If >5 , delete it
                    JsonArrayRequest movieReq = new JsonArrayRequest(check_full_url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d("Response", response.toString());
                                    //Log.d("id", "hi") ;
                                    // Parsing json
                                    // reset the list


                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            //Log.d("id","hi1") ;
                                            //Log.d("i","i:"+i);
                                            //get id
                                            JSONObject objnum = response.getJSONObject(i);
                                            int number_of_queue = objnum.getInt("number_of_queue");
                                            Log.d("number_of_queue", number_of_queue + "");
                                            if (number_of_queue > 5) {
                                                TextView error_msg = (TextView) findViewById(R.id.error_msg);
                                                error_msg.setVisibility(View.GONE);
                                                error_msg.setText("**Sorry!!This time slot if full**");
                                                error_msg.setTextColor(Color.RED);
                                                error_msg.setVisibility(View.VISIBLE);

                                                Toast.makeText(view.getContext(), "number_of_queue:" + number_of_queue + ">5", Toast.LENGTH_SHORT).show();
                                            } else {
                                                TextView error_msg = (TextView) findViewById(R.id.error_msg);
                                                error_msg.setVisibility(View.GONE);
                                                Toast.makeText(view.getContext(), "number_of_queue:" + number_of_queue + "<5", Toast.LENGTH_SHORT).show();

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

        });

    }

    public void setError(View v, CharSequence s) {
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setError(s);
    }

    public static Date addDay(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
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
