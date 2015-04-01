package com.chekoff.weatherforecast.db.model;

/**
 * Created by Plamen on 14.03.2015.
 */
public class Locations {
    int id;
    int locationID;
    String locationName;
    long lastFetched;
    String country;

    //constructor
    public Locations() {

    }

    public Locations(String locationName, int locationID, String country, long lastFetched) {
        this.locationName = locationName;
        this.locationID = locationID;
        this.lastFetched = lastFetched;
        this.country = country;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public void setLastFetched(long lastFetched) {
        this.lastFetched = lastFetched;
    }

    //getters
    public int getId() {
        return this.id;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public int getLocationID() {
        return this.locationID;
    }

    public long getLastFetched() {
        return this.lastFetched;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}