package com.example.next2me.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.next2me.HomeActivity;
import com.example.next2me.MainActivity;
import com.example.next2me.R;
import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.UserHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class myFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        UserHelper.getInstance().setNotificationsTokenId(token);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            DatabaseHelper.getInstance().getDb().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("NOTIFICATIONS").child("token-id").setValue(token);
        }
        Log.d("notif","new token"+ token.toString());
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Log.d("notif","notifica ricevuta " +remoteMessage.getNotification());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"CHANNEL_ID")
                                                                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                                                                    .setContentTitle(remoteMessage.getNotification().getTitle())
                                                                    .setContentText(remoteMessage.getNotification().getBody())
                                                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                                                                    .setContentIntent(pendingIntent)
                                                                    .setAutoCancel(true);

        NotificationManager myNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId, "Channel title", NotificationManager.IMPORTANCE_HIGH);
            myNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        // notificationId is a unique int for each notification that you must define
        myNotificationManager.notify(100, builder.build());
    }
}
