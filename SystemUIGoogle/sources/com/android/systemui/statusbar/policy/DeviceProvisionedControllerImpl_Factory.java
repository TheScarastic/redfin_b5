package com.android.systemui.statusbar.policy;

import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DeviceProvisionedControllerImpl_Factory implements Factory<DeviceProvisionedControllerImpl> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<GlobalSettings> globalSettingsProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;

    public DeviceProvisionedControllerImpl_Factory(Provider<Handler> provider, Provider<BroadcastDispatcher> provider2, Provider<GlobalSettings> provider3, Provider<SecureSettings> provider4) {
        this.mainHandlerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.globalSettingsProvider = provider3;
        this.secureSettingsProvider = provider4;
    }

    @Override // javax.inject.Provider
    public DeviceProvisionedControllerImpl get() {
        return newInstance(this.mainHandlerProvider.get(), this.broadcastDispatcherProvider.get(), this.globalSettingsProvider.get(), this.secureSettingsProvider.get());
    }

    public static DeviceProvisionedControllerImpl_Factory create(Provider<Handler> provider, Provider<BroadcastDispatcher> provider2, Provider<GlobalSettings> provider3, Provider<SecureSettings> provider4) {
        return new DeviceProvisionedControllerImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static DeviceProvisionedControllerImpl newInstance(Handler handler, BroadcastDispatcher broadcastDispatcher, GlobalSettings globalSettings, SecureSettings secureSettings) {
        return new DeviceProvisionedControllerImpl(handler, broadcastDispatcher, globalSettings, secureSettings);
    }
}
