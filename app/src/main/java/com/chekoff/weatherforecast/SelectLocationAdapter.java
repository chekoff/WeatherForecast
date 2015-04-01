package com.chekoff.weatherforecast;

import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chekoff.weatherforecast.db.model.Locations;

import java.util.List;

/**
 * Created by Plamen on 27.03.2015.
 */
public class SelectLocationAdapter extends BaseAdapter {
    int rowCount;
    LayoutInflater inflater;
    List<Locations> locationsList;

    public SelectLocationAdapter(LayoutInflater inflater, List<Locations> locationsList) {
        this.rowCount = locationsList.size();
        this.inflater = inflater;
        this.locationsList = locationsList;

    }

    @Override
    public int getCount() {
        return rowCount;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflater.inflate(R.layout.select_location_list_item, parent, false);

        TextView txtLocationName = (TextView) convertView.findViewById(R.id.txtLocationName);
        txtLocationName.setText(locationsList.get(position).getLocationName() + ", " + locationsList.get(position).getCountry());

        return convertView;
    }

}
