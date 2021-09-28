package com.google.android.systemui.elmyra;

import com.android.internal.logging.UiEventLogger;
/* compiled from: ElmyraEvent.kt */
/* loaded from: classes2.dex */
public enum ElmyraEvent implements UiEventLogger.UiEventEnum {
    ELMYRA_PRIMED(559),
    ELMYRA_RELEASED(560),
    ELMYRA_TRIGGERED_AP_SUSPENDED(561),
    ELMYRA_TRIGGERED_SCREEN_OFF(575),
    ELMYRA_TRIGGERED_SCREEN_ON(576);
    
    private final int id;

    ElmyraEvent(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }
}
