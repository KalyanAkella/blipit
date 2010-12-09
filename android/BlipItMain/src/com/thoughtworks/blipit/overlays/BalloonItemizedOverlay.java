/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package com.thoughtworks.blipit.overlays;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.views.BalloonOverlayView;

import java.lang.reflect.Method;

/**
 * An abstract extension of ItemizedOverlay for displaying an information balloon
 * upon screen-tap of each marker overlay.
 *
 * @author Jeff Gilfelt
 */
public abstract class BalloonItemizedOverlay<Item> extends ItemizedOverlay<OverlayItem> {
    protected MapView mapView;
    private BalloonOverlayView balloonView;
    private View clickRegion;
    private int viewOffset;
    protected MapController mapController;

    /**
     * Create a new BalloonItemizedOverlay
     *
     * @param defaultMarker - A bounded Drawable to be drawn on the map for each item in the overlay.
     * @param mapView - The view upon which the overlay items are to be drawn.
     */
    public BalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
        super(defaultMarker);
        this.mapView = mapView;
        viewOffset = 0;
        mapController = mapView.getController();
    }

    /**
     * Set the horizontal distance between the marker and the bottom of the information
     * balloon. The default is 0 which works well for center bounded markers. If your
     * marker is center-bottom bounded, call this before adding overlay items to ensure
     * the balloon hovers exactly above the marker.
     *
     * @param pixels - The padding between the center point and the bottom of the
     * information balloon.
     */
    public void setBalloonBottomOffset(int pixels) {
        viewOffset = pixels;
    }

    /**
     * Override this method to handle a "tap" on a balloon. By default, does nothing
     * and returns false.
     *
     * @param index - The index of the item whose balloon is tapped.
     * @return true if you handled the tap, otherwise false.
     */
    protected boolean onBalloonTap(int index) {
        return false;
    }

    /* (non-Javadoc)
     * @see com.google.android.maps.ItemizedOverlay#onTap(int)
     */
    @Override
    protected final boolean onTap(int index) {

        boolean isRecycled;
        final int thisIndex;
        GeoPoint point;

        thisIndex = index;
        OverlayItem overlayItem = createItem(index);
        point = overlayItem.getPoint();

        if (balloonView == null) {
            balloonView = new BalloonOverlayView(mapView.getContext(), viewOffset);
            clickRegion = (View) balloonView.findViewById(R.id.balloon_inner_layout);
            isRecycled = false;
        } else {
            isRecycled = true;
        }

        balloonView.setVisibility(View.GONE);

        hideOtherBalloons();

        balloonView.setData(overlayItem.getTitle(), overlayItem.getSnippet());

        MapView.LayoutParams params = new MapView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, point,
                MapView.LayoutParams.BOTTOM_CENTER);
        params.mode = MapView.LayoutParams.MODE_MAP;

        setBalloonTouchListener(thisIndex);

        balloonView.setVisibility(View.VISIBLE);

        if (isRecycled) {
            balloonView.setLayoutParams(params);
        } else {
            mapView.addView(balloonView, params);
        }

        mapController.animateTo(point);

        return true;
    }

    /**
     * Sets the visibility of this overlay's balloon view to GONE.
     */
    private void hideBalloon() {
        if (balloonView != null) {
            balloonView.setVisibility(View.GONE);
        }
    }

    /**
     * Hides the balloon view for any other BalloonItemizedOverlay instances
     * that might be present on the MapView.
     *
     */
    private void hideOtherBalloons() {

        for (Overlay overlay : mapView.getOverlays()) {
            if (overlay instanceof BalloonItemizedOverlay<?> && overlay != this) {
                ((BalloonItemizedOverlay<?>) overlay).hideBalloon();
            }
        }

    }

    /**
     * Sets the onTouchListener for the balloon being displayed, calling the
     * overridden onBalloonTap if implemented.
     *
     * @param thisIndex - The index of the item whose balloon is tapped.
     */
    private void setBalloonTouchListener(final int thisIndex) {

        try {
            @SuppressWarnings("unused")
            Method m = this.getClass().getDeclaredMethod("onBalloonTap", int.class);

            clickRegion.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {

                    View l =  ((View) v.getParent()).findViewById(R.id.balloon_main_layout);
                    Drawable d = l.getBackground();

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        int[] states = {android.R.attr.state_pressed};
                        if (d.setState(states)) {
                            d.invalidateSelf();
                        }
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        int newStates[] = {};
                        if (d.setState(newStates)) {
                            d.invalidateSelf();
                        }
                        // call overridden method
                        onBalloonTap(thisIndex);
                        return true;
                    } else {
                        return false;
                    }

                }
            });

        } catch (SecurityException e) {
            Log.e("BalloonItemizedOverlay", "setBalloonTouchListener reflection SecurityException");
            return;
        } catch (NoSuchMethodException e) {
            // method not overridden - do nothing
            return;
        }

    }
}
