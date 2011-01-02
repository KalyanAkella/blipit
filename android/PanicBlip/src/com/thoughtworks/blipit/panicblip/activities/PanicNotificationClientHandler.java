package com.thoughtworks.blipit.panicblip.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils;

public class PanicNotificationClientHandler extends Handler implements ServiceConnection {
    private PanicBlipActivity panicBlipActivity;

    public PanicNotificationClientHandler(PanicBlipActivity panicBlipActivity) {
        this.panicBlipActivity = panicBlipActivity;
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        Messenger panicNotificationService = new Messenger(service);
        try {
            Message message = Message.obtain(null, PanicBlipUtils.MSG_REGISTER_CLIENT);
            message.replyTo = panicBlipActivity.getPanicNotificationClientMessenger();
            panicNotificationService.send(message);
            panicBlipActivity.setPanicNotificationService(panicNotificationService);
            panicBlipActivity.reportPendingPanics();
        } catch (RemoteException e) {
            // In this case the service has crashed before we could even
            // do anything with it; we can count on soon being
            // disconnected (and then reconnected if it can be restarted)
            // so there is no need to do anything here.
            panicBlipActivity.setPanicNotificationService(null);
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        // This is called when the connection with the service has been
        // unexpectedly disconnected -- that is, its process crashed.
        panicBlipActivity.setPanicNotificationService(null);
    }

    @Override
    public void handleMessage(Message msg) {
        final String errorMessage = (String) msg.getData().getSerializable(PanicBlipUtils.PANIC_DATA);
        switch (msg.what) {
            case PanicBlipUtils.CLEAR_PANIC_SUCCESS:
                panicBlipActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        panicBlipActivity.clearPanicSuccess();
                    }
                });
                break;
            case PanicBlipUtils.CLEAR_PANIC_FAILURE:
                panicBlipActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        panicBlipActivity.clearPanicFailure(errorMessage);
                    }
                });
                break;
            case PanicBlipUtils.REPORT_PANIC_SUCCESS:
                panicBlipActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        panicBlipActivity.reportPanicSuccess();
                    }
                });
                break;
            case PanicBlipUtils.REPORT_PANIC_LOC_ERROR:
                panicBlipActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        panicBlipActivity.reportPanicLocUnavailable();
                    }
                });
                break;
            case PanicBlipUtils.REPORT_PANIC_FAILURE:
                panicBlipActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        panicBlipActivity.reportPanicFailure(errorMessage);
                    }
                });
                break;
        }
    }
}
