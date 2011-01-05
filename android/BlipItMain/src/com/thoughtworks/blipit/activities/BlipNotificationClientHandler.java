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

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.thoughtworks.blipit.utils.BlipItUtils;

public class BlipNotificationClientHandler extends Handler implements ServiceConnection {
    private BlipItActivity blipItActivity;

    public BlipNotificationClientHandler(BlipItActivity blipItActivity) {
        this.blipItActivity = blipItActivity;
    }

    public void onServiceConnected(ComponentName componentName, IBinder service) {
        Messenger blipItNotificationService = new Messenger(service);
        try {
            Message message = Message.obtain(null, BlipItUtils.MSG_REGISTER_CLIENT);
            message.replyTo = blipItActivity.getBlipNotificationClientMessenger();
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
            case BlipItUtils.MSG_BLIPS_UPDATED:
                blipItActivity.updateAds(msg.getData());
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
