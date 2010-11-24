package com.thoughtworks.blipit.activities;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.thoughtworks.blipit.overlays.BlipItOverlay;

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

    public BlipItActivityTest() {
        super("com.thoughtworks.blipit.activities", BlipItActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        blipItActivity = getActivity();
        mapView = (MapView) blipItActivity.findViewById(com.thoughtworks.blipit.R.id.mapview);
        mapController = mapView.getController();
        expectedTitle = blipItActivity.getResources().getString(com.thoughtworks.blipit.R.string.blip_title);
        expectedSnippet = blipItActivity.getResources().getString(com.thoughtworks.blipit.R.string.blip_snippet);
    }

    public void testPreConditions() {
        assertNotNull(mapView);
        assertNotNull(mapController);
        OverlayItem overlayItem = getOverlayItem(0, 0);
        assertEquals(expectedTitle, overlayItem.getTitle());
        assertEquals(expectedSnippet, overlayItem.getSnippet());
    }

    private OverlayItem getOverlayItem(int overlayIndex, int overlayItemIndex) {
        Overlay overlay = getOverlay(overlayIndex);
        assertTrue(overlay instanceof BlipItOverlay);
        return getOverlayItem(overlay, overlayItemIndex);
    }

    private OverlayItem getOverlayItem(Overlay overlay, int itemIndex) {
        BlipItOverlay blipItOverlay = (BlipItOverlay) overlay;
        assertEquals(1, blipItOverlay.size());
        return blipItOverlay.getItem(itemIndex);
    }

    private Overlay getOverlay(int overlayIndex) {
        List<Overlay> overlays = mapView.getOverlays();
        assertEquals(1, overlays.size());
        return overlays.get(overlayIndex);
    }

    public void testDisplayTextOnClickOfBlip() throws InterruptedException {
        mapController.setCenter(new GeoPoint(35410000, 139460000)); // Set this to Japan initially
        OverlayItem overlayItem = getOverlayItem(0, 0);
        mapController.setCenter(overlayItem.getPoint());
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
}
