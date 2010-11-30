package com.thoughtworks.blipit.activities;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.TextView;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

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
        expectedTitle = blipItActivity.getString(com.thoughtworks.blipit.R.string.blip_title);
        expectedSnippet = blipItActivity.getString(com.thoughtworks.blipit.R.string.blip_snippet);
    }

    public void testPreConditions() {
        assertNotNull(mapView);
        assertNotNull(mapController);
    }

    public void testDisplayTextOnClickOfLocationBlip() {
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
