package com.thoughtworks.blipit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.thoughtworks.blipit.overlays.BlipItOverlay;

import java.util.List;

public class BlipItActivity extends MapActivity {
    
    private LocationManager locationManager;
    private MapView mapView;
    private OverlayItem blip;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) this.findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        BlipItOverlay blipItOverlay = new BlipItOverlay(drawable, mapView);
        addBlip(blipItOverlay, mapView.getController());
        List<Overlay> mapOverlays = mapView.getOverlays();
        mapOverlays.add(blipItOverlay);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void addBlip(BlipItOverlay blipItOverlay, MapController mapController) {
        String title = this.getResources().getString(R.string.blip_title);
        String snippet = this.getResources().getString(R.string.blip_snippet);
        GeoPoint geoPoint = new GeoPoint(19240000, -99120000);
        blip = new OverlayItem(geoPoint, title, snippet);
        blipItOverlay.addBlip(blip);
        mapController.animateTo(geoPoint);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    LocationListener locationListener = new LocationListener(){

        public void onLocationChanged(Location location) {
            int latitude = (int) (location.getLatitude() * 1E6);
            int longitude = (int) (location.getLongitude() * 1E6);
            mapView.getController().animateTo(new GeoPoint(latitude, longitude));
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
    };
}
