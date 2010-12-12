package com.thoughtworks.blipit.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.utils.BlipItUtils;

public class BlipItPrefActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.blipit_prefs);
        ListPreference listPreference = (ListPreference) getPreferenceScreen().findPreference(BlipItUtils.CHANNEL_PREF_KEY);
        listPreference.setEntries(getChannels());
        listPreference.setEntryValues(getChannelValues());
        listPreference.setDefaultValue("456");
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

    private CharSequence[] getChannelValues() {
        return new CharSequence[]{"123", "456", "789"};
    }

    private CharSequence[] getChannels() {
        return new CharSequence[]{"Food", "Retail", "Transport"};
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (BlipItUtils.RADIUS_PREF_KEY.equals(key)) {
            try {
                String radiusStr = sharedPreferences.getString(key, "2");
                Float.valueOf(radiusStr);
                Toast.makeText(this, R.string.radius_preference_success, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.radius_preference_failure, Toast.LENGTH_LONG).show();
            }
        }
    }
}
