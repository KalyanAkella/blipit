package com.thoughtworks.blipit.activities;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.TextView;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.thoughtworks.blipit.overlays.BlipOverlay;

import java.util.List;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.thoughtworks.blipit.activities.BlipItActivityTest \
 * com.thoughtworks.blipit.tests/android.test.InstrumentationTestRunner
 */
public class BlipItActivityTest extends ActivityInstrumentationTestCase2<BlipItActivity> {
    private BlipItActivity blipItActivity;
    private MapView mapView;
    private MapController mapController;
    private String expectedTitle;
    private String expectedSnippet;
    private LocationManager locationManager;
    private static final String BLIPIT_TEST_LOC_PROVIDER = "BLIPIT_TEST_LOC_PROVIDER";

    public BlipItActivityTest() {
        super("com.thoughtworks.blipit.activities", BlipItActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        blipItActivity = getActivity();
        initTestLocationProvider(12.966667, 77.566667);
        mapView = (MapView) blipItActivity.findViewById(com.thoughtworks.blipit.R.id.mapview);
        mapController = mapView.getController();
        expectedTitle = blipItActivity.getString(com.thoughtworks.blipit.R.string.blip_title);
        expectedSnippet = blipItActivity.getString(com.thoughtworks.blipit.R.string.blip_snippet);
    }

    @Override
    protected void tearDown() throws Exception {
        removeTestLocationProvider();
        super.tearDown();
    }

    public void testPreConditions() {
        assertNotNull(mapView);
        assertNotNull(mapController);
    }

    private OverlayItem getOverlayItem(int overlayIndex, int overlayItemIndex) {
        Overlay overlay = getOverlay(overlayIndex);
        assertTrue(overlay instanceof BlipOverlay);
        return getOverlayItem(overlay, overlayItemIndex);
    }

    private OverlayItem getOverlayItem(Overlay overlay, int itemIndex) {
        BlipOverlay blipItOverlay = (BlipOverlay) overlay;
        assertEquals(1, blipItOverlay.size());
        return blipItOverlay.getItem(itemIndex);
    }

    private Overlay getOverlay(int overlayIndex) {
        List<Overlay> overlays = mapView.getOverlays();
        assertEquals(1, overlays.size());
        return overlays.get(overlayIndex);
    }

    public void testDisplayTextOnClickOfLocationBlip() {
//        mapController.setCenter(new GeoPoint(35410000, 139460000)); // Set this to Japan initially
//        OverlayItem overlayItem = getOverlayItem(0, 0);
//        mapController.setCenter(overlayItem.getPoint());
        TouchUtils.clickView(this, mapView);
        View titleView = blipItActivity.findViewById(com.thoughtworks.blipit.R.id.balloon_item_title);
        assertNotNull(titleView);
        assertTrue(titleView instanceof TextView);
        TextView titleTextView = (TextView) titleView;
        assertEquals(expectedTitle, titleTextView.getText());
        View snippetView = blipItActivity.findViewById(com.thoughtworks.blipit.R.id.balloon_item_snippet);
        assertNotNull(snippetView);
        assertTrue(snippetView instanceof TextView);
        TextView titleSnippetView = (TextView) snippetView;
        assertEquals(expectedSnippet, titleSnippetView.getText());
    }

    private void initTestLocationProvider(double latitude, double longitude) {
        locationManager = (LocationManager) blipItActivity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(BLIPIT_TEST_LOC_PROVIDER, false, false, false, false, false, false, false, Criteria.NO_REQUIREMENT, Criteria.ACCURACY_FINE);
        locationManager.setTestProviderEnabled(BLIPIT_TEST_LOC_PROVIDER, true);
        setTestLocation(latitude, longitude);
    }

    private void removeTestLocationProvider() {
        locationManager.setTestProviderEnabled(BLIPIT_TEST_LOC_PROVIDER, false);
        locationManager.removeTestProvider(BLIPIT_TEST_LOC_PROVIDER);
    }

    private void setTestLocation(double latitude, double longitude) {
        Location location = new Location(BLIPIT_TEST_LOC_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(System.currentTimeMillis());
        locationManager.setTestProviderLocation(BLIPIT_TEST_LOC_PROVIDER, location);
    }
}
