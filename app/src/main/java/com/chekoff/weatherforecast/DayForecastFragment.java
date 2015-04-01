package com.chekoff.weatherforecast;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chekoff.weatherforecast.db.helper.DBHelper;
import com.chekoff.weatherforecast.db.model.Forecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayForecastFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    Intent intent;
    String KEY_FORECAST_CODE = "hourly";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    public LayoutInflater mInflater;
    long currDate;
    SharedPreferences sharedPref;
    DayForecastAdapter dayForecastAdapter;
    private List<Forecast> forecastList;

    public DayForecastFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        sharedPref = getActivity().getSharedPreferences("com.chekoff.weatherforecast", Context.MODE_PRIVATE);

        final View rootView = inflater.inflate(R.layout.fragment_day_forecast, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light);

        TextView txtCurrentLocation = (TextView) rootView.findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(sharedPref.getString("set_current_location_name", ""));

        intent = getActivity().getIntent();
        String forDate = intent.getStringExtra("forDate");

        TextView txtFromDate = (TextView) rootView.findViewById(R.id.txtFromDate);
        txtFromDate.setText(forDate);


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

    public void getForecast(int requestNewData) {
        DBHelper db = new DBHelper(getActivity());
        int currentLocationId = sharedPref.getInt("set_current_location_id", 0);
        String units = sharedPref.getString("set_units", "metric").toLowerCase();

        if (requestNewData == 0)
            forecastList = db.getAllForecast(currentLocationId, KEY_FORECAST_CODE);

        //no data in DB or request new data
        if (forecastList.size() == 0 || requestNewData == 1) {
            JSONObject jsonObject = new JSON(/*String.format(
                    "http://api.openweathermap.org/data/2.5/forecast?id=%s&units=%s&mode=json",
                    currentLocationId,
                    units),
                    "forecast"*/
                    "http://chekoff.com/test/daily_forecast.txt", "forecast"
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

                    JSONObject jsonMain = jsonList.getJSONObject(i).getJSONObject("main");
                    double temp = jsonMain.getDouble("temp");
                    double humidity = jsonMain.getInt("humidity");

                    JSONObject jsonWind = jsonList.getJSONObject(i).getJSONObject("wind");
                    double wind = jsonWind.getDouble("speed");

                    JSONObject jsonWeather = jsonList.getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                    String condition = jsonWeather.getString("main");
                    String description = jsonWeather.getString("description");
                    String icon = jsonWeather.getString("icon");

                    db.createForecast(new Forecast(forDate,
                            currentLocationId,
                            temp,
                            temp,
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


    @Override
    public void onRefresh() {
        currDate = System.currentTimeMillis();

        getForecast(0);

        dayForecastAdapter = new DayForecastAdapter(this.mInflater, getActivity(), forecastList);
        setListAdapter(dayForecastAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
