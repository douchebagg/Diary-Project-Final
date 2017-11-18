package com.example.douchebag.da_project_android.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.douchebag.da_project_android.R;
import com.example.douchebag.da_project_android.activity.SplashScreenActivity;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("ACTION") != null && intent.getStringExtra("ACTION").equals("NOTIFICATION")){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.m_add))
                    .setContentTitle("Hey, you forget writing a diary?")
                    .setContentText("Click here if you want to write a diary.")
                    .setOngoing(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Intent myIntent = new Intent(context, SplashScreenActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_ONE_SHOT);

            builder.setLights(0xFFb71c1c, 1000, 2000);
            builder.setContentIntent(pendingIntent);
            notificationManager.notify(12345, builder.build());
        }
    }
}
