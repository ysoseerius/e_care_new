package com.example.yuen.e_carei_doctor.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.yuen.e_carei_doctor.customlistviewvolley.adater.CustomListAdapter_take;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.customlistviewvolley.model.Take_med_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TwoFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{



    private static final String TAG = Take_med_list.class.getSimpleName();
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

    // Movies json url
    private static final String takelisturl = "http://192.168.43.216/test/take_med_list.php";
    private ProgressDialog pDialog;
    private List<Take_med_list> takemedlistList = new ArrayList<Take_med_list>();
    private ListView listView;
    private CustomListAdapter_take listadapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    public TwoFragment() {
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
        View view = inflater.inflate(R.layout.take_med_list, container, false);

        listView = (ListView) view.findViewById(R.id.take_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        listadapter = new CustomListAdapter_take(getActivity(), takemedlistList);
        listView.setAdapter(listadapter);

        pDialog = new ProgressDialog(getActivity());
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
                Toast.makeText(getActivity(), "row " + position + " was pressed", Toast.LENGTH_LONG).show();
                switch (position) {
                    case 0:
                        TextView c =(TextView)view.findViewById(R.id.take_title);
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

        // appending offset to url
        //String url = URL_TOP_250 + offSet;

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(takelisturl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        Log.d("id", "hi") ;
                        // Parsing json
                        // reset the list
                        takemedlistList.clear();
                        listadapter.notifyDataSetChanged();

                        for (int i = 0; i < response.length();i++) {
                            try {
                                Log.d("id","hi1") ;
                                Log.d("i","i:"+i);

                                Take_med_list takemedlist = new Take_med_list();

                                Log.d("length", "length:" + response.length());

                                JSONObject objuid= response.getJSONObject(i);
                                //get name
                                Log.d("uid",objuid.getString("uid"));
                                takemedlist.setUid("UID: " + objuid.getString("uid"));

                                JSONObject objname= response.getJSONObject(++i);
                                Log.d("name", objname.getString("name"));
                                //get name
                                takemedlist.setName("Name: "+objname.getString("name"));

                                JSONObject objaid= response.getJSONObject(++i);
                                //get name
                                Log.d("aid",objaid.getString("appointment_id"));
                                takemedlist.setAid(objaid.getInt("appointment_id"));

                                //Log.d("i","i:"+i);
                                //obj= response.getJSONObject(i+1);
                                //get image url second item
                                JSONObject objimage= response.getJSONObject(++i);
                                takemedlist.setThumbnailUrl(objimage.getString("image"));
                                //Log.d("i","i:"+i);


                                //takemedlist.setTitle(obj.getString("id"));
                                //takemedlist.setThumbnailUrl(obj.getString("image"));


                                // Log.d("id",obj.getString("id")) ;
                                //  Log.d("image",obj.getString("image")) ;
								/*movie.setRating(((Number) obj.get("rating"))
										.doubleValue());
								movie.setYear(obj.getInt("releaseYear"));

								// Genre is json array
								JSONArray genreArry = obj.getJSONArray("genre");
								ArrayList<String> genre = new ArrayList<String>();
								for (int j = 0; j < genreArry.length(); j++) {
									genre.add((String) genreArry.get(j));
								}
								movie.setGenre(genre);
								*/

                                // adding movie to movies array
                                //if(i%2==1)
                                    takemedlistList.add(takemedlist);

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

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
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
