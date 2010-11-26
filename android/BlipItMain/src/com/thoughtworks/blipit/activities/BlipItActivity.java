package com.thoughtworks.blipit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Messenger;
import android.widget.Toast;
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
    private BlipNotificationServiceHandler blipNotificationServiceHandler;
    private Messenger blipItNotificationService;
    private Messenger blipNotificationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initMapView();
        initLocationListener();
        initBlipNotifications();
    }

    private void initBlipNotifications() {
        blipNotificationServiceHandler = new BlipNotificationServiceHandler(this);
        bindService(new Intent(this, BlipNotificationService.class), blipNotificationServiceHandler, BIND_AUTO_CREATE);
        blipNotificationHandler = new Messenger(blipNotificationServiceHandler);
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
        Toast.makeText(this, R.string.blip_notification_received, Toast.LENGTH_SHORT).show();
    }
}
