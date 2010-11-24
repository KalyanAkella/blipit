package com.thoughtworks.blipit.services;

public class BlipItNotifier implements Runnable {
    private BlipNotificationService blipNotificationService;

    public BlipItNotifier(BlipNotificationService blipNotificationService) {
        this.blipNotificationService = blipNotificationService;
    }

    public void run() {
        // 1. Make the webservice call
        // 2. Map the response to a set of GeoPoints
        // 3. Construct a bundle from these GeoPoints
        // 4. Multicast a message with this bundle to all registered clients using MSG_BLIPS_UPDATED
        // 5. Remove any client on RemoteException
    }
}
