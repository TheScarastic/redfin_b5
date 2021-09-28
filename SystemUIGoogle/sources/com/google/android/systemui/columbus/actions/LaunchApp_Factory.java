package com.google.android.systemui.columbus.actions;

import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.UserManager;
import com.android.internal.logging.UiEventLogger;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.gates.KeyguardVisibility;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class LaunchApp_Factory implements Factory<LaunchApp> {
    private final Provider<IActivityManager> activityManagerServiceProvider;
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<ColumbusSettings> columbusSettingsProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardVisibility> keyguardVisibilityProvider;
    private final Provider<LauncherApps> launcherAppsProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<UserManager> userManagerProvider;

    public LaunchApp_Factory(Provider<Context> provider, Provider<LauncherApps> provider2, Provider<IActivityManager> provider3, Provider<UserManager> provider4, Provider<ColumbusSettings> provider5, Provider<KeyguardVisibility> provider6, Provider<Handler> provider7, Provider<Handler> provider8, Provider<UiEventLogger> provider9) {
        this.contextProvider = provider;
        this.launcherAppsProvider = provider2;
        this.activityManagerServiceProvider = provider3;
        this.userManagerProvider = provider4;
        this.columbusSettingsProvider = provider5;
        this.keyguardVisibilityProvider = provider6;
        this.mainHandlerProvider = provider7;
        this.bgHandlerProvider = provider8;
        this.uiEventLoggerProvider = provider9;
    }

    @Override // javax.inject.Provider
    public LaunchApp get() {
        return newInstance(this.contextProvider.get(), this.launcherAppsProvider.get(), this.activityManagerServiceProvider.get(), this.userManagerProvider.get(), this.columbusSettingsProvider.get(), this.keyguardVisibilityProvider.get(), this.mainHandlerProvider.get(), this.bgHandlerProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static LaunchApp_Factory create(Provider<Context> provider, Provider<LauncherApps> provider2, Provider<IActivityManager> provider3, Provider<UserManager> provider4, Provider<ColumbusSettings> provider5, Provider<KeyguardVisibility> provider6, Provider<Handler> provider7, Provider<Handler> provider8, Provider<UiEventLogger> provider9) {
        return new LaunchApp_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static LaunchApp newInstance(Context context, LauncherApps launcherApps, IActivityManager iActivityManager, UserManager userManager, ColumbusSettings columbusSettings, KeyguardVisibility keyguardVisibility, Handler handler, Handler handler2, UiEventLogger uiEventLogger) {
        return new LaunchApp(context, launcherApps, iActivityManager, userManager, columbusSettings, keyguardVisibility, handler, handler2, uiEventLogger);
    }
}
