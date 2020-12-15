package com.oakspro.flipshop;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AppService extends Application {

    public static final String CHANNEL_ID="com.oakspro.flipshop";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel servicechannel=new NotificationChannel(CHANNEL_ID, "Flishop Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(servicechannel);
        }
    }
}
