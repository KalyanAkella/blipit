/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package com.thoughtworks.blipit.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.utils.BlipItHttpHelper;

import static com.thoughtworks.blipit.utils.BlipItUtils.AD_CHANNELS_KEY;
import static com.thoughtworks.blipit.utils.BlipItUtils.APP_TAG;
import static com.thoughtworks.blipit.utils.BlipItUtils.RADIUS_PREF_KEY;
import static com.thoughtworks.blipit.utils.BlipItUtils.getRadius;

public class BlipItPrefActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, DialogInterface.OnCancelListener {
    private Thread channelsThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.blipit_prefs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String allChannelsStr = sharedPreferences.getString(AD_CHANNELS_KEY, null);
        if (allChannelsStr == null || allChannelsStr.length() == 0) {
            loadChannels(sharedPreferences);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private String readBlipItServiceLoc() {
        String result =  null;
        try {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(this, BlipItPrefActivity.class);
            ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA);
            result = activityInfo.metaData.getString("blipit.service.loc");
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(APP_TAG, "Unable to retrieve activity metadata for " + BlipItPrefActivity.class, e);
        }
        return result;
    }

    private void showChannelsErrorToast() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Unable to retrieve channels. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we fetch the channels...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);
        return progressDialog;
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
        if (RADIUS_PREF_KEY.equals(key)) {
            float radius = getRadius(sharedPreferences, key);
            if (radius > 10f) {
                Toast.makeText(this, R.string.radius_preference_failure, Toast.LENGTH_LONG).show();
                sharedPreferences.edit().putFloat(key, 2f).commit();
            }
        } else if (AD_CHANNELS_KEY.equals(key)) {
            dismissDialog(0);
        }
    }

    public void onCancel(DialogInterface dialog) {
        if (channelsThread != null) {
            channelsThread.interrupt();
        }
    }

    private void loadChannels(final SharedPreferences sharedPreferences) {
        showDialog(0);
        channelsThread = new Thread() {
            @Override
            public void run() {
                String channelsAsString = "";
                try {
                    String blipitServiceLoc = readBlipItServiceLoc();
                    channelsAsString = BlipItHttpHelper.getInstance().getAllChannelsAsJson(blipitServiceLoc);
                    if (channelsAsString == null || channelsAsString.length() == 0) {
                        showChannelsErrorToast();
                    }
                } catch (Exception e) {
                    showChannelsErrorToast();
                    Log.e(APP_TAG, "An error occurred while retrieving ad channels", e);
                }
                sharedPreferences.edit().putString(AD_CHANNELS_KEY, channelsAsString).commit();
            }
        };
        channelsThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.blipit_pref_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reload_channels_item) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            loadChannels(sharedPreferences);
        }
        return true;
    }
}
