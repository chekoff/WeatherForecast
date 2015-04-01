package com.chekoff.weatherforecast.db.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Plamen on 14.03.2015.
 */
public class Forecast {
    int id;
    long forDate;
    int locationId;
    int tempMin;
    int tempMax;
    double humidity;
    double wind;
    String condition;
    String description;
    String icon;
    String forecastCode;
    /*String weekDay;
    String fullDate;*/

    //constructors
    public Forecast() {
    }

    public Forecast(int forDate,
                    int locationId,
                    double tempMin,
                    double tempMax,
                    double humidity,
                    double wind,
                    String condition,
                    String description,
                    String icon,
                    String forecastCode) {

        this.forDate = forDate;
        this.locationId = locationId;
        this.tempMin = (int) Math.round(tempMin);
        this.tempMax = (int) Math.round(tempMax);
        this.humidity = humidity;
        this.wind = wind;
        this.condition = condition;
        this.description = description;
        this.icon = icon;
        this.forecastCode = forecastCode;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setForDate(int forDate) {
        this.forDate = forDate;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = (int) Math.round(tempMin);
    }

    public void setTempMax(double tempMax) {
        this.tempMax = (int) Math.round(tempMax);
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /*public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }*/

    //getters
    public int getId() {
        return this.id;
    }

    public long getForDate() {
        return this.forDate;
    }

    public String getForDateWeek() {
        SimpleDateFormat dateDayOfWeek = new SimpleDateFormat("E", Locale.US);

        return dateDayOfWeek.format(this.forDate * 1000);
    }

    public String getDateDate() {
        SimpleDateFormat date = new SimpleDateFormat("MMM d, yyyy", Locale.US);

        return date.format(this.forDate * 1000);
    }

    public String getHour() {
        SimpleDateFormat date = new SimpleDateFormat("kk:mm", Locale.US);

        return date.format(this.forDate * 1000);
    }

    public int getLocationId() {
        return this.locationId;
    }

    public int getTempMin() {
        return this.tempMin;
    }

    public int getTempMax() {
        return this.tempMax;
    }

    public double getHumidity() {
        return this.humidity;
    }

    public double getWind() {
        return this.wind;
    }

    public String getCondition() {
        return this.condition;
    }

    public String getDescription() {
        return this.description;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getForecastCode() {
        return forecastCode;
    }

    public void setForecastCode(String forecastCode) {
        this.forecastCode = forecastCode;
    }

    /*public String getWeekDay() {
        return this.weekDay;
    }

    public String getFullDate() {
        return this.fullDate;
    }*/
}
