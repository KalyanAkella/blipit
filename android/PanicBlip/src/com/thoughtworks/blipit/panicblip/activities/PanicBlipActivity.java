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
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.thoughtworks.blipit.panicblip.R;
import com.thoughtworks.blipit.panicblip.services.PanicNotificationService;

import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.APP_TAG;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.CLEAR_ALL_ISSUES;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.REPORT_ISSUE;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.getMessageWithIssues;

public class PanicBlipActivity extends Activity implements View.OnClickListener, ServiceConnection {
    private Messenger panicNotificationService;
    private boolean issueReportingPending;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initPanicNotificationService();
        initPanicButtons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    private void initPanicButtons() {
        findViewById(R.id.report_issue_btn).setOnClickListener(this);
        findViewById(R.id.clear_all_issues_btn).setOnClickListener(this);
    }

    private void initPanicNotificationService() {
        Intent intent = new Intent(this, PanicNotificationService.class);
        startService(intent);
        bindService(intent, this, 0);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.report_issue_btn) {
            reportIssue();
        } else if (view.getId() == R.id.clear_all_issues_btn) {
            clearAllIssues();
        }
    }

    // TODO: does clear all issues need restart of PNS if not already running !!!
    private void clearAllIssues() {
        if (panicNotificationService == null) {
            Toast.makeText(this, "Unable to clear all issues", Toast.LENGTH_LONG).show();
        } else {
            try {
                panicNotificationService.send(Message.obtain(null, CLEAR_ALL_ISSUES));
                Toast.makeText(this, "All issues cleared", Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                Log.e(APP_TAG, "Unable to clear all issues", e);
                Toast.makeText(this, "Unable to clear all issues", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void reportIssue() {
        if (panicNotificationService == null) {
            Log.i(APP_TAG, "PanicNotificationService not running. Starting it...");
            issueReportingPending = true;
            initPanicNotificationService();
        } else _reportIssue();
    }

    private void _reportIssue() {
        try {
            panicNotificationService.send(getMessageWithIssues(REPORT_ISSUE, "Fire", "Accident"));
            Toast.makeText(this, "Issue will be reported shortly", Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            Log.e(APP_TAG, "Unable to report issue", e);
            Toast.makeText(this, "Unable to report your issue", Toast.LENGTH_LONG).show();
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        panicNotificationService = new Messenger(iBinder);
        if (issueReportingPending) {
            _reportIssue();
            issueReportingPending = false;
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        panicNotificationService = null;
    }
}
