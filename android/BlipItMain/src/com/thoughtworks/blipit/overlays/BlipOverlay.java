package com.thoughtworks.blipit.overlays;

import android.graphics.drawable.Drawable;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.thoughtworks.blipit.utils.BlipItUtils;
import com.thoughtworks.contract.Blip;

import java.util.ArrayList;
import java.util.List;

public class BlipOverlay extends BalloonItemizedOverlay<OverlayItem> {
    private List<OverlayItem> blips = new ArrayList<OverlayItem>();

    public BlipOverlay(Drawable drawable, MapView mapView) {
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

    public void addBlips(List<Blip> blips) {
        if (blips == null) return;
        this.blips.clear();
        for (Blip blip : blips) {
            addBlip(BlipItUtils.getOverlayItem(blip));
        }
        mapView.invalidate();
    }
}
