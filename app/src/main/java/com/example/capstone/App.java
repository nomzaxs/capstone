package com.example.capstone;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.capstone.constant.Constants;
import com.example.capstone.helper.ApplicationHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationHelper.initApplicationHelper(this);
        localNotificationSetup(this);
    }


    private static void localNotificationSetup(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.SERVICE_NOTIFICATION_CHANNEL, "서비스 알림", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("앱 작동이 유지되도록합니다. 알림을 끄지 않는 것을 추천합니다.");
            channel.enableLights(false);
            channel.setShowBadge(true);
            NotificationManager manager = application.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.DEFAULT_NOTIFICATION_CHANNEL, "기본 알림", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("어떤 앱이 카메라, 마이크, 위치정보를 사용하는지 보여줍니다.");
            channel.enableLights(true);
            channel.setShowBadge(true);
            NotificationManager manager = application.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
