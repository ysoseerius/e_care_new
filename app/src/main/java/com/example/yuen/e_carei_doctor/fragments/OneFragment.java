package com.example.yuen.e_carei_doctor.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yuen.e_carei.Case_history_review;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;
import com.example.yuen.e_carei_doctor.customlistviewvolley.adater.CustomListAdapter;
import com.example.yuen.e_carei_doctor.customlistviewvolley.model.Appointment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OneFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{



    private static final String TAG = Appointment.class.getSimpleName();
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

    // Movies json url
    private static final String url = "http://192.168.43.216/test/appointment_list.php";
    private ProgressDialog pDialog;
    private List<Appointment> appointmentList = new ArrayList<Appointment>();
    private ListView listView;
    private CustomListAdapter listadapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList arlist_uid=new ArrayList();
    private ArrayList arlist_name=new ArrayList();
    private ArrayList arlist_photo=new ArrayList();

    public OneFragment() {
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
        View view = inflater.inflate(R.layout.appointment_list, container, false);

        listView = (ListView) view.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        listadapter = new CustomListAdapter(getActivity(), appointmentList);
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
                String saved_image = arlist_photo.get(position).toString().substring(26);
                String saved_name = arlist_name.get(position).toString();
                String saved_uid = arlist_uid.get(position).toString();
                Log.d(saved_image,saved_name);

                Intent i = new Intent();
                i.setClass(getActivity(), Case_history_review.class);
                i.putExtra("uid", saved_uid);
                i.putExtra("name",saved_name);
                i.putExtra("image",saved_image);
                startActivity(i);
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
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        //Log.d("id", "hi") ;
                        // Parsing json
                        // reset the list
                        appointmentList.clear();
                        listadapter.notifyDataSetChanged();

                        for (int i = 0; i < response.length();i++) {
                            try {
                                Log.d("i","i:"+i);
                                Log.d(TAG, response.toString());
                                Appointment appointment = new Appointment();

                                Log.d("length", "length:" + response.length());

                                    JSONObject objname= response.getJSONObject(i);
                                    //get id
                                Log.d("name", objname.getString("name"));
                                    appointment.setName("Name:" + objname.getString("name"));
                                    arlist_name.add(objname.getString("name"));

                                    JSONObject objuid = response.getJSONObject(++i);
                                Log.d("uid",objuid.getString("uid"));
                                appointment.setUid("UID:"+objuid.getString("uid"));
                                    arlist_uid.add(objuid.getString("uid"));

                                    JSONObject objatype = response.getJSONObject(++i);
                                Log.d("atype",objatype.getString("appointment_type"));
                                    appointment.setAtype("Appointment type:"+objatype.getString("appointment_type"));

                                    JSONObject objctype = response.getJSONObject(++i);
                                Log.d("ctype",objctype.getString("consultation_type"));
                                    appointment.setCtype("Consultation type:"+objctype.getString("consultation_type"));

                                    JSONObject objtime = response.getJSONObject(++i);
                                Log.d("time",objtime.getString("time"));
                                    appointment.setTime("Time:"+objtime.getString("time"));

                                    JSONObject objqueue = response.getJSONObject(++i);
                                //Log.d("name",objqueue.getString("name"));
                                Log.d("queue",objqueue.getInt("queue")+"");
                                    appointment.setQueue(objqueue.getInt("queue"));

                                //Log.d("i","i:"+i);
                                //obj= response.getJSONObject(i+1);
                                //get image url second item
                                JSONObject objdate = response.getJSONObject(++i);
                                Log.d("image",objdate.getString("date"));
                                appointment.setDate("Date:"+objdate.getString("date"));

                                    JSONObject objimage = response.getJSONObject(++i);
                                Log.d("image",objimage.getString("image"));
                                    appointment.setThumbnailUrl(objimage.getString("image"));
                                arlist_photo.add(objimage.getString("image"));
                                //Log.d("i","i:"+i);

                                //patient.setTitle(obj.getString("id"));
                                //patient.setThumbnailUrl(obj.getString("image"));


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
                               // if(i%7==1)
                                    appointmentList.add(appointment);

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
