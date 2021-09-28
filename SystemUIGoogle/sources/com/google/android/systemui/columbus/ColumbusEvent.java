package com.google.android.systemui.columbus;

import com.android.internal.logging.UiEventLogger;
/* compiled from: ColumbusEvent.kt */
/* loaded from: classes2.dex */
public enum ColumbusEvent implements UiEventLogger.UiEventEnum {
    COLUMBUS_UNKNOWN_EVENT(0),
    COLUMBUS_DOUBLE_TAP_DETECTED(628),
    COLUMBUS_INVOKED_ASSISTANT(629),
    COLUMBUS_INVOKED_SCREENSHOT(630),
    COLUMBUS_INVOKED_PLAY_MEDIA(631),
    COLUMBUS_INVOKED_PAUSE_MEDIA(639),
    COLUMBUS_INVOKED_OVERVIEW(632),
    COLUMBUS_INVOKED_NOTIFICATION_SHADE_OPEN(633),
    COLUMBUS_INVOKED_NOTIFICATION_SHADE_CLOSE(634),
    COLUMBUS_INVOKED_LAUNCH_APP(815),
    COLUMBUS_INVOKED_LAUNCH_SHORTCUT(816),
    COLUMBUS_INVOKED_ON_SETTINGS(817),
    COLUMBUS_MODE_LOW_POWER_ACTIVE(635),
    COLUMBUS_MODE_HIGH_POWER_ACTIVE(636),
    COLUMBUS_MODE_INACTIVE(637);
    
    private final int id;

    ColumbusEvent(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }
}
