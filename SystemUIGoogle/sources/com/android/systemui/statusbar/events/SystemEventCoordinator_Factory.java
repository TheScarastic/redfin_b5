package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemEventCoordinator_Factory implements Factory<SystemEventCoordinator> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<PrivacyItemController> privacyControllerProvider;
    private final Provider<SystemClock> systemClockProvider;

    public SystemEventCoordinator_Factory(Provider<SystemClock> provider, Provider<BatteryController> provider2, Provider<PrivacyItemController> provider3, Provider<Context> provider4) {
        this.systemClockProvider = provider;
        this.batteryControllerProvider = provider2;
        this.privacyControllerProvider = provider3;
        this.contextProvider = provider4;
    }

    @Override // javax.inject.Provider
    public SystemEventCoordinator get() {
        return newInstance(this.systemClockProvider.get(), this.batteryControllerProvider.get(), this.privacyControllerProvider.get(), this.contextProvider.get());
    }

    public static SystemEventCoordinator_Factory create(Provider<SystemClock> provider, Provider<BatteryController> provider2, Provider<PrivacyItemController> provider3, Provider<Context> provider4) {
        return new SystemEventCoordinator_Factory(provider, provider2, provider3, provider4);
    }

    public static SystemEventCoordinator newInstance(SystemClock systemClock, BatteryController batteryController, PrivacyItemController privacyItemController, Context context) {
        return new SystemEventCoordinator(systemClock, batteryController, privacyItemController, context);
    }
}
