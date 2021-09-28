package com.android.systemui.power;

import com.android.settingslib.fuelgauge.Estimate;
/* loaded from: classes.dex */
public interface EnhancedEstimates {
    Estimate getEstimate();

    boolean getLowWarningEnabled();

    long getLowWarningThreshold();

    long getSevereWarningThreshold();

    boolean isHybridNotificationEnabled();
}
