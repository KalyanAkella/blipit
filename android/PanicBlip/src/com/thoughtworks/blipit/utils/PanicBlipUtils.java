package com.thoughtworks.blipit.utils;

import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.publish.SaveBlipRequest;

import java.util.ArrayList;
import java.util.Arrays;

public class PanicBlipUtils {
    public static final int REPORT_ISSUE = 0;
    public static final String APP_TAG = "PanicBlipActivity";
    public static final String PANIC_BLIP = "PANIC_BLIP";

    public static GeoLocation getGeoLocation(Location lastKnownLocation) {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(lastKnownLocation.getLatitude());
        geoLocation.setLongitude(lastKnownLocation.getLongitude());
        return geoLocation;
    }

    public static Message getMessageWithIssues(int messageId, String... issues) {
        Message message = Message.obtain(null, messageId);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PANIC_BLIP, asArrayList(issues));
        message.setData(bundle);
        return message;
    }

    public static ArrayList<String> asArrayList(String... strs) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(Arrays.asList(strs));
        return arrayList;
    }

    public static SaveBlipRequest getSaveBlipRequest(Location newLocation, ArrayList<String> issueList) {
        SaveBlipRequest saveBlipRequest = new SaveBlipRequest();
        saveBlipRequest.setUserLocation(getGeoLocation(newLocation));
        saveBlipRequest.setApplicableChannels(issueList);
        return saveBlipRequest;
    }
}
