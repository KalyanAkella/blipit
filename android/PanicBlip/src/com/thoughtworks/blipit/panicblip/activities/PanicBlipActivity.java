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
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.thoughtworks.blipit.panicblip.R;
import com.thoughtworks.blipit.panicblip.services.PanicNotificationService;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipServiceHelper;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils;
import com.thoughtworks.contract.common.Category;
import com.thoughtworks.contract.common.Channel;
import com.thoughtworks.contract.common.GetChannelsResponse;
import com.thoughtworks.contract.utils.ChannelUtils;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.APP_TAG;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.CLEAR_PANIC;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.PANIC_CHANNELS_KEY;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.REPORT_PANIC;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.getMessage;

public class PanicBlipActivity extends Activity implements View.OnClickListener, DialogInterface.OnCancelListener, SharedPreferences.OnSharedPreferenceChangeListener, AdapterView.OnItemClickListener {
    private Thread channelsThread;
    private Messenger panicNotificationService;
    private boolean panicReportingPending;
    private ListView panicChannelList;
    private View submitPanicBtn;
    private View clearPanicBtn;
    private List<Channel> panicChannels;
    private List<String> panicChannelNames;
    private PanicNotificationClientHandler panicNotificationClientHandler;
    private Messenger panicNotificationHandler;

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
                        GetChannelsResponse response = PanicBlipServiceHelper.getPublishResource(blipitServiceUrl).getAvailableChannels(Category.PANIC);
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
        panicChannelList.setOnItemClickListener(this);
    }

    private void updatePanicChannelList(String allChannelsStr) {
        if (allChannelsStr == null || allChannelsStr.length() == 0) return;
        panicChannels = ChannelUtils.toChannelList(allChannelsStr, Category.PANIC);
        panicChannelNames = ChannelUtils.toChannelNames(panicChannels);
        runOnUiThread(new Runnable() {
            public void run() {
                panicChannelList.setAdapter(new ArrayAdapter<String>(PanicBlipActivity.this, android.R.layout.simple_list_item_multiple_choice, panicChannelNames));
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

    // TODO: there's a service connection leak error when we exit the activity after clicking on Clear Panic. Need to fix this !!!
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    public void unbindService() {
        if (panicNotificationService != null) {
            try {
                panicNotificationService.send(Message.obtain(null, PanicBlipUtils.MSG_UNREGISTER_CLIENT));
            } catch (RemoteException e) {
                Log.e(APP_TAG, "Unable to unregister from panic notification service", e);
            }
        }
        unbindService(panicNotificationClientHandler);
    }

    private void initPanicButtons() {
        submitPanicBtn = findViewById(R.id.submit_panic_btn);
        submitPanicBtn.setOnClickListener(this);
        clearPanicBtn = findViewById(R.id.clear_panic_btn);
        clearPanicBtn.setOnClickListener(this);
    }

    private void initPanicNotificationService() {
        panicNotificationClientHandler = new PanicNotificationClientHandler(this);
        Intent intent = new Intent(this, PanicNotificationService.class);
        startService(intent);
        bindService(intent, panicNotificationClientHandler, 0);
        panicNotificationHandler = new Messenger(panicNotificationClientHandler);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.submit_panic_btn) {
            reportPanic();
        } else if (view.getId() == R.id.clear_panic_btn) {
            clearPanic();
        }
    }

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
            panicNotificationService.send(getMessage(REPORT_PANIC, getSelectedChannels()));
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

    public void setPanicNotificationService(Messenger panicNotificationService) {
        this.panicNotificationService = panicNotificationService;
    }

    public Messenger getPanicNotificationClientMessenger() {
        return panicNotificationHandler;
    }

    public void reportPendingPanics() {
        if (panicReportingPending) {
            _reportPanic();
            panicReportingPending = false;
        }
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

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (panicChannelList.isItemChecked(position)) {
            submitPanicBtn.setEnabled(true);
        } else {
            long[] checkedItemPositions = panicChannelList.getCheckedItemIds();
            if (checkedItemPositions.length == 0) {
                submitPanicBtn.setEnabled(false);
            } else {
                submitPanicBtn.setEnabled(true);
            }
        }
    }

    public void clearPanicSuccess() {
        showMessageOnUI("Issue cleared successfully");
        panicChannelList.clearChoices();
        panicChannelList.invalidateViews();
        clearPanicBtn.setEnabled(false);
        submitPanicBtn.setEnabled(false);
    }

    public void clearPanicFailure(String errorMessage) {
        showMessageOnUI("We are unable to clear your issue at this time. Please try again.");
        clearPanicBtn.setEnabled(true);
    }

    public void reportPanicSuccess() {
        showMessageOnUI("Issue reported successfully");
        clearPanicBtn.setEnabled(true);
    }

    public void reportPanicLocUnavailable() {
        showMessageOnUI("Your issue will be reported as soon as we detect your current location");
        clearPanicBtn.setEnabled(false);
    }

    public void reportPanicFailure(String errorMessage) {
        showMessageOnUI("We are unable to report your issue at this time. Please try again.");
        clearPanicBtn.setEnabled(false);
    }

    private void showMessageOnUI(final String message) {
        Toast.makeText(PanicBlipActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
