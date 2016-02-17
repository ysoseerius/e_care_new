package com.example.yuen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
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

import za.co.neilson.alarm.AlarmActivity;

public class PatientReport extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    int navItemId;
    private Toolbar toolbar;
    private static final String NAV_ITEM_ID = "nav_index";

    private EditText inputEmail, inputTitle, inputMessage;
    private TextView level;
    private TextInputLayout inputLayoutEmail, inputLayoutTitle, inputLayoutMessage;
    private Button btnSend;
    private Spinner spinner;
    private ArrayAdapter<String> lunchList;
    private Context mContext;

    private SQLiteHandler db;
    private SessionManager session;
    private String username;
    private String uid;

    private String[] em_level = {"Low", "Medium", "High"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("E-care");
        setSupportActionBar(toolbar);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().getItem(4).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(PatientReport.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                switch (menuItem.getItemId()) {
                    case R.id.nav_1:
                        intent.setClass(PatientReport.this,Case_history_review.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_2:
                        intent.setClass(PatientReport.this, queueshow.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_3:
                        intent.setClass(PatientReport.this, Appointmentcreate.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_4:
                        intent.setClass(PatientReport.this, AlarmActivity.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_5:
                        intent.setClass(PatientReport.this, PatientReport.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
                        break;
                    case R.id.nav_6:
                        //logout
                        AlertDialog.Builder builder = new AlertDialog.Builder(PatientReport.this);
                        //Uncomment the below code to Set the message and title from the strings.xml file
                        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to close this application ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        session.setLogin(false);
                                        db.deleteUsers();
                                        final Intent intent_logout = new Intent(PatientReport.this, LoginActivity.class);
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
        username = dbuser.get("name");
        uid = dbuser.get("uid");
        headerName.setText(username);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        CirculaireNetworkImageView headerphoto = (CirculaireNetworkImageView) header.findViewById(R.id.drawer_thumbnail);
        headerphoto.setImageUrl("http://192.168.43.216/test/" + dbuser.get("image"), imageLoader);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        level= (TextView) findViewById(R.id.date_label);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_title);
        inputLayoutMessage = (TextInputLayout) findViewById(R.id.input_layout_message);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputTitle = (EditText) findViewById(R.id.input_title);
        inputMessage = (EditText) findViewById(R.id.input_message);
        btnSend = (Button) findViewById(R.id.btn_send);
        mContext = this.getApplicationContext();
        spinner = (Spinner)findViewById(R.id.time_spinner);

        inputEmail.setText("ng2b30@hotmail.com");
        lunchList = new ArrayAdapter<String>(PatientReport.this, android.R.layout.simple_spinner_item, em_level);
        spinner.setAdapter(lunchList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // Toast.makeText(mContext, "你選的是" + em_level[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputTitle.addTextChangedListener(new MyTextWatcher(inputTitle));
        inputMessage.addTextChangedListener(new MyTextWatcher(inputMessage));

        level.setTextSize(getResources().getDimension(R.dimen.textsize));
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
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
    /**
     * If the form is valid, the button will immediately intent to the email client
     */
    private void submitForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validateTitle()) {
            return;
        }

        if (!validateMessage()) {
            return;
        }

        String to = inputEmail.getText().toString();
        String title = inputTitle.getText().toString();
        String message = "Uid: "+ uid +"\n" + "Username: " + username + "\n" +"Emergency Level: " + spinner.getSelectedItem().toString() + "\n" + "Detail : " + inputMessage.getText().toString();

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
        //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, title);
        email.putExtra(Intent.EXTRA_TEXT, message);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
        Toast.makeText(PatientReport.this, "Report has sent to the doctor", Toast.LENGTH_LONG).show();
        finish();

    }

    private boolean validateEmail() {

        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
            inputEmail.setLinkTextColor(Color.WHITE);
        }

        return true;
    }

    private boolean validateTitle() {
        if (inputTitle.getText().toString().trim().isEmpty()) {
            inputLayoutTitle.setError(getString(R.string.err_msg_title));
            requestFocus(inputTitle);
            return false;
        } else {
            inputLayoutTitle.setErrorEnabled(false);
            inputTitle.setLinkTextColor(Color.WHITE);
        }

        return true;
    }

    private boolean validateMessage() {
        if (inputMessage.getText().toString().trim().isEmpty()) {
            inputLayoutMessage.setError(getString(R.string.err_msg_message));
            requestFocus(inputMessage);
            return false;
        } else {
            inputLayoutMessage.setErrorEnabled(false);
            inputMessage.setLinkTextColor(Color.WHITE);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_title:
                    validateTitle();
                    break;
                case R.id.input_message:
                    validateMessage();
                    break;
            }
        }
    }
}
