package com.android.keyguard.clock;

import java.util.List;
/* loaded from: classes.dex */
public abstract class ClockModule {
    public static List<ClockInfo> provideClockInfoList(ClockManager clockManager) {
        return clockManager.getClockInfos();
    }
}
