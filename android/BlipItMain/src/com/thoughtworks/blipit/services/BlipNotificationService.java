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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlipNotificationService extends IntentService {
    private final List<Messenger> clients;
    private final Messenger clientMessenger;
    private GeoPoint currentUserLocation;

    public static final int MSG_REGISTER_CLIENT = 1;
    // TODO: handle unregister in the lifecycle methods of BlipItActivity
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_USER_LOCATION_UPDATED = 3;
    public static final int MSG_BLIPS_UPDATED = 4;

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
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, BlipNotificationService.class), 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long firstTime = SystemClock.elapsedRealtime();
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 5 * 1000, pendingIntent);
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

    @Override
    protected void onHandleIntent(Intent intent) {
        // 1. Make the webservice call
        // 2. Map the response to a set of GeoPoints
        // 3. Construct a bundle from these GeoPoints
        // 4. Multicast a message with this bundle to all registered clients using MSG_BLIPS_UPDATED
        // 5. Remove any client on RemoteException
        for (Iterator<Messenger> iterator = clients.iterator(); iterator.hasNext();) {
            Messenger client = iterator.next();
            Message message = Message.obtain(null, BlipNotificationService.MSG_BLIPS_UPDATED);
            try {
                client.send(message);
            } catch (RemoteException e) {
                // The client is dead.  Remove it from the list;
                iterator.remove();
            }
        }
    }
}
