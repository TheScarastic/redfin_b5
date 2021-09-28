package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarTouchableRegionManager_Factory implements Factory<StatusBarTouchableRegionManager> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;

    public StatusBarTouchableRegionManager_Factory(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<ConfigurationController> provider3, Provider<HeadsUpManagerPhone> provider4) {
        this.contextProvider = provider;
        this.notificationShadeWindowControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.headsUpManagerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public StatusBarTouchableRegionManager get() {
        return newInstance(this.contextProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.configurationControllerProvider.get(), this.headsUpManagerProvider.get());
    }

    public static StatusBarTouchableRegionManager_Factory create(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<ConfigurationController> provider3, Provider<HeadsUpManagerPhone> provider4) {
        return new StatusBarTouchableRegionManager_Factory(provider, provider2, provider3, provider4);
    }

    public static StatusBarTouchableRegionManager newInstance(Context context, NotificationShadeWindowController notificationShadeWindowController, ConfigurationController configurationController, HeadsUpManagerPhone headsUpManagerPhone) {
        return new StatusBarTouchableRegionManager(context, notificationShadeWindowController, configurationController, headsUpManagerPhone);
    }
}
