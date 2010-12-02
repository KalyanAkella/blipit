package com.thoughtworks.blipit.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.thoughtworks.blipit.R;
import com.thoughtworks.blipit.utils.BlipItUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlipNotificationService extends IntentService {
    private final List<Messenger> clients;
    private final Messenger clientMessenger;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private GeoPoint currentUserLocation;

    public BlipNotificationService() {
        super("BlipNotificationServiceThread");
        clients = new ArrayList<Messenger>();
        clientMessenger = new Messenger(new BlipNotificationServiceHandler(this));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return clientMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        scheduleNotificationService();
        Toast.makeText(this, R.string.blip_notification_service_started, Toast.LENGTH_SHORT).show();
    }

    private void scheduleNotificationService() {
        pendingIntent = PendingIntent.getService(this, 0, new Intent(this, BlipNotificationService.class), 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long firstTime = SystemClock.elapsedRealtime();
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 5 * 1000, pendingIntent);
    }

    @Override
    public void onDestroy() {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, R.string.blip_notification_service_stopped, Toast.LENGTH_SHORT).show();
    }

    public void addClient(Messenger messenger) {
        this.clients.add(messenger);
    }

    public void removeClient(Messenger messenger) {
        this.clients.remove(messenger);
    }

    public synchronized void setCurrentUserLocation(GeoPoint currentUserLocation) {
        this.currentUserLocation = currentUserLocation;
        Toast.makeText(this, R.string.user_location_updated, Toast.LENGTH_SHORT).show();
    }

    public synchronized GeoPoint getCurrentUserLocation() {
        return currentUserLocation;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 1. Make the webservice call
        // 2. Map the response to a set of GeoPoints
        // 3. Construct a bundle from these GeoPoints
        // 4. Multicast a message with this bundle to all registered clients using MSG_BLIPS_UPDATED
        // 5. Remove any client on RemoteException
        for (Iterator<Messenger> iterator = clients.iterator(); iterator.hasNext();) {
            Messenger client = iterator.next();
            Message message = Message.obtain(null, BlipItUtils.MSG_BLIPS_UPDATED);
            try {
                client.send(message);
            } catch (RemoteException e) {
                // The client is dead.  Remove it from the list;
                iterator.remove();
            }
        }
    }
}
