package com.example.yuen.e_carei;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuen.PatientReport;
import com.example.yuen.e_carei_app.CustomListAdapter;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei.Patientlist;
import com.example.yuen.info.androidhive.showpatientlist.PatientList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Case_history_review extends Activity {

    private static final String url = "http://192.168.56.1/ecare_db/read_case_history.php";
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
    Patientlist movie = new Patientlist();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history_review);


        final TextView no_of_case_history = (TextView) findViewById(R.id.no_of_case_history);
        TextView date_of_case_history = (TextView)findViewById(R.id.date_of_case_history);

        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        name = i.getStringExtra("name");

        namet = (TextView)findViewById(R.id.nameResult);
        idt = (TextView)findViewById(R.id.idResult);
        //namet.setText(name);
        //idt.setText(uid);

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
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
//                Intent modify_intent = new Intent(Case_history_review.this,
//                        Case_history_review_solo.class);
//
//                modify_intent.putExtra("uid", "1");
//                modify_intent.putExtra("case_number", no_of_case_history.getText());
//
//                startActivity(modify_intent);
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(Case_history_review.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Log.d(R.id.nav_1+"", menuItem.getItemId() + " ");
                Intent intent = new Intent();
                switch (menuItem.getItemId())
                {

                    case R.id.nav_1:

                        break;
                    case R.id.nav_2:
                        intent.setClass(Case_history_review.this,PatientReport.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_3:
                        intent.setClass(Case_history_review.this,PatientList.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_4:
                        //intent.setClass(Case_history_review.this,make_appointment.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    public void hi(){
        RequestQueue queue1 = Volley.newRequestQueue(this);

        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", "1");

        final JSONObject jsonObject = new JSONObject(params);

        JsonArrayRequest movieReq = new JsonArrayRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);

                                movie.setNo_of_case_history(obj.getString(no_case_history));
                                movie.setdate_of_case_history(obj.getString(date_case_history));

                                // adding movie to movies array
                                patientlists.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG1", "Error: " + error.getMessage());
                hidePDialog();

            }
        });
        AppController.getInstance().addToRequestQueue(movieReq);
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
