package com.google.android.apps.wallpaper.backdrop;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import com.android.wallpaper.module.DefaultWallpaperPreferences$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public class BackdropPreferences {
    public static BackdropPreferences sInstance;
    public SharedPreferences mSharedPrefs;

    public BackdropPreferences(Context context) {
        this.mSharedPrefs = context.getSharedPreferences("wallpaper-backdrop", 0);
        final BackupManager backupManager = new BackupManager(context);
        this.mSharedPrefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener(this) { // from class: com.google.android.apps.wallpaper.backdrop.BackdropPreferences.1
            @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
                backupManager.dataChanged();
            }
        });
    }

    public static BackdropPreferences getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BackdropPreferences(context);
        }
        return sInstance;
    }

    public void clear() {
        this.mSharedPrefs.edit().remove("collection_id").remove("collection_name").remove("required_network_state").remove("resume_token").apply();
    }

    public String getCollectionId() {
        return this.mSharedPrefs.getString("collection_id", null);
    }

    public String getCollectionName() {
        return this.mSharedPrefs.getString("collection_name", null);
    }

    public String getResumeToken() {
        return this.mSharedPrefs.getString("resume_token", null);
    }

    public void setResumeToken(String str) {
        DefaultWallpaperPreferences$$ExternalSyntheticOutline0.m(this.mSharedPrefs, "resume_token", str);
    }
}
