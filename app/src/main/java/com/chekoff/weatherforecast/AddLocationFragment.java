package com.chekoff.weatherforecast;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chekoff.weatherforecast.db.helper.DBHelper;
import com.chekoff.weatherforecast.db.model.Locations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddLocationFragment extends DialogFragment {
    boolean isDataLoading;
    Handler mHandler;

    public AddLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mHandler = new Handler();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_location, null);
        final EditText etLocationName = (EditText) dialogView.findViewById(R.id.etLocationName);

        builder.setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!isDataLoading)
                                    new AddLocationTask(getActivity()).execute(etLocationName.getText().toString().trim());
                            }
                        });


                        //addLocation(etLocationName.getText().toString().trim());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.setTitle("Add Location");
        // Create the AlertDialog object and return it
        return builder.create();
    }


    //adding location
    public String addLocation(final String locationName, Context mContext) {
        String resultMsg;
        //TODO DUPLICATE LOCATIONS NAMES
        DBHelper db = new DBHelper(getActivity());
        JSONObject jsonObject;
        SharedPreferences sharedPref;
        sharedPref = getActivity().getSharedPreferences("com.chekoff.weatherforecast", mContext.MODE_PRIVATE);


        jsonObject =
                new JSON("http://api.openweathermap.org/data/2.5/find?q=" + locationName, "location").getJsonObject();

        if (jsonObject == null) {
            resultMsg = "Could not find location: '" + locationName + "'.";


        } else {
            //check if location is already added
            try {
                JSONArray list = jsonObject.getJSONArray("list");
                final String jsonLocationName = list.getJSONObject(0).getString("name");
                int locationID = list.getJSONObject(0).getInt("id");
                String country = list.getJSONObject(0).getJSONObject("sys").getString("country");

                //check is requested location name is the same as returned location name

                if (locationName.toUpperCase().equals(jsonLocationName.toUpperCase())) {
                    //check if the location exists in database
                    if (db.checkLocationExists(locationID)) {
                        resultMsg = jsonLocationName + " is already added.";

                    } else {
                        //add location to database
                        Locations location = new Locations(jsonLocationName,
                                locationID,
                                country,
                                System.currentTimeMillis() / 1000);

                        if (db.createLocation(location) > 0) {
                            resultMsg = jsonLocationName + " was successfully added.";

                            //setting current location
                            int currentLocationID = sharedPref.getInt("set_current_location_id", 0);
                            if (currentLocationID == 0) {
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("set_current_location_id", locationID);
                                editor.putString("set_current_location_name",
                                        jsonLocationName + ", " + country);
                                editor.commit();

                                TextView txtCurrentLocation = (TextView) getActivity().findViewById(R.id.txtCurrentLocation);
                                txtCurrentLocation.setText(sharedPref.getString("set_current_location_name", ""));
                            }
                        } else {
                            resultMsg = "Error adding " + jsonLocationName + " to DB.";

                        }
                    }
                } else {

                    resultMsg = "Could not find location: '" + locationName + "'.";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                resultMsg = "";
            }
        }

        return resultMsg;
    }

    private class AddLocationTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        private Context mContext;

        public AddLocationTask(Context mContext) {
            this.mContext = mContext;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog before sending http request
            isDataLoading = true;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Searching location...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            return addLocation(params[0], mContext);

        }

        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();
            isDataLoading = false;

            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();

            super.onPostExecute(result);
        }
    }


}
