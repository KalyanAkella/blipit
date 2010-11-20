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
import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class BlipItActivity extends MapActivity {

    private LocationManager locationManager;
    private MapView mapView;
    private OverlayItem blip;
    private static final String BLIPIT_SERVICE_URI = "http://localhost:8080/blipit";


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
        String snippet;
        try {
            snippet = getSnippet();
        } catch (IOException e) {
            snippet = "Error from server - IOE";
        } catch (ClassNotFoundException e) {
            snippet = "Error from server - CNFE";
        }
        GeoPoint geoPoint = new GeoPoint(12966667, 77566667);
        blip = new OverlayItem(geoPoint, title, snippet);
        blipItOverlay.addBlip(blip);
        mapController.animateTo(geoPoint);
    }

    private String getSnippet() throws IOException, ClassNotFoundException {
        HttpClient httpClient = new DefaultHttpClient();

        BlipItRequest blipItRequest = new BlipItRequest();
        blipItRequest.setMessage(this.getResources().getString(R.string.blip_snippet));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(blipItRequest);

        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(byteArrayOutputStream.toByteArray());
        byteArrayEntity.setContentType("binary/octet-stream");
        byteArrayEntity.setChunked(true);

        HttpPost httpPost = new HttpPost(BLIPIT_SERVICE_URI);
        httpPost.setEntity(byteArrayEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inputStream = httpEntity.getContent();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        BlipItResponse blipItResponse = (BlipItResponse) objectInputStream.readObject();
        httpEntity.consumeContent();
        httpClient.getConnectionManager().shutdown();
        return blipItResponse.getMessage();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    LocationListener locationListener = new LocationListener() {

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
