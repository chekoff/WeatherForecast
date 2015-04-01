package com.chekoff.weatherforecast;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chekoff.weatherforecast.db.helper.DBHelper;
import com.chekoff.weatherforecast.db.model.Locations;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteLocationFragment extends DialogFragment {


    public DeleteLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final DBHelper db = new DBHelper(getActivity());
        final SharedPreferences sharedPref;
        sharedPref = getActivity().getSharedPreferences("com.chekoff.weatherforecast", Context.MODE_PRIVATE);


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Location")
                .setMessage("Are you sure you want to delete " + sharedPref.getString("set_current_location_name", "") + "?")
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        int currentLocationID = sharedPref.getInt("set_current_location_id", 0);

                        db.delLocation(currentLocationID);

                        Locations locations = db.getFirstLocations();
                        TextView txtCurrentLocation = (TextView) getActivity().findViewById(R.id.txtCurrentLocation);

                        SharedPreferences.Editor editor = sharedPref.edit();

                        if (locations != null) {
                            editor.putInt("set_current_location_id", locations.getLocationID());
                            editor.putString("set_current_location_name",
                                    locations.getLocationName() + ", " + locations.getCountry());
                            editor.commit();


                            txtCurrentLocation.setText(sharedPref.getString("set_current_location_name", ""));
                        } else {
                            editor.putInt("set_current_location_id", 0);
                            editor.putString("set_current_location_name", "Select Location");
                            editor.commit();

                            txtCurrentLocation.setText("Add Location");

                            Toast.makeText(getActivity(), "There are no locations added", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
