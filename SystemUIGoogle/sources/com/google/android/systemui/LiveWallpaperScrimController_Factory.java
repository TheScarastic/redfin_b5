package com.google.android.systemui;

import android.app.AlarmManager;
import android.app.IWallpaperManager;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class LiveWallpaperScrimController_Factory implements Factory<LiveWallpaperScrimController> {
    private final Provider<AlarmManager> alarmManagerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<DelayedWakeLock.Builder> delayedWakeLockBuilderProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<LightBarController> lightBarControllerProvider;
    private final Provider<LockscreenWallpaper> lockscreenWallpaperProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
    private final Provider<IWallpaperManager> wallpaperManagerProvider;

    public LiveWallpaperScrimController_Factory(Provider<LightBarController> provider, Provider<DozeParameters> provider2, Provider<AlarmManager> provider3, Provider<KeyguardStateController> provider4, Provider<DelayedWakeLock.Builder> provider5, Provider<Handler> provider6, Provider<IWallpaperManager> provider7, Provider<LockscreenWallpaper> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<ConfigurationController> provider10, Provider<DockManager> provider11, Provider<Executor> provider12, Provider<UnlockedScreenOffAnimationController> provider13) {
        this.lightBarControllerProvider = provider;
        this.dozeParametersProvider = provider2;
        this.alarmManagerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.delayedWakeLockBuilderProvider = provider5;
        this.handlerProvider = provider6;
        this.wallpaperManagerProvider = provider7;
        this.lockscreenWallpaperProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
        this.configurationControllerProvider = provider10;
        this.dockManagerProvider = provider11;
        this.mainExecutorProvider = provider12;
        this.unlockedScreenOffAnimationControllerProvider = provider13;
    }

    @Override // javax.inject.Provider
    public LiveWallpaperScrimController get() {
        return newInstance(this.lightBarControllerProvider.get(), this.dozeParametersProvider.get(), this.alarmManagerProvider.get(), this.keyguardStateControllerProvider.get(), this.delayedWakeLockBuilderProvider.get(), this.handlerProvider.get(), this.wallpaperManagerProvider.get(), this.lockscreenWallpaperProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.configurationControllerProvider.get(), this.dockManagerProvider.get(), this.mainExecutorProvider.get(), this.unlockedScreenOffAnimationControllerProvider.get());
    }

    public static LiveWallpaperScrimController_Factory create(Provider<LightBarController> provider, Provider<DozeParameters> provider2, Provider<AlarmManager> provider3, Provider<KeyguardStateController> provider4, Provider<DelayedWakeLock.Builder> provider5, Provider<Handler> provider6, Provider<IWallpaperManager> provider7, Provider<LockscreenWallpaper> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<ConfigurationController> provider10, Provider<DockManager> provider11, Provider<Executor> provider12, Provider<UnlockedScreenOffAnimationController> provider13) {
        return new LiveWallpaperScrimController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static LiveWallpaperScrimController newInstance(LightBarController lightBarController, DozeParameters dozeParameters, AlarmManager alarmManager, KeyguardStateController keyguardStateController, DelayedWakeLock.Builder builder, Handler handler, IWallpaperManager iWallpaperManager, LockscreenWallpaper lockscreenWallpaper, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, DockManager dockManager, Executor executor, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        return new LiveWallpaperScrimController(lightBarController, dozeParameters, alarmManager, keyguardStateController, builder, handler, iWallpaperManager, lockscreenWallpaper, keyguardUpdateMonitor, configurationController, dockManager, executor, unlockedScreenOffAnimationController);
    }
}
