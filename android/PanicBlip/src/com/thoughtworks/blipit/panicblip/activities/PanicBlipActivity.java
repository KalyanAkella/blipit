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

package com.thoughtworks.blipit.panicblip.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.thoughtworks.blipit.panicblip.R;
import com.thoughtworks.blipit.panicblip.services.PanicNotificationService;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipServiceHelper;
import com.thoughtworks.contract.common.Channel;
import com.thoughtworks.contract.common.ChannelCategory;
import com.thoughtworks.contract.common.GetChannelsResponse;
import com.thoughtworks.contract.utils.ChannelUtils;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.APP_TAG;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.CLEAR_PANIC;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.PANIC_CHANNELS_KEY;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.REPORT_PANIC;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.getMessageWithChannels;

public class PanicBlipActivity extends Activity implements View.OnClickListener, ServiceConnection, DialogInterface.OnCancelListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private Thread channelsThread;
    private Messenger panicNotificationService;
    private boolean panicReportingPending;
    private ListView panicChannelList;
    private View submitPanicBtn;
    private View clearPanicBtn;
    private List<Channel> panicChannels;
    private List<String> panicChannelNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initPanicNotificationService();
        initPanicButtons();
        initPanicChannelList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String allChannelsStr = sharedPreferences.getString(PANIC_CHANNELS_KEY, null);
        if (allChannelsStr == null || allChannelsStr.length() == 0) {
            showDialog(0);
            channelsThread = new Thread() {
                @Override
                public void run() {
                    String channelObjectsAsString = "";
                    try {
                        String blipitServiceUrl = readBlipItServiceUrl();
                        GetChannelsResponse response = PanicBlipServiceHelper.getPublishResource(blipitServiceUrl).getAvailableChannels(ChannelCategory.PANIC);
                        if (response.isSuccess()) {
                            channelObjectsAsString = ChannelUtils.getChannelsAsString(response.getChannels());
                        } else {
                            showChannelsErrorToast();
                            Log.e(APP_TAG, response.getBlipItError().getMessage());
                        }
                    } catch (Exception e) {
                        showChannelsErrorToast();
                        Log.e(APP_TAG, "An error occurred while retrieving channels", e);
                    }
                    sharedPreferences.edit().putString(PANIC_CHANNELS_KEY, channelObjectsAsString).commit();
                    updatePanicChannelList(channelObjectsAsString);
                }
            };
            channelsThread.start();
        } else updatePanicChannelList(allChannelsStr);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initPanicChannelList() {
        panicChannelList = (ListView) findViewById(R.id.panicChannelList);
    }

    private void updatePanicChannelList(String allChannelsStr) {
        if (allChannelsStr == null || allChannelsStr.length() == 0) return;
        panicChannels = ChannelUtils.toChannelList(allChannelsStr);
        panicChannelNames = ChannelUtils.toChannelNames(panicChannels);
        runOnUiThread(new Runnable() {
            public void run() {
                panicChannelList.setAdapter(new ArrayAdapter<String>(PanicBlipActivity.this, android.R.layout.simple_list_item_multiple_choice, panicChannelNames));
                submitPanicBtn.setEnabled(true);
                clearPanicBtn.setEnabled(true);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we fetch the panic topics...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);
        return progressDialog;
    }

    private void showChannelsErrorToast() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Unable to retrieve channels. Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String readBlipItServiceUrl() {
        String result = null;
        try {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(this, PanicBlipActivity.class);
            ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA);
            result = activityInfo.metaData.getString("blipit.service.url");
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(APP_TAG, "Unable to retrieve activity metadata for " + PanicBlipActivity.class, e);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    private void initPanicButtons() {
        submitPanicBtn = findViewById(R.id.submit_panic_btn);
        submitPanicBtn.setOnClickListener(this);
        clearPanicBtn = findViewById(R.id.clear_panic_btn);
        clearPanicBtn.setOnClickListener(this);
    }

    private void initPanicNotificationService() {
        Intent intent = new Intent(this, PanicNotificationService.class);
        startService(intent);
        bindService(intent, this, 0);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.submit_panic_btn) {
            reportPanic();
        } else if (view.getId() == R.id.clear_panic_btn) {
            clearPanic();
        }
    }

    // TODO: does clear panic need restart of PNS if not already running !!!
    private void clearPanic() {
        if (panicNotificationService == null) {
            Toast.makeText(this, "Unable to clear all issues", Toast.LENGTH_LONG).show();
        } else {
            try {
                panicNotificationService.send(Message.obtain(null, CLEAR_PANIC));
            } catch (RemoteException e) {
                Log.e(APP_TAG, "Unable to clear all issues", e);
                Toast.makeText(this, "Unable to clear all issues", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void reportPanic() {
        if (panicNotificationService == null) {
            Log.i(APP_TAG, "PanicNotificationService not running. Starting it...");
            panicReportingPending = true;
            initPanicNotificationService();
        } else _reportPanic();
    }

    private void _reportPanic() {
        try {
            panicNotificationService.send(getMessageWithChannels(REPORT_PANIC, getSelectedChannels()));
        } catch (RemoteException e) {
            Log.e(APP_TAG, "Unable to submit your panic", e);
            Toast.makeText(this, "Unable to submit your panic", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Channel> getSelectedChannels() {
        ArrayList<Channel> channelList = new ArrayList<Channel>();
        SparseBooleanArray checkedItemPositions = panicChannelList.getCheckedItemPositions();
        for (int i = 0; i < panicChannelNames.size(); i++) {
            String channel = panicChannelNames.get(i);
            if (checkedItemPositions.get(i, false)) {
                for (Channel availableChannel : panicChannels) {
                    if (availableChannel.getName().equals(channel)) {
                        channelList.add(availableChannel);
                        break;
                    }
                }
            }
        }
        return channelList;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        panicNotificationService = new Messenger(iBinder);
        if (panicReportingPending) {
            _reportPanic();
            panicReportingPending = false;
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        panicNotificationService = null;
    }

    public void onCancel(DialogInterface dialog) {
        if (channelsThread != null) {
            channelsThread.interrupt();
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PANIC_CHANNELS_KEY.equals(key)) {
            dismissDialog(0);
        }
    }
}
