package com.chekoff.weatherforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.lang.reflect.Array;
import java.util.Arrays;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences preferences;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        preferences = getActivity().getSharedPreferences("com.chekoff.weatherforecast", Context.MODE_PRIVATE);

        Preference currentLocationID = findPreference("set_current_location_id");
        ListPreference units = (ListPreference) findPreference("set_units");
        EditTextPreference forecastDays = (EditTextPreference) findPreference("set_forecast_days");
        final ListPreference unitsUnits = (ListPreference) findPreference("set_units_units");

        currentLocationID.setSummary(preferences.getString("set_current_location_name", "Location is not set"));
        /*units.setSummary(preferences.getString("set_units", "Select unit system"));
        forecastDays.setSummary(preferences.getString("set_forecast_days", "Select forecast period"));*/
        units.setSummary(units.getValue());
        forecastDays.setSummary(forecastDays.getText());
        unitsUnits.setSummary(unitsUnits.getValue());

        if (units.getValue().toString().equals("Metric")) {
            unitsUnits.setDefaultValue(R.string.set_units_metric_default);
            unitsUnits.setEntries(R.array.set_units_metric);
            unitsUnits.setEntryValues(R.array.set_units_metric);
        }

        if (units.getValue().toString().equals("Imperial")) {
            unitsUnits.setDefaultValue(R.string.set_units_imperial_default);
            unitsUnits.setEntries(R.array.set_units_imperial);
            unitsUnits.setEntryValues(R.array.set_units_imperial);
        }

        currentLocationID.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SelectLocationFragment selectLocationFragment = new SelectLocationFragment();
                selectLocationFragment.show(getFragmentManager(), "selectLocationFragment");

                preference.setSummary(preferences.getString("set_current_location_name", "Location is not set"));

                return true;
            }
        });

        currentLocationID.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                //currentLocationName.setSummary(preferences.getString("set_current_location_name", "Location is not set"));
                preference.setSummary(preferences.getString("set_current_location_name", "Location is not set"));
                return true;
            }
        });


        units.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());


                if (newValue.toString().equals("Metric")) {
                    String[] unitsArray = getResources().getStringArray(R.array.set_units_metric);
                    if (!Arrays.asList(unitsArray).contains(unitsUnits.getValue())) {
                        unitsUnits.setValue(getString(R.string.set_units_metric_default));
                        unitsUnits.setSummary(unitsUnits.getValue());
                        unitsUnits.setDefaultValue(R.string.set_units_metric_default);
                        unitsUnits.setEntries(R.array.set_units_metric);
                        unitsUnits.setEntryValues(R.array.set_units_metric);
                    }
                }

                if (newValue.toString().equals("Imperial")) {
                    String[] unitsArray = getResources().getStringArray(R.array.set_units_imperial);
                    if (!Arrays.asList(unitsArray).contains(unitsUnits.getValue())) {
                        unitsUnits.setValue(getString(R.string.set_units_imperial_default));
                        unitsUnits.setSummary(unitsUnits.getValue());
                        unitsUnits.setDefaultValue(R.string.set_units_imperial_default);
                        unitsUnits.setEntries(R.array.set_units_imperial);
                        unitsUnits.setEntryValues(R.array.set_units_imperial);
                    }
                }

                return true;
            }
        });

        unitsUnits.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
                return true;
            }
        });


        forecastDays.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
