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

package com.thoughtworks.blipit.panicblip.services;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.thoughtworks.blipit.types.Channel;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils;

import java.util.ArrayList;

public class PanicNotificationServiceHandler extends Handler {
    private PanicNotificationService panicNotificationService;

    public PanicNotificationServiceHandler(PanicNotificationService panicNotificationService, Looper looper) {
        super(looper);
        this.panicNotificationService = panicNotificationService;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case PanicBlipUtils.MSG_REGISTER_CLIENT:
                panicNotificationService.addClient(msg.replyTo);
                break;
            case PanicBlipUtils.REPORT_PANIC:
                Bundle bundle = msg.getData();
                panicNotificationService.reportPanic((ArrayList<Channel>) bundle.getSerializable(PanicBlipUtils.PANIC_DATA));
                break;
            case PanicBlipUtils.CLEAR_PANIC:
                panicNotificationService.clearPanic();
                break;
            case PanicBlipUtils.LOCATION_CHANGED:
                panicNotificationService.onLocationChanged((Intent) msg.obj);
                break;
            case PanicBlipUtils.MSG_UNREGISTER_CLIENT:  // TODO: must be called as the service lives on even after the client activity dies
                panicNotificationService.removeClient(msg.replyTo);
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
