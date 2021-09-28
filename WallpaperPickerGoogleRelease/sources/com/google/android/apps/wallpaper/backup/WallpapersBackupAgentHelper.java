package com.google.android.apps.wallpaper.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.google.android.apps.wallpaper.backdrop.BackdropAlarmScheduler;
import com.google.android.apps.wallpaper.backdrop.BackdropPreferences;
import com.google.android.apps.wallpaper.backdrop.BackdropTaskScheduler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class WallpapersBackupAgentHelper extends BackupAgentHelper {
    @Override // android.app.backup.BackupAgent
    public void onCreate() {
        addHelper("persistent_backup_agent_helper_prefs", new SharedPreferencesBackupHelper(this, "persistent_backup_agent_helper"));
        addHelper("wallpaper_prefs", new SharedPreferencesBackupHelper(this, "wallpaper"));
        addHelper("wallpaper-backdrop_prefs", new SharedPreferencesBackupHelper(this, "wallpaper-backdrop"));
    }

    @Override // android.app.backup.BackupAgentHelper, android.app.backup.BackupAgent
    public void onRestore(BackupDataInput backupDataInput, int i, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        super.onRestore(backupDataInput, i, parcelFileDescriptor);
        SharedPreferences sharedPreferences = getSharedPreferences("persistent_backup_agent_helper", 0);
        HashMap hashMap = new HashMap();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            int indexOf = key.indexOf("/");
            if (indexOf < 0 || indexOf >= key.length() - 1) {
                Log.w("WallpapersBackupAgent", "Format of key \"" + key + "\" not understood, so skipping its restore.");
            } else {
                String substring = key.substring(0, indexOf);
                String substring2 = key.substring(indexOf + 1);
                SharedPreferences.Editor editor = (SharedPreferences.Editor) hashMap.get(substring);
                if (editor == null) {
                    editor = getSharedPreferences(substring, 0).edit();
                    hashMap.put(substring, editor);
                }
                if (value instanceof Boolean) {
                    editor.putBoolean(substring2, ((Boolean) value).booleanValue());
                } else if (value instanceof Float) {
                    editor.putFloat(substring2, ((Float) value).floatValue());
                } else if (value instanceof Integer) {
                    editor.putInt(substring2, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    editor.putLong(substring2, ((Long) value).longValue());
                } else if (value instanceof String) {
                    editor.putString(substring2, (String) value);
                } else {
                    Class<?> cls = null;
                    if (value instanceof Set) {
                        Set<String> set = (Set) value;
                        Iterator<String> it = set.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                editor.putStringSet(substring2, set);
                                break;
                            } else if (!(it.next() instanceof String)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Skipping restore of key ");
                                sb.append(substring2);
                                sb.append(" because its value is a set containing an object of type ");
                                if (value != null) {
                                    cls = value.getClass();
                                }
                                sb.append(cls);
                                sb.append(".");
                                Log.w("WallpapersBackupAgent", sb.toString());
                            }
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Skipping restore of key ");
                        sb2.append(substring2);
                        sb2.append(" because its value is the unrecognized type ");
                        if (value != null) {
                            cls = value.getClass();
                        }
                        sb2.append(cls);
                        sb2.append(".");
                        Log.w("WallpapersBackupAgent", sb2.toString());
                    }
                }
            }
        }
        for (SharedPreferences.Editor editor2 : hashMap.values()) {
            editor2.apply();
        }
        getSharedPreferences("persistent_backup_agent_helper", 0).edit().clear().apply();
    }

    @Override // android.app.backup.BackupAgent
    public void onRestoreFinished() {
        Context applicationContext = getApplicationContext();
        Injector injector = InjectorProvider.getInjector();
        injector.getUserEventLogger(applicationContext).logRestored();
        if (2 == injector.getPreferences(applicationContext).getWallpaperPresentationMode()) {
            BackdropTaskScheduler.getInstance(applicationContext).scheduleOneOffTask(BackdropPreferences.getInstance(applicationContext).mSharedPrefs.getInt("required_network_state", 1), 0);
            BackdropAlarmScheduler.setOvernightAlarm(applicationContext);
        }
    }
}
