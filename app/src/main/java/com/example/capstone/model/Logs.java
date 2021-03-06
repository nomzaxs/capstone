package com.example.capstone.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "logs_database")
public class Logs implements Serializable {

    @PrimaryKey
    @NonNull
    private long timestamp;
    private String packageName;
    private int camera_state;
    private int mic_state;
    private int loc_state;

    public Logs(long timestamp, String packageName, int camera_state, int mic_state, int loc_state) {
        this.timestamp = timestamp;
        this.packageName = packageName;
        this.camera_state = camera_state;
        this.mic_state = mic_state;
        this.loc_state = loc_state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getCamera_state() {
        return camera_state;
    }

    public int getMic_state() {
        return mic_state;
    }

    public int getLoc_state() {
        return loc_state;
    }
}
