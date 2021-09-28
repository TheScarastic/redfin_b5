package com.android.systemui.theme;

import android.app.WallpaperManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ThemeOverlayController_Factory implements Factory<ThemeOverlayController> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<ThemeOverlayApplier> themeOverlayApplierProvider;
    private final Provider<UserManager> userManagerProvider;
    private final Provider<UserTracker> userTrackerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;
    private final Provider<WallpaperManager> wallpaperManagerProvider;

    public ThemeOverlayController_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Handler> provider3, Provider<Executor> provider4, Provider<Executor> provider5, Provider<ThemeOverlayApplier> provider6, Provider<SecureSettings> provider7, Provider<WallpaperManager> provider8, Provider<UserManager> provider9, Provider<DeviceProvisionedController> provider10, Provider<UserTracker> provider11, Provider<DumpManager> provider12, Provider<FeatureFlags> provider13, Provider<WakefulnessLifecycle> provider14) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.bgHandlerProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.bgExecutorProvider = provider5;
        this.themeOverlayApplierProvider = provider6;
        this.secureSettingsProvider = provider7;
        this.wallpaperManagerProvider = provider8;
        this.userManagerProvider = provider9;
        this.deviceProvisionedControllerProvider = provider10;
        this.userTrackerProvider = provider11;
        this.dumpManagerProvider = provider12;
        this.featureFlagsProvider = provider13;
        this.wakefulnessLifecycleProvider = provider14;
    }

    @Override // javax.inject.Provider
    public ThemeOverlayController get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.bgHandlerProvider.get(), this.mainExecutorProvider.get(), this.bgExecutorProvider.get(), this.themeOverlayApplierProvider.get(), this.secureSettingsProvider.get(), this.wallpaperManagerProvider.get(), this.userManagerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.userTrackerProvider.get(), this.dumpManagerProvider.get(), this.featureFlagsProvider.get(), this.wakefulnessLifecycleProvider.get());
    }

    public static ThemeOverlayController_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Handler> provider3, Provider<Executor> provider4, Provider<Executor> provider5, Provider<ThemeOverlayApplier> provider6, Provider<SecureSettings> provider7, Provider<WallpaperManager> provider8, Provider<UserManager> provider9, Provider<DeviceProvisionedController> provider10, Provider<UserTracker> provider11, Provider<DumpManager> provider12, Provider<FeatureFlags> provider13, Provider<WakefulnessLifecycle> provider14) {
        return new ThemeOverlayController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static ThemeOverlayController newInstance(Context context, BroadcastDispatcher broadcastDispatcher, Handler handler, Executor executor, Executor executor2, ThemeOverlayApplier themeOverlayApplier, SecureSettings secureSettings, WallpaperManager wallpaperManager, UserManager userManager, DeviceProvisionedController deviceProvisionedController, UserTracker userTracker, DumpManager dumpManager, FeatureFlags featureFlags, WakefulnessLifecycle wakefulnessLifecycle) {
        return new ThemeOverlayController(context, broadcastDispatcher, handler, executor, executor2, themeOverlayApplier, secureSettings, wallpaperManager, userManager, deviceProvisionedController, userTracker, dumpManager, featureFlags, wakefulnessLifecycle);
    }
}
