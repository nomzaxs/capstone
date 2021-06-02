package com.example.capstone.manager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.capstone.constant.Constants;

public class PreferenceManager {
    private static PreferenceManager instance;
    private Application application;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;

    private class PREF_CONSTANTS {
        public static final String SERVICE_KEY = "me.example.capstone.SERVICE";
        public static final String ICON_KEY = "me.example.capstone.ICON";

        public static final String CAMERA_KEY = "me.example.capstone.CAMERA";
        public static final String MIC_KEY = "me.example.capstone.MICROPHONE";
        public static final String LOCATION_KEY = "me.example.capstone.LOCATION";
    }

    public static PreferenceManager getInstance(Application application) {
        if (instance == null) {
            instance = new PreferenceManager(application);
        }
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    public PreferenceManager(Application application) {
        this.application = application;
        this.sharedPreferences = application.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.prefEditor = sharedPreferences.edit();
    }

    public boolean isServiceEnabled() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.SERVICE_KEY, false);
    }

    public void setServiceEnabled(boolean set) {
        prefEditor.putBoolean(PREF_CONSTANTS.SERVICE_KEY, set).apply();
    }

    // 마이크
    public boolean isMicEnabled() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.MIC_KEY, true);
    }

    // 카메라
    public boolean isCameraEnabled() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.CAMERA_KEY, true);
    }

    // 위치정보
    public boolean isLocationEnabled() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.LOCATION_KEY, false);
    }

    public void setLocationEnabled(boolean set) {
        prefEditor.putBoolean(PREF_CONSTANTS.LOCATION_KEY, set).apply();
    }

    // 인디케이터
    public boolean isIconsEnabled() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.ICON_KEY, true);
    }
}