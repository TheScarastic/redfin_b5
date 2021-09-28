package com.android.keyguard.clock;

import com.android.systemui.plugins.ClockPlugin;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final /* synthetic */ class ClockManager$AvailableClocks$$ExternalSyntheticLambda1 implements Supplier {
    public final /* synthetic */ ClockPlugin f$0;

    public /* synthetic */ ClockManager$AvailableClocks$$ExternalSyntheticLambda1(ClockPlugin clockPlugin) {
        this.f$0 = clockPlugin;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        return this.f$0.getThumbnail();
    }
}
