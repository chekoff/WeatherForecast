package com.chekoff.weatherforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chekoff.weatherforecast.db.model.Forecast;

import java.util.List;

/**
 * Created by Plamen on 31.03.2015.
 */
public class DayForecastAdapter extends BaseAdapter{
    LayoutInflater mInflater;
    int rowCount;
    List<Forecast> forecastList;
    Context mContext;

    public DayForecastAdapter(LayoutInflater mInflater, FragmentActivity activity, List<Forecast> forecastList) {
        this.mInflater = mInflater;
        this.mContext = activity;
        this.rowCount = forecastList.size();
        this.forecastList = forecastList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemsView;
        Forecast forecast;

        if (convertView == null) {
            itemsView = mInflater.inflate(R.layout.forecast_main_list_item, parent, false);
        } else {
            itemsView = convertView;
        }

        forecast = forecastList.get(position);

        TextView txtWeekDay = (TextView) itemsView.findViewById(R.id.txtWeekDay);
        TextView txtDate = (TextView) itemsView.findViewById(R.id.txtDate);
        TextView txtTempMaxMin = (TextView) itemsView.findViewById(R.id.txtTempMaxMin);
        TextView txtCondition = (TextView) itemsView.findViewById(R.id.txtCondition);
        TextView txtDescription = (TextView) itemsView.findViewById(R.id.txtDescription);
        TextView txtHumidity = (TextView) itemsView.findViewById(R.id.txtHumidity);
        TextView txtWindSpeed = (TextView) itemsView.findViewById(R.id.txtWindSpeed);

        ImageView imgIcon = (ImageView) itemsView.findViewById(R.id.imgIcon);
        imgIcon.setImageResource(mContext.getResources().getIdentifier("ic_" + forecast.getIcon().substring(0, 3), "mipmap", mContext.getPackageName()));

        //setting constants for conversion
        //SharedPreferences sharedPreferences = mContext.getSharedPreferences("com.chekoff.weatherforecast", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String units = sharedPreferences.getString("set_units", "Metric");
        String unitsUnits = sharedPreferences.getString("set_units_units", "km/h");
        String degrees = "C";

        double tempMinConverted = 0;
        double windSpeedConverted = 0;


        if (units.equals("Metric")) {
            tempMinConverted = Math.round(forecast.getTempMin());
            degrees = "C";

            if (unitsUnits.equals("m/s")) {
                windSpeedConverted = Math.round(forecast.getWind() * 100.0) / 100.0;
            }
            if (unitsUnits.equals("km/h")) {
                windSpeedConverted = Math.round(forecast.getWind() * 100.0 * 3.6) / 100.0;
            }
        }

        if (units.equals("Imperial")) {
            tempMinConverted = Math.round(forecast.getTempMin() * 1.8 + 32);
            degrees = "F";

            if (unitsUnits.equals("fps")) {
                windSpeedConverted = Math.round(forecast.getWind() * 100.0 * 3.28084) / 100.0;
            }
            if (unitsUnits.equals("mph")) {
                windSpeedConverted = Math.round(forecast.getWind() * 100.0 * 2.23694) / 100.0;
            }
        }


        txtWeekDay.setText("" + forecast.getHour());
        txtDate.setText("" + forecast.getDateDate());
        txtTempMaxMin.setText("" + Math.round(tempMinConverted) + "\u00B0" + degrees);
        txtCondition.setText("" + forecast.getCondition());
        txtDescription.setText("" + forecast.getDescription());
        txtHumidity.setText("Humidity: " + forecast.getHumidity() + "%");
        txtWindSpeed.setText("Wind Speed: " + windSpeedConverted + " " + unitsUnits);



        return itemsView;
    }

    @Override
    public int getCount() {
        return this.rowCount;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
