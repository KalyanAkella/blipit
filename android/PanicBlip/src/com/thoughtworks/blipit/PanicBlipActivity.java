package com.thoughtworks.blipit;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.thoughtworks.blipit.utils.PanicBlipServiceHelper;
import com.thoughtworks.blipit.utils.PanicBlipUtils;
import com.thoughtworks.contract.publish.BlipItPublishResource;
import com.thoughtworks.contract.publish.SaveBlipRequest;
import com.thoughtworks.contract.publish.SaveBlipResponse;

import java.util.Arrays;

public class PanicBlipActivity extends Activity implements View.OnClickListener {

    // TODO: Read this url from settings.xml either through prefs or resource.getString API
    private static final String BLIPIT_SERVICE_URL = "http://10.0.2.2:8080/blipit/subscribe";
    private static final String APP_TAG = "PanicBlipActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button reportIssueBtn = (Button) findViewById(R.id.report_issue_btn);
        reportIssueBtn.setOnClickListener(this);
    }

    public void onClick(View view) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = getLastKnownLocation(locationManager);
        if (lastKnownLocation == null)
            Toast.makeText(this, "Unable to obtain your current GPS location", Toast.LENGTH_LONG).show();
        else
            reportIssue(lastKnownLocation);
    }

    private void reportIssue(Location lastKnownLocation) {
        try {
            BlipItPublishResource publishResource = PanicBlipServiceHelper.getPublishResource(BLIPIT_SERVICE_URL);
            SaveBlipRequest saveBlipRequest = new SaveBlipRequest();
            saveBlipRequest.setUserLocation(PanicBlipUtils.getGeoLocation(lastKnownLocation));
            saveBlipRequest.setApplicableChannels(Arrays.asList("fire", "accident"));
            SaveBlipResponse saveBlipResponse = publishResource.saveBlip(saveBlipRequest);
            if (saveBlipResponse.isFailure()) {
                showAndLogError(saveBlipResponse.getBlipItError().getMessage(), null);
            } else {
                Toast.makeText(this, "Issue reported successfully", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            showAndLogError("Error occurred while saving alert", e);
        }
    }

    private void showAndLogError(String message, Throwable o) {
        Log.e(APP_TAG, message, o);
        Toast.makeText(this, "Unable to report your issue", Toast.LENGTH_LONG).show();
    }

    private Location getLastKnownLocation(LocationManager locationManager) {
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation == null)
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation == null)
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return lastKnownLocation;
    }
}
