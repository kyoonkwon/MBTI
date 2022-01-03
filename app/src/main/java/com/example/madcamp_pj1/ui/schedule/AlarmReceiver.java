package com.example.madcamp_pj1.ui.schedule;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.madcamp_pj1.MainActivity;
import com.example.madcamp_pj1.R;

import java.io.BufferedInputStream;
import java.io.IOException;

public class AlarmReceiver extends BroadcastReceiver {

    NotificationManager manager;
    NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent2 = new Intent(context, MainActivity.class);
        String title = intent.getExtras().getString("title");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, title.hashCode(), intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("ALARM", title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(title) == null) {
                manager.createNotificationChannel(
                        new NotificationChannel(title, title, NotificationManager.IMPORTANCE_DEFAULT)
                );
            }
            builder = new NotificationCompat.Builder(context, title);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        AssetManager am = context.getAssets();
        BufferedInputStream buf = null;
        try {
            buf = new BufferedInputStream(am.open("logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(buf);

        builder.setContentTitle(title)
                .setSmallIcon(R.drawable.ic_baseline_chat_bubble_outline_24)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Log.e("ALARM", title);
        Notification notification = builder.build();
        manager.notify(title.hashCode(), notification);
    }
}