package com.oakspro.flipshop;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.oakspro.flipshop.ui.orders.OrdersFragment;

import static com.oakspro.flipshop.AppService.CHANNEL_ID;

public class ServiceController extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String email=intent.getStringExtra("input");

        Intent notificationIntent=new Intent(this, new OrdersFragment().getClass());
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification builder=new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Orders Status")
                .setContentText(email)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, builder);
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
