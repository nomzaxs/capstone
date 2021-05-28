package com.example.capstone;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.capstone.constant.Constants;
import com.example.capstone.helper.ApplicationHelper;
import com.google.firebase.auth.FirebaseAuth;

public class App extends Application {
    private FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationHelper.initApplicationHelper(this);
        mAuth = FirebaseAuth.getInstance();
        localNotificationSetup(this);
    }


    private static void localNotificationSetup(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.SERVICE_NOTIFICATION_CHANNEL, "Service Notification", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("This keeps the app alive. We recommend not to disable this notification.");
            channel.enableLights(false);
            channel.setShowBadge(true);
            NotificationManager manager = application.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.DEFAULT_NOTIFICATION_CHANNEL, "Default Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This shows what app is using the camera or mic or location");
            channel.enableLights(true);
            channel.setShowBadge(true);
            NotificationManager manager = application.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
