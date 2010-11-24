package com.thoughtworks.blipit.services;

import android.os.Handler;
import android.os.Message;

public class BlipNotificationServiceHandler extends Handler {
    private BlipNotificationService blipNotificationService;

    public BlipNotificationServiceHandler(BlipNotificationService blipNotificationService) {
        this.blipNotificationService = blipNotificationService;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case BlipNotificationService.MSG_REGISTER_CLIENT:
                blipNotificationService.addClient(msg.replyTo);
                break;
            case BlipNotificationService.MSG_UNREGISTER_CLIENT:
                blipNotificationService.removeClient(msg.replyTo);
                break;
            case BlipNotificationService.MSG_USER_LOCATION_UPDATED:
                // update current user location here
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
