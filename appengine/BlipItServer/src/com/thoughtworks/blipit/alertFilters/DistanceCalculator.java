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

package com.thoughtworks.blipit.alertFilters;

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
