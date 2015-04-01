package com.chekoff.weatherforecast;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chekoff.weatherforecast.db.helper.DBHelper;
import com.chekoff.weatherforecast.db.model.Locations;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectLocationFragment extends DialogFragment {
    List<Locations> locationsList;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ViewGroup viewGroup;


    public SelectLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        sharedPref = getActivity().getSharedPreferences("com.chekoff.weatherforecast", Context.MODE_PRIVATE);
        DBHelper db = new DBHelper(getActivity());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View convertView = inflater.inflate(R.layout.select_location_list, null, false);

        builder.setTitle(R.string.select_location);
        builder.setView(convertView);

        locationsList = db.getAllLocations();

        ListView listView = (ListView) convertView.findViewById(R.id.select_location_list);
        final SelectLocationAdapter adapter = new SelectLocationAdapter(inflater, locationsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                editor = sharedPref.edit();
                editor.putInt("set_current_location_id", locationsList.get(position).getLocationID());
                editor.putString("set_current_location_name",
                        locationsList.get(position).getLocationName() + ", " + locationsList.get(position).getCountry());
                editor.commit();

                Intent intent = getActivity().getIntent();
                if (intent.getStringExtra(intent.EXTRA_TEXT) != null &&
                        intent.getStringExtra(intent.EXTRA_TEXT).contains("Settings")) {
                    getActivity().overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(intent);
                } else {
                    TextView txtCurrentLocation = (TextView) getActivity().findViewById(R.id.txtCurrentLocation);
                    txtCurrentLocation.setText(sharedPref.getString("set_current_location_name", ""));

                }


                /*UpdatePreferences updatePreferences = new UpdatePreferences();
                updatePreferences.preferenceKey = "set_current_location_id";
                updatePreferences.preferenceSummary = locationsList.get(position).getLocationName() + ", " + locationsList.get(position).getCountry();

                updatePreferences.updSummary();*/


                getDialog().dismiss();

            }
        });

        return builder.create();
    }

}
