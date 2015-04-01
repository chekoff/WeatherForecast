package com.chekoff.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chekoff.weatherforecast.db.helper.DBHelper;
import com.chekoff.weatherforecast.db.model.Forecast;
import com.chekoff.weatherforecast.db.model.Locations;
//import com.chekoff.weatherforecast.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//import android.app.ListFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ForecastFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    String KEY_FORECAST_CODE = "daily";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public LayoutInflater mInflater;
    long currDate;
    SharedPreferences sharedPref;
    ForecastAdapter forecastAdapter;
    private List<Forecast> forecastList;

    //constructor
    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = inflater;
        sharedPref = getActivity().getSharedPreferences("com.chekoff.weatherforecast", Context.MODE_PRIVATE);

        final View rootView = inflater.inflate(R.layout.forecast_main_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light);

        TextView txtCurrentLocation = (TextView) rootView.findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(sharedPref.getString("set_current_location_name", ""));

        ImageButton imgButtLocationMap = (ImageButton) rootView.findViewById(R.id.imgButtLocationMap);
        imgButtLocationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + sharedPref.getString("set_current_location_name", "")));
                startActivity(intent);
            }
        });

        onRefresh();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView txtCurrentLocation = (TextView) getActivity().findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(sharedPref.getString("set_current_location_name", ""));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), DayForecastActivity.class);
        intent.putExtra("forDate", forecastList.get(position).getDateDate());
        startActivity(intent);

    }

    @Override
    public void onRefresh() {
        currDate = System.currentTimeMillis();

        getForecast(0);

        forecastAdapter = new ForecastAdapter(this.mInflater, getActivity(), forecastList);
        setListAdapter(forecastAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }

      //TODO Async
    private class FetchForecastData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void getForecast(int requestNewData) {
        DBHelper db = new DBHelper(getActivity());
        int currentLocationId = sharedPref.getInt("set_current_location_id", 0);
        String units = sharedPref.getString("set_units", "metric").toLowerCase();
        int forecastPeriod = sharedPref.getInt("set_forecast_days", 5);

        if (requestNewData == 0)
            forecastList = db.getAllForecast(currentLocationId, KEY_FORECAST_CODE);

        //no data in DB or request new data
        if (forecastList.size() == 0 || requestNewData == 1) {
            JSONObject jsonObject = new JSON(/*String.format(
                    "http://api.openweathermap.org/data/2.5/forecast/daily?id=%s&units=%s&cnt=%s&mode=json",
                    currentLocationId,
                    units,
                    "" + forecastPeriod),
                    "forecast"*/
                    "http://chekoff.com/test/openweather.txt", "forecast"
            ).getJsonObject();

            if (jsonObject == null) {
                Toast.makeText(getActivity(), "Can not fetch forecast data. \nTry again later.", Toast.LENGTH_SHORT).show();
                return;
            }

            //fetching forecast data
            try {
                JSONArray jsonList = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonList.length(); i++) {
                    int forDate = jsonList.getJSONObject(i).getInt("dt");
                    double humidity = jsonList.getJSONObject(i).getInt("humidity");
                    double wind = jsonList.getJSONObject(i).getDouble("speed");

                    JSONObject jsonTemp = jsonList.getJSONObject(i).getJSONObject("temp");
                    double tempMin = jsonTemp.getDouble("min");
                    double tempMax = jsonTemp.getDouble("max");

                    JSONObject jsonWeather = jsonList.getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                    String condition = jsonWeather.getString("main");
                    String description = jsonWeather.getString("description");
                    String icon = jsonWeather.getString("icon");

                    db.createForecast(new Forecast(forDate,
                            currentLocationId,
                            tempMin,
                            tempMax,
                            humidity,
                            wind,
                            condition,
                            description,
                            icon,
                            KEY_FORECAST_CODE));
                }

                forecastList = db.getAllForecast(currentLocationId, KEY_FORECAST_CODE);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
