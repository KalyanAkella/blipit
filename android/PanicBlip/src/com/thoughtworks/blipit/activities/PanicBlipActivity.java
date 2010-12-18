package com.thoughtworks.blipit.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.services.PanicNotificationService;
import com.thoughtworks.blipit.utils.PanicBlipUtils;

import static com.thoughtworks.blipit.utils.PanicBlipUtils.getMessageWithIssues;

public class PanicBlipActivity extends Activity implements View.OnClickListener, ServiceConnection {
    private Messenger panicNotificationService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initPanicNotificationService();
        initPanicButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    private void initPanicButton() {
        findViewById(R.id.report_issue_btn).setOnClickListener(this);
    }

    private void initPanicNotificationService() {
        bindService(new Intent(this, PanicNotificationService.class), this, BIND_AUTO_CREATE);
    }

    public void onClick(View view) {
        if (panicNotificationService == null)
            Toast.makeText(this, "Unable to report issue", Toast.LENGTH_LONG).show();
        else {
            try {
                panicNotificationService.send(getMessageWithIssues(PanicBlipUtils.REPORT_ISSUE, "Fire", "Accident"));
                Toast.makeText(this, "Issue will be reported shortly", Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                Log.e(PanicBlipUtils.APP_TAG, "Unable to report issue", e);
                Toast.makeText(this, "Unable to report your issue", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        panicNotificationService = new Messenger(iBinder);
    }

    public void onServiceDisconnected(ComponentName componentName) {
        panicNotificationService = null;
    }
}
