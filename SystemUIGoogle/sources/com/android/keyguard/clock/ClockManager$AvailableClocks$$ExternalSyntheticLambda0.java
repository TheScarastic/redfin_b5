package com.android.keyguard.clock;

import com.android.keyguard.clock.ClockManager;
import com.android.systemui.plugins.ClockPlugin;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final /* synthetic */ class ClockManager$AvailableClocks$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ ClockManager.AvailableClocks f$0;
    public final /* synthetic */ ClockPlugin f$1;

    public /* synthetic */ ClockManager$AvailableClocks$$ExternalSyntheticLambda0(ClockManager.AvailableClocks availableClocks, ClockPlugin clockPlugin) {
        this.f$0 = availableClocks;
        this.f$1 = clockPlugin;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        return ClockManager.AvailableClocks.$r8$lambda$hLHTYHEn_sJRR4dSOCFM0ZDNShg(this.f$0, this.f$1);
    }
}
