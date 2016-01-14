package com.example.yuen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yuen.e_carei.R;

public class PatientReport extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText inputEmail, inputTitle, inputMessage;
    private TextView level;
    private TextInputLayout inputLayoutEmail, inputLayoutTitle, inputLayoutMessage;
    private Button btnSend;
    private Spinner spinner;
    private ArrayAdapter<String> lunchList;
    private Context mContext;
    private String[] em_level = {"Low", "Medium", "High"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_report);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
