package com.android.systemui;

import android.util.EventLog;
/* loaded from: classes.dex */
public class EventLogTags {
    public static void writeSysuiHeadsUpStatus(String str, int i) {
        EventLog.writeEvent(36001, str, Integer.valueOf(i));
    }

    public static void writeSysuiStatusBarState(int i, int i2, int i3, int i4, int i5, int i6) {
        EventLog.writeEvent(36004, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6));
    }

    public static void writeSysuiLockscreenGesture(int i, int i2, int i3) {
        EventLog.writeEvent(36021, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
    }
}
