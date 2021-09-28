package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ForegroundServiceDismissalFeatureController_Factory implements Factory<ForegroundServiceDismissalFeatureController> {
    private final Provider<Context> contextProvider;
    private final Provider<DeviceConfigProxy> proxyProvider;

    public ForegroundServiceDismissalFeatureController_Factory(Provider<DeviceConfigProxy> provider, Provider<Context> provider2) {
        this.proxyProvider = provider;
        this.contextProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ForegroundServiceDismissalFeatureController get() {
        return newInstance(this.proxyProvider.get(), this.contextProvider.get());
    }

    public static ForegroundServiceDismissalFeatureController_Factory create(Provider<DeviceConfigProxy> provider, Provider<Context> provider2) {
        return new ForegroundServiceDismissalFeatureController_Factory(provider, provider2);
    }

    public static ForegroundServiceDismissalFeatureController newInstance(DeviceConfigProxy deviceConfigProxy, Context context) {
        return new ForegroundServiceDismissalFeatureController(deviceConfigProxy, context);
    }
}
