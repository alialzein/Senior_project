package com.example.alialzein.myclassroom;

import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ALiAlzein on 3/30/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String Notif_title = remoteMessage.getNotification().getTitle();
        String Notif_body = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.default_profile_img)
                .setContentTitle(Notif_title)
                .setContentText(Notif_body)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
