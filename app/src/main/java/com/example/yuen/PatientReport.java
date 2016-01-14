package com.example.yuen;

import android.content.Context;
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

import com.example.yuen.e_carei.Case_history_review;
import com.example.yuen.e_carei.R;

public class PatientReport extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    int navItemId;
    private Toolbar toolbar;
    private EditText inputEmail, inputTitle, inputMessage;
    private TextView level;
    private TextInputLayout inputLayoutEmail, inputLayoutTitle, inputLayoutMessage;
    private Button btnSend;
    private Spinner spinner;
    private ArrayAdapter<String> lunchList;
    private Context mContext;
    private static final String NAV_ITEM_ID = "nav_index";

    private String[] em_level = {"Low", "Medium", "High"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("E-care");
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(PatientReport.this, menuItem.getItemId() + " pressed", Toast.LENGTH_LONG).show();
                Log.d(R.id.nav_1 + "", menuItem.getItemId() + " ");
                switch (menuItem.getItemId()) {


                    case R.id.nav_1:

                        break;
                    case R.id.nav_2:
                        Intent intent = new Intent();
                        intent.setClass(PatientReport.this, Case_history_review.class);
                        //intent .putExtra("name", "Hello B Activity");
                        startActivity(intent);
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

        level= (TextView) findViewById(R.id.level);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutTitle = (TextInputLayout) findViewById(R.id.input_layout_title);
        inputLayoutMessage = (TextInputLayout) findViewById(R.id.input_layout_message);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputTitle = (EditText) findViewById(R.id.input_title);
        inputMessage = (EditText) findViewById(R.id.input_message);
        btnSend = (Button) findViewById(R.id.btn_send);
        mContext = this.getApplicationContext();
        spinner = (Spinner)findViewById(R.id.my_spinner);
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
        String message = "Emergency Level: " + spinner.getSelectedItem().toString() + "\n" + "Detail : " + inputMessage.getText().toString();

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
        //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, title);
        email.putExtra(Intent.EXTRA_TEXT, message);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
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
