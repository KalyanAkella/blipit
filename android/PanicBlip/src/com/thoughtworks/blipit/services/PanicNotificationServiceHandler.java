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
        }
    }
}
