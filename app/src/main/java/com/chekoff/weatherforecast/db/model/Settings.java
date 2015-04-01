package com.chekoff.weatherforecast.db.model;

/**
 * Created by Plamen on 19.03.2015.
 */
public class Settings {
    int days_count;
    int update_interval;
    String units;
    int hourly_forecast;

    //constructor
    public Settings() {
    }

    public Settings(int days_count, int update_interval, String units, int hourly_forecast) {
        this.days_count = days_count;
        this.update_interval = update_interval;
        this.units = units;
        this.hourly_forecast = hourly_forecast;
    }

    //setters
    public void setDays_count(int days_count) {
        this.days_count = days_count;
    }

    public void setUpdate_interval(int update_interval) {
        this.update_interval = update_interval;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setHourly_forecast(int hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    //getters
    public int getDays_count() {
        return days_count;
    }

    public int getUpdate_interval() {
        return update_interval;
    }

    public String getUnits() {
        return units;
    }

    public int getHourly_forecast() {
        return hourly_forecast;
    }


}
