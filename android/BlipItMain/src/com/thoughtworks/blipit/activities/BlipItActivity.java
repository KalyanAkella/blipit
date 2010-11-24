package com.thoughtworks.blipit.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Messenger;
import android.os.SystemClock;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.overlays.BlipItOverlay;
import com.thoughtworks.blipit.services.BlipNotificationService;
import com.thoughtworks.blipit.utils.HttpHelper;
import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResponse;

import java.util.List;

// TODO: Handle other lifecycle events to correctly interact with BlipItNotificationService
public class BlipItActivity extends MapActivity {

    private MapView mapView;
    private BlipNotificationServiceConnection blipNotificationServiceConnection;
    private Messenger blipItNotificationService;
    private Messenger blipNotificationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initMapView();
        initLocationListener();

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, BlipNotificationService.class), 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long firstTime = SystemClock.elapsedRealtime();
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 30 * 1000, pendingIntent);

        blipNotificationServiceConnection = new BlipNotificationServiceConnection(this);
        bindService(new Intent(this, BlipNotificationService.class), blipNotificationServiceConnection, BIND_AUTO_CREATE);

        blipNotificationHandler = new Messenger(new BlipNotificationHandler(this));
    }

    public void setBlipItNotificationService(Messenger blipItNotificationService) {
        this.blipItNotificationService = blipItNotificationService;
    }

    private void initMapView() {
        mapView = (MapView) this.findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        BlipItOverlay blipItOverlay = new BlipItOverlay(drawable, mapView);
        addBlip(blipItOverlay, mapView.getController());
        List<Overlay> mapOverlays = mapView.getOverlays();
        mapOverlays.add(blipItOverlay);
    }

    private void initLocationListener() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new UserLocationListener(this));
    }

    private void addBlip(BlipItOverlay blipItOverlay, MapController mapController) {
        String title = this.getResources().getString(R.string.blip_title);
        GeoPoint geoPoint = new GeoPoint(12966667, 77566667);
        OverlayItem blip = new OverlayItem(geoPoint, title, getSnippet());
        blipItOverlay.addBlip(blip);
        mapController.animateTo(geoPoint);
    }

    private String getSnippet() {
        String message;
        try {
            BlipItRequest blipItRequest = new BlipItRequest();
            blipItRequest.setMessage(this.getResources().getString(R.string.blip_snippet));
            BlipItResponse blipItResponse = HttpHelper.getResponse(blipItRequest);
            message = blipItResponse.getMessage();
        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public void animateTo(GeoPoint geoPoint) {
        mapView.getController().animateTo(geoPoint);
    }

    public Messenger getBlipNotificationHandler() {
        return blipNotificationHandler;
    }

    public void updateBlips(Bundle data) {
        // TODO: repaint the Maps overlays with the updated blips
    }
}
