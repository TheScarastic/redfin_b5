package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;
/* loaded from: classes2.dex */
public interface SettingsProxy {
    ContentResolver getContentResolver();

    String getStringForUser(String str, int i);

    Uri getUriFor(String str);

    boolean putStringForUser(String str, String str2, int i);

    boolean putStringForUser(String str, String str2, String str3, boolean z, int i, boolean z2);

    default int getUserId() {
        return getContentResolver().getUserId();
    }

    default void registerContentObserver(String str, ContentObserver contentObserver) {
        registerContentObserver(getUriFor(str), contentObserver);
    }

    default void registerContentObserver(Uri uri, ContentObserver contentObserver) {
        registerContentObserverForUser(uri, contentObserver, getUserId());
    }

    default void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver) {
        registerContentObserverForUser(uri, z, contentObserver, getUserId());
    }

    default void registerContentObserverForUser(String str, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(getUriFor(str), contentObserver, i);
    }

    default void registerContentObserverForUser(Uri uri, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(uri, false, contentObserver, i);
    }

    default void registerContentObserverForUser(Uri uri, boolean z, ContentObserver contentObserver, int i) {
        getContentResolver().registerContentObserver(uri, z, contentObserver, i);
    }

    default void unregisterContentObserver(ContentObserver contentObserver) {
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    default String getString(String str) {
        return getStringForUser(str, getUserId());
    }

    default boolean putString(String str, String str2) {
        return putStringForUser(str, str2, getUserId());
    }

    default int getInt(String str, int i) {
        return getIntForUser(str, i, getUserId());
    }

    default int getIntForUser(String str, int i, int i2) {
        String stringForUser = getStringForUser(str, i2);
        if (stringForUser == null) {
            return i;
        }
        try {
            return Integer.parseInt(stringForUser);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    default boolean putInt(String str, int i) {
        return putIntForUser(str, i, getUserId());
    }

    default boolean putIntForUser(String str, int i, int i2) {
        return putStringForUser(str, Integer.toString(i), i2);
    }

    default long getLong(String str, long j) {
        return getLongForUser(str, j, getUserId());
    }

    default long getLongForUser(String str, long j, int i) {
        String stringForUser = getStringForUser(str, i);
        if (stringForUser == null) {
            return j;
        }
        try {
            return Long.parseLong(stringForUser);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    default float getFloat(String str, float f) {
        return getFloatForUser(str, f, getUserId());
    }

    default float getFloatForUser(String str, float f, int i) {
        String stringForUser = getStringForUser(str, i);
        if (stringForUser == null) {
            return f;
        }
        try {
            return Float.parseFloat(stringForUser);
        } catch (NumberFormatException unused) {
            return f;
        }
    }

    default float getFloat(String str) throws Settings.SettingNotFoundException {
        return getFloatForUser(str, getUserId());
    }

    default float getFloatForUser(String str, int i) throws Settings.SettingNotFoundException {
        String stringForUser = getStringForUser(str, i);
        if (stringForUser != null) {
            try {
                return Float.parseFloat(stringForUser);
            } catch (NumberFormatException unused) {
                throw new Settings.SettingNotFoundException(str);
            }
        } else {
            throw new Settings.SettingNotFoundException(str);
        }
    }
}
