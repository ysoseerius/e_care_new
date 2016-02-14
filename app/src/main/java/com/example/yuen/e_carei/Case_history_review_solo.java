package com.example.yuen.e_carei;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yuen.PatientReport;
import com.example.yuen.info.androidhive.showpatientlist.PatientList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuen on 15/2/2016.
 */
public class Case_history_review_solo extends Activity {

    private static final String url = "http://127.0.0.1/ecare_db/read_case_history_solo.php";
    private List<Patientlist> caseItem = new ArrayList<Patientlist>();
    private ListView patientList;

    private ProgressDialog PD;
    private DrawerLayout drawerLayout;
    private String uid, name, doctor, medication, detail, report, case_number;
    private static final String no_case_history = "no_case_history";
    private static final String date_case_history = "date_case_history";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history_solo);


        Intent i = getIntent();
        //uid = i.getStringExtra("uid");
        //case_number = i.getStringExtra("case_number");





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(Case_history_review_solo.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                Intent intent = new Intent();
                switch (menuItem.getItemId())
                {

                    case R.id.nav_1:

                        break;
                    case R.id.nav_2:
                        intent.setClass(Case_history_review_solo.this,PatientReport.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_3:
                        intent.setClass(Case_history_review_solo.this,PatientList.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_4:
                        //intent.setClass(Case_history_review_solo.this,make_appointment.class);
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

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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
}

