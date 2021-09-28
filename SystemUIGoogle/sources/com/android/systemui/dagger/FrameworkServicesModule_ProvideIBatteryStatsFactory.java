package com.android.systemui.dagger;

import com.android.internal.app.IBatteryStats;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIBatteryStatsFactory implements Factory<IBatteryStats> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIBatteryStatsFactory INSTANCE = new FrameworkServicesModule_ProvideIBatteryStatsFactory();
    }

    @Override // javax.inject.Provider
    public IBatteryStats get() {
        return provideIBatteryStats();
    }

    public static FrameworkServicesModule_ProvideIBatteryStatsFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IBatteryStats provideIBatteryStats() {
        return (IBatteryStats) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIBatteryStats());
    }
}
