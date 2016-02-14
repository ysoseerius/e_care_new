package com.example.yuen.e_carei_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yuen.e_carei.Patientlist;
import com.example.yuen.e_carei.R;

import java.util.List;

/**
 * Created by Yuen on 13/2/2016.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Patientlist> caseItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Patientlist> movieItems) {
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

        TextView no_of_case_history = (TextView) convertView.findViewById(R.id.no_of_case_history);
        TextView date_of_case_history = (TextView) convertView.findViewById(R.id.date_of_case_history);

        // getting movie data for the row
        Patientlist m = caseItems.get(position);

        no_of_case_history.setText("Case History " + m.getNo_of_case_history());
        date_of_case_history.setText(m.getDate_of_case_history());

        return convertView;
    }

}
