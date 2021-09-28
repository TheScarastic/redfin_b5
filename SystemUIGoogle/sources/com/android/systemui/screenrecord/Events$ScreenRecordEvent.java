package com.android.systemui.screenrecord;

import com.android.internal.logging.UiEventLogger;
/* loaded from: classes.dex */
public enum Events$ScreenRecordEvent implements UiEventLogger.UiEventEnum {
    SCREEN_RECORD_START(299),
    SCREEN_RECORD_END_QS_TILE(300),
    SCREEN_RECORD_END_NOTIFICATION(301);
    
    private final int mId;

    Events$ScreenRecordEvent(int i) {
        this.mId = i;
    }

    public int getId() {
        return this.mId;
    }
}
