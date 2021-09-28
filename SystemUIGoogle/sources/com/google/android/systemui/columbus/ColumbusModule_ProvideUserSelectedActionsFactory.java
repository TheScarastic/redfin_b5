package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.actions.LaunchApp;
import com.google.android.systemui.columbus.actions.LaunchOpa;
import com.google.android.systemui.columbus.actions.LaunchOverview;
import com.google.android.systemui.columbus.actions.ManageMedia;
import com.google.android.systemui.columbus.actions.OpenNotificationShade;
import com.google.android.systemui.columbus.actions.TakeScreenshot;
import com.google.android.systemui.columbus.actions.UserAction;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Map;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusModule_ProvideUserSelectedActionsFactory implements Factory<Map<String, UserAction>> {
    private final Provider<LaunchApp> launchAppProvider;
    private final Provider<LaunchOpa> launchOpaProvider;
    private final Provider<LaunchOverview> launchOverviewProvider;
    private final Provider<ManageMedia> manageMediaProvider;
    private final Provider<OpenNotificationShade> openNotificationShadeProvider;
    private final Provider<TakeScreenshot> takeScreenshotProvider;

    public ColumbusModule_ProvideUserSelectedActionsFactory(Provider<LaunchOpa> provider, Provider<ManageMedia> provider2, Provider<TakeScreenshot> provider3, Provider<LaunchOverview> provider4, Provider<OpenNotificationShade> provider5, Provider<LaunchApp> provider6) {
        this.launchOpaProvider = provider;
        this.manageMediaProvider = provider2;
        this.takeScreenshotProvider = provider3;
        this.launchOverviewProvider = provider4;
        this.openNotificationShadeProvider = provider5;
        this.launchAppProvider = provider6;
    }

    @Override // javax.inject.Provider
    public Map<String, UserAction> get() {
        return provideUserSelectedActions(this.launchOpaProvider.get(), this.manageMediaProvider.get(), this.takeScreenshotProvider.get(), this.launchOverviewProvider.get(), this.openNotificationShadeProvider.get(), this.launchAppProvider.get());
    }

    public static ColumbusModule_ProvideUserSelectedActionsFactory create(Provider<LaunchOpa> provider, Provider<ManageMedia> provider2, Provider<TakeScreenshot> provider3, Provider<LaunchOverview> provider4, Provider<OpenNotificationShade> provider5, Provider<LaunchApp> provider6) {
        return new ColumbusModule_ProvideUserSelectedActionsFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static Map<String, UserAction> provideUserSelectedActions(LaunchOpa launchOpa, ManageMedia manageMedia, TakeScreenshot takeScreenshot, LaunchOverview launchOverview, OpenNotificationShade openNotificationShade, LaunchApp launchApp) {
        return (Map) Preconditions.checkNotNullFromProvides(ColumbusModule.provideUserSelectedActions(launchOpa, manageMedia, takeScreenshot, launchOverview, openNotificationShade, launchApp));
    }
}
