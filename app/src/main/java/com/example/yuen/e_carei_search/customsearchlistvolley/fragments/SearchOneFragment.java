package com.example.yuen.e_carei_search.customsearchlistvolley.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_search.customsearchlistvolley.activity.SearchTabsActivity;
import com.example.yuen.e_carei_search.customsearchlistvolley.model.Search_list;

import java.util.ArrayList;


public class SearchOneFragment extends Fragment {

    private static final String TAG = Search_list.class.getSimpleName();
    private ProgressDialog pDialog;
    private TextInputLayout inputLayoutSearch;
    private Button btnSend;
    private Spinner spinner;
    private ArrayAdapter<String> searchList;
    private String item,input;
    private EditText inputSearch;
    private String[] em_item = {"","uid", "name", "hkid"};
    private ImageButton btnStartVoiceInput;
    private int InputResultCode = 1;

    public SearchOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_page, container, false);
        spinner = (Spinner) view.findViewById(R.id.search_spinner);
        searchList = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, em_item);
        spinner.setAdapter(searchList);
        inputSearch = (EditText) view.findViewById(R.id.input_search);

        inputSearch.setVisibility(View.GONE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // Toast.makeText(mContext, "你選的是" + em_level[position], Toast.LENGTH_SHORT).show();
                item = em_item[position].toString();
                if (em_item[position] != "") {
                    inputSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnStartVoiceInput = (ImageButton) view.findViewById(R.id.btnStartVoiceInput);
        btnStartVoiceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //调用系统传感器语音输入Api
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //必须输入项语言模式
                intent.putExtra(RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE, "en-UK");

                try {
                    startActivityForResult(intent, InputResultCode);
                    inputSearch.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getContext(),
                            "抱歉您的系统不支持语音识别输入。",
                            Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });

        Button btnsearch = (Button) view.findViewById(R.id.search_button);
        inputSearch.addTextChangedListener(new MyTextWatcher(inputSearch));
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(), SearchTabsActivity.class);
                intent.putExtra("item", item);//傳遞Double
                input=inputSearch.getText().toString();
                intent.putExtra("input", input);//傳遞String
                //切換Activity
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InputResultCode && resultCode == Activity.RESULT_OK && data != null) {
            //获取解析的结果
            ArrayList<String> voiceList = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            inputSearch.setText(voiceList.get(0));
        }
    }
        @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private boolean validateTitle() {
        if (inputSearch.getText().toString().trim().isEmpty()) {
            inputLayoutSearch.setError("Empty input!!");
            return false;
        } else {
            inputLayoutSearch.setErrorEnabled(false);
            inputSearch.setLinkTextColor(Color.WHITE);
        }

        return true;
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
                case R.id.input_search:
                    break;
            }
        }
    }



}
