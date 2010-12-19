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

package com.thoughtworks.blipit.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.thoughtworks.blipit.utils.PanicBlipUtils;

import java.util.ArrayList;

public class PanicNotificationServiceHandler extends Handler {
    private PanicNotificationService panicNotificationService;

    public PanicNotificationServiceHandler(PanicNotificationService panicNotificationService) {
        this.panicNotificationService = panicNotificationService;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == PanicBlipUtils.REPORT_ISSUE) {
            Bundle bundle = msg.getData();
            ArrayList<String> issues = bundle.getStringArrayList(PanicBlipUtils.PANIC_BLIP);
            panicNotificationService.reportAndRegisterPanic(issues);
        } else if (msg.what == PanicBlipUtils.CLEAR_ALL_ISSUES) {
            panicNotificationService.clearAllIssues();
        }
    }
}
