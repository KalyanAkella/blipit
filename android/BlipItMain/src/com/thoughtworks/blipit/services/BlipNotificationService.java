package com.thoughtworks.blipit.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.thoughtworks.blipit.R;

import java.util.ArrayList;
import java.util.List;

// TODO: use an IntentService instead, to handle threading stuff
public class BlipNotificationService extends Service {
    private final List<Messenger> clients;
    private final Messenger clientMessenger;
    private GeoPoint currentUserLocation;

    public static final int MSG_REGISTER_CLIENT = 1;
    // TODO: handle unregister in the lifecycle methods of BlipItActivity
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_USER_LOCATION_UPDATED = 3;
    public static final int MSG_BLIPS_UPDATED = 4;

    public BlipNotificationService() {
        clients = new ArrayList<Messenger>();
        clientMessenger = new Messenger(new BlipNotificationServiceHandler(this));
        Thread blipNotifierThread = new Thread(null, new BlipItNotifier(this), "BlipItNotifierThread");
        blipNotifierThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return clientMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, R.string.blip_notification_service_started, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, R.string.blip_notification_service_stopped, Toast.LENGTH_SHORT).show();
    }

    public void addClient(Messenger messenger) {
        this.clients.add(messenger);
    }

    public void removeClient(Messenger messenger) {
        this.clients.remove(messenger);
    }

    public List<Messenger> getClients() {
        return clients;
    }
}
