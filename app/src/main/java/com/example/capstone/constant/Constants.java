package com.example.capstone.constant;

import com.example.capstone.BuildConfig;

public class Constants {
    public static final String SHARED_PREFERENCE_NAME = "com.example.capstone.PREFERENCES";
    public static final String DEFAULT_NOTIFICATION_CHANNEL = "APP_NOTIFICATION";
    public static final String SERVICE_NOTIFICATION_CHANNEL = "APP_SERVICE_NOTIFICATION";
    public static final int NOTIFICATION_ID = 256;

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}