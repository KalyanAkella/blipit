package com.thoughtworks.blipit.alertFilters;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.GeoLocationToGeoPointConverter;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.contract.GeoLocation;

import java.util.ArrayList;
import java.util.List;

public class DistanceFilter implements IAlertFilter {

    private GeoLocation userLocation;
    private double distanceOfConvinience;
    private DistanceCalculator distanceCalculator;

    public DistanceFilter(GeoLocation userLocation, double distanceOfConvinience) {
        this.userLocation = userLocation;
        this.distanceOfConvinience = distanceOfConvinience;
        distanceCalculator = new DistanceCalculator();
    }

    public void apply(List<Blip> alerts) {
        final ArrayList<Blip> alertsOutsideDistanceOfConvinience = new ArrayList<Blip>();
        for (Blip alert : alerts) {
            final GeoPt alertPt = alert.getGeoPoint();
            final GeoPt userPt = GeoLocationToGeoPointConverter.Convert(userLocation);
            final double distanceFromUser = distanceCalculator.CalculationByDistance(userPt, alertPt);
            if(distanceFromUser > distanceOfConvinience)
                alertsOutsideDistanceOfConvinience.add(alert);
        }
        alerts.removeAll(alertsOutsideDistanceOfConvinience);
    }
}