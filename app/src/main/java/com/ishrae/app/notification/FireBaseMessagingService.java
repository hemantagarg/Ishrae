package com.ishrae.app.notification;
 /*
 * Author       :   Vishal Jain
 * Designation  :   Android Developer
 * E-mail       :   vishal@nssolutions.in
 * Date         :   7/7/16
 * Company      :   Nss Solutions
 * Purpose      :   purpose
 * Description  :   desc.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ishrae.app.R;
import com.ishrae.app.activity.DashboardActivity;

import org.json.JSONObject;

// this class will get called automatically since it is define in manifest file.
public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = FireBaseMessagingService.class.getSimpleName();
    NotificationCompat.Builder notificationBuilder;
    private int notificationID = 0;
    private int pos = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
        JSONObject object = new JSONObject(remoteMessage.getData());
        sendNotification(remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String messageBody) {
        int num = (int) System.currentTimeMillis();
        Intent intent = null;
        intent = new Intent(this, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, num, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder = new NotificationCompat.Builder(this);


        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= 21) {
            notificationBuilder.setVibrate(new long[0]);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(num, notificationBuilder.build());
    }
}
