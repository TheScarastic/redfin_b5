package com.android.wm.shell.onehanded;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.accessibility.AccessibilityShortcutController;
import com.android.wm.shell.R;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public final class OneHandedSettingsUtil {
    private static final String ONE_HANDED_MODE_TARGET_NAME = AccessibilityShortcutController.ONE_HANDED_COMPONENT_NAME.getShortClassName();

    public Uri registerSettingsKeyObserver(String str, ContentResolver contentResolver, ContentObserver contentObserver, int i) {
        Uri uriFor = Settings.Secure.getUriFor(str);
        if (!(contentResolver == null || uriFor == null)) {
            contentResolver.registerContentObserver(uriFor, false, contentObserver, i);
        }
        return uriFor;
    }

    public void unregisterSettingsKeyObserver(ContentResolver contentResolver, ContentObserver contentObserver) {
        if (contentResolver != null) {
            contentResolver.unregisterContentObserver(contentObserver);
        }
    }

    public boolean getSettingsOneHandedModeEnabled(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_enabled", 0, i) == 1;
    }

    public boolean setOneHandedModeEnabled(ContentResolver contentResolver, int i, int i2) {
        return Settings.Secure.putIntForUser(contentResolver, "one_handed_mode_enabled", i, i2);
    }

    public boolean getSettingsTapsAppToExit(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "taps_app_to_exit", 1, i) == 1;
    }

    public int getSettingsOneHandedModeTimeout(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_timeout", 8, i);
    }

    public boolean getSettingsSwipeToNotificationEnabled(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "swipe_bottom_to_notification_enabled", 0, i) == 1;
    }

    public int getTutorialShownCounts(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "one_handed_tutorial_show_count", 0, i);
    }

    public boolean getShortcutEnabled(ContentResolver contentResolver, int i) {
        String stringForUser = Settings.Secure.getStringForUser(contentResolver, "accessibility_button_targets", i);
        if (TextUtils.isEmpty(stringForUser)) {
            return false;
        }
        return stringForUser.contains(ONE_HANDED_MODE_TARGET_NAME);
    }

    public boolean getOneHandedModeActivated(ContentResolver contentResolver, int i) {
        return Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_activated", 0, i) == 1;
    }

    public boolean setOneHandedModeActivated(ContentResolver contentResolver, int i, int i2) {
        return Settings.Secure.putIntForUser(contentResolver, "one_handed_mode_activated", i, i2);
    }

    public int getTransitionDuration(Context context) {
        return context.getResources().getInteger(R.integer.config_one_handed_translate_animation_duration);
    }

    public float getTranslationFraction(Context context) {
        return context.getResources().getFraction(R.fraction.config_one_handed_offset, 1, 1);
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, ContentResolver contentResolver, int i) {
        printWriter.println("OneHandedSettingsUtil");
        printWriter.print("  isOneHandedModeEnable=");
        printWriter.println(getSettingsOneHandedModeEnabled(contentResolver, i));
        printWriter.print("  oneHandedTimeOut=");
        printWriter.println(getSettingsOneHandedModeTimeout(contentResolver, i));
        printWriter.print("  tapsAppToExit=");
        printWriter.println(getSettingsTapsAppToExit(contentResolver, i));
        printWriter.print("  shortcutActivated=");
        printWriter.println(getOneHandedModeActivated(contentResolver, i));
        printWriter.print("  tutorialShownCounts=");
        printWriter.println(getTutorialShownCounts(contentResolver, i));
    }
}
