package com.thoughtworks.blipit.activities;

import android.os.Handler;
import android.os.Message;
import com.thoughtworks.blipit.services.BlipNotificationService;

public class BlipNotificationHandler extends Handler {
    private BlipItActivity blipItActivity;

    public BlipNotificationHandler(BlipItActivity blipItActivity) {
        this.blipItActivity = blipItActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case BlipNotificationService.MSG_BLIPS_UPDATED:
                blipItActivity.updateBlips(msg.getData());
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
