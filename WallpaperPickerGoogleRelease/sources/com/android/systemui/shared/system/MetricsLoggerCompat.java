package com.android.systemui.shared.system;

import com.android.internal.logging.MetricsLogger;
/* loaded from: classes.dex */
public class MetricsLoggerCompat {
    public static final int OVERVIEW_ACTIVITY = 224;
    private final MetricsLogger mMetricsLogger = new MetricsLogger();

    public void action(int i) {
        this.mMetricsLogger.action(i);
    }

    public void hidden(int i) {
        this.mMetricsLogger.hidden(i);
    }

    public void visibility(int i, boolean z) {
        this.mMetricsLogger.visibility(i, z);
    }

    public void visible(int i) {
        this.mMetricsLogger.visible(i);
    }

    public void action(int i, int i2) {
        this.mMetricsLogger.action(i, i2);
    }
}
