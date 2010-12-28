package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.GeoPt;

public class DistanceCalculator {

   private double radius = 6371777;

   // R = earth's radius (mean radius = 6,371km)

   public double CalculationByDistance(GeoPt point1, GeoPt point2) {
      double lat1 = point1.getLatitude();
      double lat2 = point2.getLatitude();
      double lon1 = point1.getLongitude();
      double lon2 = point2.getLongitude();
      double dLat = Math.toRadians(lat2-lat1);
      double dLon = Math.toRadians(lon2-lon1);
      double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
         Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
         Math.sin(dLon/2) * Math.sin(dLon/2);
      double c = 2 * Math.asin(Math.sqrt(a));
      return radius * c;
   }
}
