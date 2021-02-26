package com.rakeshsdetautomation.cricpredict;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends android.app.Application {

    public static final String CHANNEL_PREDICTION_ID = "prediction_reminder_channel";
    public static final String CHANNEL_LEADERSHIP_ID = "leadership_reminder_channel";
    public static final int prediction_id = 1;
    public static final int leadership_id = 2;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel_prediction_reminder = new NotificationChannel(
                    CHANNEL_PREDICTION_ID,
                    "Prediction Reminder Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel_prediction_reminder.setDescription("This is Prediction Reminder Channel");

            NotificationChannel channel_leadership_reminder = new NotificationChannel(
                    CHANNEL_LEADERSHIP_ID,
                    "Leadership Reminder Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel_leadership_reminder.setDescription("This is Leadership Reminder Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel_prediction_reminder);
            manager.createNotificationChannel(channel_leadership_reminder);
        }

    }
}
