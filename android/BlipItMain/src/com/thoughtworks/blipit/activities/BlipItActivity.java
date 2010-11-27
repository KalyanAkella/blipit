package com.thoughtworks.blipit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.overlays.BlipItOverlay;
import com.thoughtworks.blipit.services.BlipNotificationService;
import com.thoughtworks.blipit.utils.BlipItServiceHelper;
import com.thoughtworks.blipit.utils.BlipItUtils;
import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResource;
import com.thoughtworks.contract.BlipItResponse;

// TODO: Handle other lifecycle events to correctly interact with BlipItNotificationService
public class BlipItActivity extends MapActivity implements LocationListener {
    private static final String TAG = "BlipItActivity";

    private MapView mapView;
    private BlipNotificationClientHandler blipNotificationClientHandler;
    private Messenger blipItNotificationService;
    private Messenger blipNotificationHandler;
    private BlipItResource blipItResource;
    private GeoPoint userLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initLocationListener();
        initUserLocation(savedInstanceState);
        initMapView();
        initBlipNotifications();
    }

    private void initUserLocation(Bundle savedInstanceState) {
        if (BlipItUtils.containsGeoPoint(savedInstanceState)) {
            userLocation = BlipItUtils.getGeoPointFromBundle(savedInstanceState);
        } else {
            // TODO: Get this from LocationManager
            userLocation = new GeoPoint(12966667, 77566667);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        BlipItUtils.saveGeoPointInBundle(outState, userLocation);
    }

    private void initBlipNotifications() {
        blipNotificationClientHandler = new BlipNotificationClientHandler(this);
        bindService(new Intent(this, BlipNotificationService.class), blipNotificationClientHandler, BIND_AUTO_CREATE);
        blipNotificationHandler = new Messenger(blipNotificationClientHandler);
        blipItResource = new BlipItServiceHelper().getBlipItResource();
    }

    public void setBlipItNotificationService(Messenger blipItNotificationService) {
        this.blipItNotificationService = blipItNotificationService;
    }

    private void initMapView() {
        mapView = (MapView) this.findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        BlipItOverlay blipItOverlay = new BlipItOverlay(drawable, mapView);
        addBlip(blipItOverlay);
        mapView.getOverlays().add(blipItOverlay);
    }

    private void initLocationListener() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void addBlip(BlipItOverlay blipItOverlay) {
        String title = this.getResources().getString(R.string.blip_title);
        OverlayItem blip = new OverlayItem(userLocation, title, getSnippet());
        blipItOverlay.addBlip(blip);
        mapView.getController().animateTo(userLocation);
    }

    private String getSnippet() {
        BlipItRequest blipItRequest = new BlipItRequest();
        blipItRequest.setMessage(this.getResources().getString(R.string.blip_snippet));
        BlipItResponse blipItResponse = new BlipItServiceHelper().getBlipItResource().echo(blipItRequest);
        return blipItResponse.getMessage();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public Messenger getBlipNotificationHandler() {
        return blipNotificationHandler;
    }

    public void updateBlips(Bundle data) {
        Toast.makeText(this, R.string.blip_notification_received, Toast.LENGTH_SHORT).show();
    }

    public void onLocationChanged(Location location) {
        GeoPoint geoPoint = BlipItUtils.asGeoPoint(location);
        mapView.getController().animateTo(geoPoint);
        sendUserLocationUpdate(geoPoint);
    }

    private void sendUserLocationUpdate(GeoPoint geoPoint) {
        try {
            blipItNotificationService.send(BlipItUtils.getMessageWithGeoPoint(geoPoint, BlipItUtils.MSG_USER_LOCATION_UPDATED));
        } catch (RemoteException e) {
            Log.e(TAG, "An error occured while updating user location", e);
        }
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onProviderEnabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onProviderDisabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
