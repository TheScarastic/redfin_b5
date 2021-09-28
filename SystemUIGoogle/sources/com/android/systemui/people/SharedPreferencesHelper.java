package com.android.systemui.people;

import android.content.SharedPreferences;
import com.android.systemui.people.widget.PeopleTileKey;
/* loaded from: classes.dex */
public class SharedPreferencesHelper {
    public static void clear(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.apply();
    }

    public static void setPeopleTileKey(SharedPreferences sharedPreferences, PeopleTileKey peopleTileKey) {
        setPeopleTileKey(sharedPreferences, peopleTileKey.getShortcutId(), peopleTileKey.getUserId(), peopleTileKey.getPackageName());
    }

    public static void setPeopleTileKey(SharedPreferences sharedPreferences, String str, int i, String str2) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("shortcut_id", str);
        edit.putInt("user_id", i);
        edit.putString("package_name", str2);
        edit.apply();
    }

    public static PeopleTileKey getPeopleTileKey(SharedPreferences sharedPreferences) {
        return new PeopleTileKey(sharedPreferences.getString("shortcut_id", null), sharedPreferences.getInt("user_id", -1), sharedPreferences.getString("package_name", null));
    }
}
