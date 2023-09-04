package com.marchtech.AlarmManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;

@DesignerComponent( version = 1,
                    description = "Extension to help create and set alarms.",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = "images/extension.png")
@SimpleObject(external = true)
public class AlarmManager extends AndroidNonvisibleComponent implements AlarmReceiver {
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String DEFAULT_NOTIFICATION_ID = "default";
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public AlarmManager(ComponentContainer container) {
        super(container.$form());
    }

    private void scheduleNotification(Notification notification, int delay) {
        
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel nChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            assert nManager != null;
            nManager.createNotificationChannel(nChannel);
        }

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        assert nManager != null;
        nManager.notify(id, notification);
    }
}