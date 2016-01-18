package com.example.yuen.e_carei_login;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
//    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
//    private Button btnLinkToRegister;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private Button qrbutton;
    private String qrresult;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Login");


        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
//        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        qrbutton = (Button)findViewById(R.id.qrbutton);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, com.example.yuen.e_carei.Case_history_review.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(username, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
//        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        RegisterActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });

        qrbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent("la.droid.qr.scan");    //使用QRDroid的掃描功能
                i.putExtra("la.droid.qr.complete", true);   //完整回傳，不截掉scheme
                try {
                    //開啟 QRDroid App 的掃描功能，等待 call back onActivityResult()
                    startActivityForResult(i, 0);
                } catch(ActivityNotFoundException ex) {
                    //若沒安裝 QRDroid，則開啟 Google Play商店，並顯示 QRDroid App
                    Toast.makeText(getApplicationContext(),
                            "Please install this QR code scanner first", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=la.droid.qr"));
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( 0==requestCode && null!=data && data.getExtras()!=null ) {
            //掃描結果存放在 key 為 la.droid.qr.result 的值中
            qrresult = data.getExtras().getString("la.droid.qr.result");

            qrresult = qrresult.substring(3);

            Log.d("hihihhi", qrresult);

            qrLogin(qrresult);
        }
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();


        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite


                        JSONObject user = jObj.getJSONObject("user");
                        String uid = user.getString("uid");
                        String name = user.getString("name");
                        String age = user.getString("age");
                        String sex = user.getString("sex");
                        String hkid = user.getString("hkid");
                        String account_type = user.getString("Account_type");
                        String image = user.getString("image");
                        String phone = user.getString("phone");


                        // Inserting row in users table
                        db.addUser(uid, name, age, sex, hkid, account_type, image, phone);

                        // Launch main activity
//                        if(account_type=="1")
//                            ;
//                            else
//                            ;
                        HashMap<String, String> dbuser = db.getUserDetails();

                        String name2 = dbuser.get("name");

                        Toast.makeText(getApplicationContext(),
                                "Hello, " + name2 + " . " + "Welcome back! ", Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(LoginActivity.this,
                                com.example.yuen.e_carei.Case_history_review.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void qrLogin(final String qrresult) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_QRLOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        JSONObject user = jObj.getJSONObject("user");
                        String uid = user.getString("uid");
                        String name = user.getString("name");
                        String age = user.getString("age");
                        String sex = user.getString("sex");
                        String hkid = user.getString("hkid");
                        String account_type = user.getString("Account_type");
                        String image = user.getString("image");
                        String phone = user.getString("phone");


                        // Inserting row in users table
                        db.addUser(uid, name, age, sex, hkid, account_type, image, phone);

                        HashMap<String, String> dbuser = db.getUserDetails();

                        String name2 = dbuser.get("name");

                        Toast.makeText(getApplicationContext(),
                                "Hello, " + name2 + " . " + "Welcome back! ", Toast.LENGTH_LONG).show();

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                com.example.yuen.e_carei.Case_history_review.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//               Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", qrresult);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
