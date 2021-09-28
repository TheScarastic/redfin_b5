package com.android.systemui.shared.system;

import android.content.Context;
import com.android.internal.util.LatencyTracker;
/* loaded from: classes.dex */
public class LatencyTrackerCompat {
    @Deprecated
    public static void logToggleRecents(int i) {
        LatencyTracker.logActionDeprecated(1, i, false);
    }

    public static void logToggleRecents(Context context, int i) {
        LatencyTracker.getInstance(context).logAction(1, i);
    }
}
