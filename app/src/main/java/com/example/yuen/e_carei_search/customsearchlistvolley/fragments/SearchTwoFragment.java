package com.example.yuen.e_carei_search.customsearchlistvolley.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_search.customsearchlistvolley.adater.CustomSearchListAdapter;
import com.example.yuen.e_carei_search.customsearchlistvolley.model.Search_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchTwoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{



    private static final String TAG = Search_list.class.getSimpleName();
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

    // Movies json url
    private static final String searchurl = "http://192.168.43.216/test/search_list.php";
    private ProgressDialog pDialog;
    private List<Search_list> SearchList = new ArrayList<Search_list>();
    private ListView listView;
    private CustomSearchListAdapter listadapter;
    private String input="";
    private String item="";
    private int num_search_result = 0;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SearchTwoFragment() {
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
        View view = inflater.inflate(R.layout.search_result_list, container, false);
        // Inflate the layout for this fragment
        listView = (ListView) view.findViewById(R.id.search_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        listadapter = new CustomSearchListAdapter(getActivity(), SearchList);
        listView.setAdapter(listadapter);

        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        // changing action bar color
        // getActionBar().setBackgroundDrawable(
        //        new ColorDrawable(Color.parseColor("#00BBD3")));

        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchPatients();
                                    }
                                }
        );

        listView.setAdapter(listadapter);

        // Click event for single list row
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "row " + position + " was pressed", Toast.LENGTH_LONG).show();
                switch (position) {
                    case 0:
                        TextView c = (TextView) view.findViewById(R.id.title);
                        String item = c.getText().toString();
                        Log.d("id", item);
                        break;

                    case 1:
                        break;
                }

            }

        });

        return view;
    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        fetchPatients();
    }

    /**
     * Fetching movies json by making http call
     */
    private void fetchPatients() {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest Req = new JsonArrayRequest(searchurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        Log.d("id", "hi") ;
                        // Parsing json
                        // reset the list
                        SearchList.clear();
                        listadapter.notifyDataSetChanged();

                        for (int i = 0; i < response.length();i++) {
                            try {
                                Log.d("id", "hi1");
                                Log.d("i", "i:" + i);

                                Search_list searchlist = new Search_list();
                                String get_input="";
                                String check_input = "";

                                Log.d("length", "length:" + response.length());

                                JSONObject objuid = response.getJSONObject(i);
                                JSONObject objname = response.getJSONObject(++i);
                                //get name
                                JSONObject objhkid = response.getJSONObject(++i);

                                JSONObject objimage = response.getJSONObject(++i);
                                Intent intent= getActivity().getIntent();
                                Bundle b = intent.getExtras();
                                if(b!=null)
                                {
                                    item = (String) b.get("item");
                                    input = (String) b.get("input");
                                }


                                Log.d(get_input,check_input);
                                if(input != null) {
                                    Log.d(item,input);
                                    check_input = input;
                                }

                                if(item != null) {
                                    if (item.equals("name")) {
                                        get_input = objname.getString(item);
                                    } else if (item.equals("uid")) {
                                        get_input = objuid.getString(item);
                                    } else if (item.equals("hkid")) {
                                        get_input = objuid.getString(item);
                                    }
                                }

                                Log.d(get_input,check_input);
                                if (get_input.contains(check_input)) {
                                    Log.d(get_input,check_input);
                                    Log.d("","");
                                        Log.d("uid", objuid.getString("uid"));
                                        searchlist.setUid(objuid.getString("uid"));
                                        Log.d("name", objname.getString("name"));
                                        searchlist.setName(objname.getString("name"));
                                        Log.d("hkid", objhkid.getString("hkid"));
                                        searchlist.setThumbnailUrl(objimage.getString("image"));
                                        num_search_result++;
                                        SearchList.add(searchlist);
                                }
                                // updating offset value to highest value
                                //if (i >= offSet)
                                //	offSet = i;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        listadapter.notifyDataSetChanged();
                        Log.d("num", num_search_result + "");
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(Req);

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

    //public PatientList() {
    //Required empty public constructor

    //}



}
