package com.thoughtworks.blipit.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.thoughtworks.blipit.services.BlipNotificationService;

public class BlipNotificationServiceHandler extends Handler implements ServiceConnection {
    private BlipItActivity blipItActivity;

    public BlipNotificationServiceHandler(BlipItActivity blipItActivity) {
        this.blipItActivity = blipItActivity;
    }

    public void onServiceConnected(ComponentName componentName, IBinder service) {
        Messenger blipItNotificationService = new Messenger(service);
        try {
            Message message = Message.obtain(null, BlipNotificationService.MSG_REGISTER_CLIENT);
            message.replyTo = blipItActivity.getBlipNotificationHandler();
            blipItNotificationService.send(message);
            blipItActivity.setBlipItNotificationService(blipItNotificationService);
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even
            // do anything with it; we can count on soon being
            // disconnected (and then reconnected if it can be restarted)
            // so there is no need to do anything here.
            blipItActivity.setBlipItNotificationService(null);
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        // This is called when the connection with the service has been
        // unexpectedly disconnected -- that is, its process crashed.
        blipItActivity.setBlipItNotificationService(null);
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
