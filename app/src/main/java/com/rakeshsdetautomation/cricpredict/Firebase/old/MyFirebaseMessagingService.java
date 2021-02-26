package com.rakeshsdetautomation.cricpredict.Firebase.old;

import com.google.firebase.messaging.FirebaseMessagingService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.rakeshsdetautomation.cricpredict.App;
//import com.google.firebase.quickstart.fcm.R;
import com.rakeshsdetautomation.cricpredict.R;
import com.rakeshsdetautomation.cricpredict.constants.BaseClass;
import com.rakeshsdetautomation.cricpredict.loginandregistration.MainActivity;


import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.ParseException;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 *   <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    //private static final UnicodeSetIterator R = com.rakeshsdetautomation.cricpredict.R;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getNotification().getBody() != null){
            //sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            if(remoteMessage.getNotification().getBody().toLowerCase().contains("prediction")){
                try {
                    sendPredictionNotification();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(remoteMessage.getNotification().getBody().toLowerCase().contains("leadership")){
                try {
                    sendLeadershipNotification();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        System.out.println(intent.getExtras().getString("type"));

        if(intent.getExtras() != null){
            //sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            if(intent.getExtras().getString("type").toLowerCase().contains("prediction")){
                try {
                    sendPredictionNotification();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(intent.getExtras().getString("type").toLowerCase().contains("leadership")){
                System.out.println("inside leadership handle");
                try {
                    sendLeadershipNotification();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        System.out.println("debug---" +  token);
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, App.CHANNEL_PREDICTION_ID)
                        .setSmallIcon(R.drawable.cricket_notification_icon)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(App.CHANNEL_PREDICTION_ID
                    ,
                    "Prediction channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
    * Send prediction notification
     */
    private void sendPredictionNotification() throws ParseException {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        BaseClass.getMatchesStringBaseClass();


        for(int i=0; i< BaseClass.predictionCutOff_notification.size(); i++){

            String firstTeamImageName = BaseClass.team1Name_notification.get(i).toLowerCase().trim().replaceAll(" ","_");
            String secondTeamImageName = BaseClass.team2Name_notification.get(i).toLowerCase().trim().replaceAll(" ","_");

            int resId = getResources().getIdentifier(firstTeamImageName, "drawable", getPackageName());
            Bitmap firstTeamIcon = BitmapFactory.decodeResource(this.getResources(), resId);
            resId = getResources().getIdentifier(secondTeamImageName, "drawable", getPackageName());
            Bitmap secondTeamIcon = BitmapFactory.decodeResource(this.getResources(), resId);

            Bitmap mergedImages = BaseClass.combineTwoBitmapImages(firstTeamIcon, secondTeamIcon);

            Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_PREDICTION_ID)
                    .setSmallIcon(R.drawable.cricket_notification_icon)
                    .setContentTitle(BaseClass.team1Name_notification.get(i) + " vs " + BaseClass.team2Name_notification.get(i))
                    .setContentText(BaseClass.predictionCutOff_notification.get(i))
                    .setLargeIcon(mergedImages)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(mergedImages)
                            .bigLargeIcon(null))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setAutoCancel(true)
                    .setColor(Color.rgb(0, 204, 51))
                    .setLights(Color.RED, 3000, 3000)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setSound(alarmSound)
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(App.CHANNEL_PREDICTION_ID
                        ,
                        "Prediction Channel",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notification);

        }

    }

    /*
    * Send leadership notification
     */

    private void sendLeadershipNotification() throws ParseException {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        BaseClass.getCurrentParticipantRankBaseClass();

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_PREDICTION_ID)
                .setSmallIcon(R.drawable.cricket_notification_icon)
                .setContentTitle(BaseClass.rank_notification)
                .setContentText(BaseClass.pointsCollected_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setColor(Color.rgb(0, 204, 51))
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(App.CHANNEL_LEADERSHIP_ID
                    ,
                    "Leadership Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notification);
    }


}
