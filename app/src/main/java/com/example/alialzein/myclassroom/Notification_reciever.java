package com.example.alialzein.myclassroom;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Notification_reciever extends BroadcastReceiver{
    int fromCreateClassroom;

    @Override
    public void onReceive(final Context context, Intent intent) {
      //  int fromCreateClassroom = intent.getIntExtra("id",100);
        String className = intent.getStringExtra("class_name");
        String UniqueOfClassroom = intent.getStringExtra("UniqueOfClassroom");


        DatabaseReference localNotificationRef = FirebaseDatabase.getInstance().getReference().child("local_notification");
        localNotificationRef.child(UniqueOfClassroom).child("notification_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 fromCreateClassroom  = ( dataSnapshot.getValue(int.class));
//                Toast.makeText(context, fromCreateClassroom, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, SplashActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, fromCreateClassroom, repeatingIntent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("class reminder")
                .setContentText("you have a "+ className+" class after 15 min")
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        notificationManager.notify(fromCreateClassroom,builder.build());


    }
}
