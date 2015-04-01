package com.chekoff.weatherforecast.db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chekoff.weatherforecast.db.model.Forecast;
import com.chekoff.weatherforecast.db.model.Locations;
import com.chekoff.weatherforecast.db.model.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plamen on 14.03.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DBHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "w_myweather";

    //Table Names
    private static final String TABLE_SETTINGS = "w_settings";
    private static final String TABLE_LOCATIONS = "w_locations";
    private static final String TABLE_FORECAST = "w_forecast";

    //Common Columns
    private static final String KEY_ID = "id";

    //Table Settings Columns
    private static final String KEY_SET_DAYS = "days_count";
    private static final String KEY_SET_UPDATE_INTERVAL = "update_interval";
    private static final String KEY_SET_UNITS = "units";
    private static final String KEY_SET_HOURLY_FORECAST = "hourly_forecast";


    //Table Locations Columns
    private static final String KEY_LOCATION_NAME = "location_name";
    private static final String KEY_LOCATION_COUNTRY = "country";
    private static final String KEY_LAST_FETCHED = "last_fetched";

    //Table Forecast Column
    private static final String KEY_FOR_DATE = "for_date";
    private static final String KEY_LOCATION_ID = "location_id";
    private static final String KEY_TEMP_MIN = "temp_min";
    private static final String KEY_TEMP_MAX = "temp_max";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_WIND = "wind";
    private static final String KEY_CONDITION = "condition";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ICON = "icon";
    private static final String KEY_FORECAST_CODE = "forecast_code";

    // Table Create Statements
    //Table Settings
    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE  " +
            TABLE_SETTINGS + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            KEY_SET_DAYS + " INTEGER, " +
            KEY_SET_UPDATE_INTERVAL + " INTEGER, " +
            KEY_SET_UNITS + " TEXT, " +
            KEY_SET_HOURLY_FORECAST + " INTEGER" +
            ")";

    //Table Locations
    private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE " +
            TABLE_LOCATIONS + "(" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            KEY_LOCATION_ID + " INTEGER, " +
            KEY_LOCATION_NAME + " TEXT, " +
            KEY_LOCATION_COUNTRY + " TEXT, " +
            KEY_LAST_FETCHED + " INTEGER" +
            ")";

    //Table Forecast
    private static final String CREATE_TABLE_FORECAST = "CREATE TABLE " +
            TABLE_FORECAST + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_FOR_DATE + " INTEGER," +
            KEY_LOCATION_ID + " INTEGER," +
            KEY_TEMP_MIN + " INTEGER," +
            KEY_TEMP_MAX + " INTEGER," +
            KEY_HUMIDITY + " REAL," +
            KEY_WIND + " REAL," +
            KEY_CONDITION + " TEXT," +
            KEY_DESCRIPTION + " TEXT," +
            KEY_ICON + " TEXT, " +
            KEY_FORECAST_CODE + " TEXT" +
            ")";


    //constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating tables
        db.execSQL(CREATE_TABLE_SETTINGS);
        db.execSQL(CREATE_TABLE_LOCATIONS);
        db.execSQL(CREATE_TABLE_FORECAST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORECAST);

        //create tables again
        onCreate(db);

    }

    //Create Settings
    public long createSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SET_DAYS, settings.getDays_count());
        values.put(KEY_SET_UPDATE_INTERVAL, settings.getUpdate_interval());
        values.put(KEY_SET_UNITS, settings.getUnits());
        values.put(KEY_SET_HOURLY_FORECAST, settings.getHourly_forecast());

        long settingID = db.insert(TABLE_SETTINGS, null, values);

        closeDB();

        return settingID;
    }

    //Get Settings
    public Settings readSettings() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SETTINGS;

        Cursor c_res = db.rawQuery(selectQuery, null);

        c_res.moveToFirst();

        Settings settings = new Settings(
                c_res.getInt(c_res.getColumnIndex(KEY_SET_DAYS)),
                c_res.getInt(c_res.getColumnIndex(KEY_SET_UPDATE_INTERVAL)),
                c_res.getString(c_res.getColumnIndex(KEY_SET_UNITS)),
                c_res.getInt(c_res.getColumnIndex(KEY_SET_HOURLY_FORECAST)));

        closeDB();

        return settings;
    }

    //Update Settings
    public long updateSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SET_DAYS, settings.getDays_count());
        values.put(KEY_SET_UPDATE_INTERVAL, settings.getUpdate_interval());
        values.put(KEY_SET_UNITS, settings.getUnits());
        values.put(KEY_SET_HOURLY_FORECAST, settings.getHourly_forecast());

        long settingsID = db.update(TABLE_FORECAST, values, null, null);

        closeDB();

        return settingsID;
    }

    //Create Location
    public long createLocation(Locations location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION_NAME, location.getLocationName());
        values.put(KEY_LOCATION_COUNTRY, location.getCountry());
        values.put(KEY_LOCATION_ID, location.getLocationID());
        values.put(KEY_LAST_FETCHED, location.getLastFetched());

        //insert row
        long locationId = db.insert(TABLE_LOCATIONS, null, values);

        closeDB();

        //return location_id KEY_ID
        return locationId;
    }//public long createLocation(Locations location)

    //Get All Locations
    public boolean checkLocationExists(int locationID) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_LOCATIONS +
                " WHERE " + KEY_LOCATION_ID + " = " + locationID;

        Cursor c_res = db.rawQuery(selectQuery, null);

        boolean result;
        if (c_res.getCount() > 0)
            result = true;
        else
            result = false;

        closeDB();

        return result;
    }//public boolean checkLocationExists(int locationID)

    //Get first location in DB
    public Locations getFirstLocations() {
        Locations location = new Locations();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_LOCATIONS + " ORDER BY " + KEY_LOCATION_NAME + " LIMIT 1";

        Cursor c_res = db.rawQuery(selectQuery, null);

        if (c_res.getCount() == 0)
            return null;
        else if (c_res.moveToFirst()) {
            do {
                location.setId(c_res.getInt(c_res.getColumnIndex(KEY_ID)));
                location.setLocationName(c_res.getString(c_res.getColumnIndex(KEY_LOCATION_NAME)));
                location.setCountry(c_res.getString(c_res.getColumnIndex(KEY_LOCATION_COUNTRY)));
                location.setLocationID(c_res.getInt(c_res.getColumnIndex(KEY_LOCATION_ID)));
                location.setLastFetched(c_res.getInt(c_res.getColumnIndex(KEY_LAST_FETCHED)));
            }
            while (c_res.moveToNext());
        }

        closeDB();

        return location;
    }//public Locations getFirstLocations()

    //Get All Locations
    public List<Locations> getAllLocations() {
        List<Locations> locationsList = new ArrayList<Locations>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_LOCATIONS + " ORDER BY " + KEY_LOCATION_NAME;

        Cursor c_res = db.rawQuery(selectQuery, null);

        if (c_res.moveToFirst()) {
            do {
                Locations location = new Locations();

                location.setId(c_res.getInt(c_res.getColumnIndex(KEY_ID)));
                location.setLocationName(c_res.getString(c_res.getColumnIndex(KEY_LOCATION_NAME)));
                location.setCountry(c_res.getString(c_res.getColumnIndex(KEY_LOCATION_COUNTRY)));
                location.setLocationID(c_res.getInt(c_res.getColumnIndex(KEY_LOCATION_ID)));
                location.setLastFetched(c_res.getInt(c_res.getColumnIndex(KEY_LAST_FETCHED)));

                //adding object to list
                locationsList.add(location);
            }

            while (c_res.moveToNext());
        }

        closeDB();

        return locationsList;
    }//public List<Locations> getAllLocations()

    //Delete Location
    public void delLocation(long location_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LOCATIONS, KEY_LOCATION_ID + " = ?", new String[]{String.valueOf(location_id)});
        delForecast(location_id);

        closeDB();
    }//public void delLocation(int location_id)

    //Create Forecast
    public long createForecast(Forecast forecast) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOR_DATE, forecast.getForDate());
        values.put(KEY_LOCATION_ID, forecast.getLocationId());
        values.put(KEY_TEMP_MIN, forecast.getTempMin());
        values.put(KEY_TEMP_MAX, forecast.getTempMax());
        values.put(KEY_HUMIDITY, forecast.getHumidity());
        values.put(KEY_WIND, forecast.getWind());
        values.put(KEY_CONDITION, forecast.getCondition());
        values.put(KEY_DESCRIPTION, forecast.getDescription());
        values.put(KEY_ICON, forecast.getIcon());
        values.put(KEY_FORECAST_CODE, forecast.getForecastCode());

        //insert row
        long forecastId = db.insert(TABLE_FORECAST, null, values);

        closeDB();

        //return forecast_id KEY_ID
        return forecastId;
    }//public long createForecast(Forecast forecast)

    //Get Forecast for Location
    public List<Forecast> getAllForecast(int locationId, String forecastCode) {

        List<Forecast> forecastList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_FORECAST +
                " WHERE " + KEY_LOCATION_ID + " = " + locationId +
                " AND " + KEY_FORECAST_CODE + " = '" + forecastCode + "'";
        //DI
        Log.e(LOG, selectQuery);

        Cursor c_res = db.rawQuery(selectQuery, null);

        if (c_res.moveToFirst()) {
            do {
                Forecast forecast = new Forecast();

                forecast.setId(c_res.getInt(c_res.getColumnIndex(KEY_ID)));
                forecast.setForDate(c_res.getInt(c_res.getColumnIndex(KEY_FOR_DATE)));
                forecast.setTempMin(c_res.getInt(c_res.getColumnIndex(KEY_TEMP_MIN)));
                forecast.setTempMax(c_res.getInt(c_res.getColumnIndex(KEY_TEMP_MAX)));
                forecast.setCondition(c_res.getString(c_res.getColumnIndex(KEY_CONDITION)));
                forecast.setDescription(c_res.getString(c_res.getColumnIndex(KEY_DESCRIPTION)));
                forecast.setHumidity(c_res.getDouble(c_res.getColumnIndex(KEY_HUMIDITY)));
                forecast.setWind(c_res.getDouble(c_res.getColumnIndex(KEY_WIND)));
                forecast.setIcon(c_res.getString(c_res.getColumnIndex(KEY_ICON)));
                forecast.setLocationId(locationId);

                //adding object to list
                forecastList.add(forecast);

            } while (c_res.moveToNext());
        }

        closeDB();

        return forecastList;
    }//public List<Forecast> getAllForecast(int locationId)

    //delete all forecast for location
    public void delForecast(long locationId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_FORECAST, KEY_LOCATION_ID + " =? ", new String[]{String.valueOf(locationId)});

        closeDB();
    } //public int delForecast(int locationId)

    //delete all forecast data
    public void delAllForecast() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_FORECAST, null, null);

        closeDB();
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
