package com.example.yuen.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.yuen.e_carei.R;
import com.example.yuen.e_carei_app.AppController;

import java.util.List;

/**
 * Created by Yuen on 9/1/2016.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Case_history> caseItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Case_history> movieItems) {
        this.activity = activity;
        this.caseItems = movieItems;
    }

    @Override
    public int getCount() {
        return caseItems.size();
    }

    @Override
    public Object getItem(int location) {
        return caseItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.case_history_list_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        //NetworkImageView thumbNail = (NetworkImageView) convertView
        //        .findViewById(R.id.thumbnail);
        TextView no_of_case_history = (TextView) convertView.findViewById(R.id.no_of_case_history);
        TextView date_of_case_history = (TextView) convertView.findViewById(R.id.date_of_case_history);

        // getting movie data for the row
        Case_history cases = caseItems.get(position);

        // thumbnail image
        //thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        //title.setText(m.getTitle());

        // rating
        //rating.setText("Rating: " + String.valueOf(m.getRating()));

        return convertView;
    }

}
