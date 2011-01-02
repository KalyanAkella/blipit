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

package com.thoughtworks.blipit.filters;

import com.google.appengine.api.datastore.GeoPt;

public class DistanceCalculator {

    /**
     * Computes the great-circle distance between two geo-locations based on Haversine formula.
     * See http://en.wikipedia.org/wiki/Haversine_formula for more information.
     *
     * @param src the source point
     * @param dest the destination point
     * @return great circle distance in meters between the given points based on Haversine formula
     */
    public double computeDistance1(GeoPt src, GeoPt dest) {
        double lat1 = src.getLatitude();
        double lat2 = dest.getLatitude();
        double lon1 = src.getLongitude();
        double lon2 = dest.getLongitude();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (double) 6371777 * c;
    }

    /**
     * <pre>
     * Computes the distance between two points on the surface of the Earth spheroid. Assumes the Earth to be an
     * oblate spheroid for the computation as specified by WGS84.
     * 
     * Algorithm is an implementation of the iterative methods developed by T. Vincenty. Refer
     * http://en.wikipedia.org/wiki/Vincenty%27s_formulae for the algorithm.
     *</pre>
     *
     * @param src the source point
     * @param dest the destination point
     * @return geographical distance in meters between the points
     * @throws IllegalArgumentException when either <code>src</code> or <code>dest</code> is missing
     */
    public double computeDistance2(GeoPt src, GeoPt dest) {
        if (src == null || dest == null) {
            throw new IllegalArgumentException("Source & Destination parameters are mandatory");
        }
        double lat1 = Math.toRadians(src.getLatitude());
        double lon1 = Math.toRadians(src.getLongitude());
        double lat2 = Math.toRadians(dest.getLatitude());
        double lon2 = Math.toRadians(dest.getLongitude());

        return computeDistance(lat1, lon1, lat2, lon2);
    }

    private static double computeDistance(double lat1, double lon1, double lat2, double lon2) {

        int MAXITERS = 100;

        double a = 6378137.0; // WGS84 major axis
        double b = 6356752.314245; // WGS84 semi-major axis
        double f = 1/298.257223563; // WGS84 flattening of the ellipsoid
        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

        double L = lon2 - lon1;
        double A = 0.0;
        double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
        double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;

        double sigma = 0.0;
        double deltaSigma = 0.0;
        double cosSqAlpha = 0.0;
        double cos2SM = 0.0;
        double cosSigma = 0.0;
        double sinSigma = 0.0;
        double cosLambda = 0.0;
        double sinLambda = 0.0;

        double lambda = L; // initial guess
        for (int iter = 0; iter < MAXITERS; iter++) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
            sinSigma = Math.sqrt(sinSqSigma);
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
            sigma = Math.atan2(sinSigma, cosSigma); // (16)
            double sinAlpha = (sinSigma == 0) ? 0.0 :
                    cosU1cosU2 * sinLambda / sinSigma; // (17)
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SM = (cosSqAlpha == 0) ? 0.0 :
                    cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)

            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
            A = 1 + (uSquared / 16384.0) * // (3)
                    (4096.0 + uSquared *
                            (-768 + uSquared * (320.0 - 175.0 * uSquared)));
            double B = (uSquared / 1024.0) * // (4)
                    (256.0 + uSquared *
                            (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
            double C = (f / 16.0) *
                    cosSqAlpha *
                    (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B * sinSigma * // (6)
                    (cos2SM + (B / 4.0) *
                            (cosSigma * (-1.0 + 2.0 * cos2SMSq) -
                                    (B / 6.0) * cos2SM *
                                            (-3.0 + 4.0 * sinSigma * sinSigma) *
                                            (-3.0 + 4.0 * cos2SMSq)));

            lambda = L +
                    (1.0 - C) * f * sinAlpha *
                            (sigma + C * sinSigma *
                                    (cos2SM + C * cosSigma *
                                            (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.abs(delta) < 1.0e-12) {
                break;
            }
        }

        return (b * A * (sigma - deltaSigma));
    }
}
