package com.thoughtworks.blipit.overlays;

import android.graphics.drawable.Drawable;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class BlipItOverlay extends BalloonItemizedOverlay<OverlayItem> {
    private List<OverlayItem> blips = new ArrayList<OverlayItem>();

    public BlipItOverlay(Drawable drawable, MapView mapView) {
        super(boundCenter(drawable), mapView);
    }

    @Override
    protected OverlayItem createItem(int index) {
        return blips.get(index);
    }

    @Override
    public int size() {
        return blips.size();
    }

    public void addBlip(OverlayItem blip) {
        blips.add(blip);
        populate();
    }
}
