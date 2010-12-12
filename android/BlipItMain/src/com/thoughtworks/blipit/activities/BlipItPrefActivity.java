package com.thoughtworks.blipit.activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.utils.BlipItUtils;

public class BlipItPrefActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.blipit_prefs);
        ListPreference listPreference = (ListPreference) getPreferenceScreen().findPreference(BlipItUtils.CHANNEL_PREF_KEY);
        listPreference.setEntries(getChannels());
        listPreference.setEntryValues(getChannelValues());
        listPreference.setDefaultValue("456");
    }

    private CharSequence[] getChannelValues() {
        return new CharSequence[]{"123", "456", "789"};
    }

    private CharSequence[] getChannels() {
        return new CharSequence[]{"Food", "Retail", "Transport"};
    }
}
