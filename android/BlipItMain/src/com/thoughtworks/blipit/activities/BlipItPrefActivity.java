package com.thoughtworks.blipit.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.utils.BlipItUtils;

public class BlipItPrefActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.blipit_prefs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (BlipItUtils.RADIUS_PREF_KEY.equals(key)) {
            float radius = BlipItUtils.getRadius(sharedPreferences, key);
            if (radius > 10f) {
                Toast.makeText(this, R.string.radius_preference_failure, Toast.LENGTH_LONG).show();
                sharedPreferences.edit().putFloat(key, 2f).commit();
            }
        }
    }
}
