package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.view.IWindowManager;
import android.view.WindowManager;
import com.android.systemui.statusbar.SuperStatusBarViewFactory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarWindowController_Factory implements Factory<StatusBarWindowController> {
    private final Provider<StatusBarContentInsetsProvider> contentInsetsProvider;
    private final Provider<Context> contextProvider;
    private final Provider<IWindowManager> iWindowManagerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<SuperStatusBarViewFactory> superStatusBarViewFactoryProvider;
    private final Provider<WindowManager> windowManagerProvider;

    public StatusBarWindowController_Factory(Provider<Context> provider, Provider<WindowManager> provider2, Provider<IWindowManager> provider3, Provider<SuperStatusBarViewFactory> provider4, Provider<StatusBarContentInsetsProvider> provider5, Provider<Resources> provider6) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.iWindowManagerProvider = provider3;
        this.superStatusBarViewFactoryProvider = provider4;
        this.contentInsetsProvider = provider5;
        this.resourcesProvider = provider6;
    }

    @Override // javax.inject.Provider
    public StatusBarWindowController get() {
        return newInstance(this.contextProvider.get(), this.windowManagerProvider.get(), this.iWindowManagerProvider.get(), this.superStatusBarViewFactoryProvider.get(), this.contentInsetsProvider.get(), this.resourcesProvider.get());
    }

    public static StatusBarWindowController_Factory create(Provider<Context> provider, Provider<WindowManager> provider2, Provider<IWindowManager> provider3, Provider<SuperStatusBarViewFactory> provider4, Provider<StatusBarContentInsetsProvider> provider5, Provider<Resources> provider6) {
        return new StatusBarWindowController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static StatusBarWindowController newInstance(Context context, WindowManager windowManager, IWindowManager iWindowManager, SuperStatusBarViewFactory superStatusBarViewFactory, StatusBarContentInsetsProvider statusBarContentInsetsProvider, Resources resources) {
        return new StatusBarWindowController(context, windowManager, iWindowManager, superStatusBarViewFactory, statusBarContentInsetsProvider, resources);
    }
}
