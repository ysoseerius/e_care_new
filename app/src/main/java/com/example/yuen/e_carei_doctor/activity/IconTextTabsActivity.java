package com.example.yuen.e_carei_doctor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.android.volley.toolbox.ImageLoader;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.customlistviewvolley.CirculaireNetworkImageView;
import com.example.yuen.e_carei_doctor.fragments.OneFragment;
import com.example.yuen.e_carei_doctor.fragments.TwoFragment;
import com.example.yuen.e_carei_login.LoginActivity;
import com.example.yuen.e_carei_login.SQLiteHandler;
import com.example.yuen.e_carei_login.SessionManager;
import com.example.yuen.e_carei_search.customsearchlistvolley.activity.SearchTabsActivity;
import com.example.yuen.info.androidhive.showpatientlist.PatientList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class IconTextTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TextView username;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int navItemId;

    private SessionManager session;

    private SQLiteHandler db;

    private int[] tabIcons = {
            R.drawable.icon_queue,
            R.drawable.icon_pill,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_text_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("E-care");
        //setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView username = (TextView)findViewById(R.id.drawer_name);

        session = new SessionManager(getApplicationContext());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().getItem(1).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(IconTextTabsActivity.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                Intent intent = new Intent();
                switch (menuItem.getItemId())
                {
                    case R.id.nav_p1:
                        intent.setClass(IconTextTabsActivity.this, PatientList.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_p2:
                        intent.setClass(IconTextTabsActivity.this, IconTextTabsActivity.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_p3:
                        intent.setClass(IconTextTabsActivity.this, IconTextTabsActivity.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_p4:
                        intent.setClass(IconTextTabsActivity.this, SearchTabsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_p5:
                        //logout
                        AlertDialog.Builder builder = new AlertDialog.Builder(IconTextTabsActivity.this);
                        //Uncomment the below code to Set the message and title from the strings.xml file
                        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to close this application ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        session.setLogin(false);
                                        db.deleteUsers();
                                        final Intent intent_logout = new Intent(IconTextTabsActivity.this, LoginActivity.class);
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
        String username_show = dbuser.get("name");
        headerName.setText(username_show);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        CirculaireNetworkImageView headerphoto = (CirculaireNetworkImageView) header.findViewById(R.id.drawer_thumbnail);
        headerphoto.setImageUrl("http://192.168.43.216/test/" + dbuser.get("image"), imageLoader);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_case_history_review, menu);
        return true;
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


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "Appointment List");
        adapter.addFrag(new TwoFragment(), "Take medication");

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
