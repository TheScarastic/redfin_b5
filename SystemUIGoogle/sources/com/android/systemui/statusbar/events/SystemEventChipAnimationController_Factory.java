package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.statusbar.SuperStatusBarViewFactory;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarWindowController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemEventChipAnimationController_Factory implements Factory<SystemEventChipAnimationController> {
    private final Provider<Context> contextProvider;
    private final Provider<StatusBarLocationPublisher> locationPublisherProvider;
    private final Provider<SuperStatusBarViewFactory> statusBarViewFactoryProvider;
    private final Provider<StatusBarWindowController> statusBarWindowControllerProvider;

    public SystemEventChipAnimationController_Factory(Provider<Context> provider, Provider<SuperStatusBarViewFactory> provider2, Provider<StatusBarWindowController> provider3, Provider<StatusBarLocationPublisher> provider4) {
        this.contextProvider = provider;
        this.statusBarViewFactoryProvider = provider2;
        this.statusBarWindowControllerProvider = provider3;
        this.locationPublisherProvider = provider4;
    }

    @Override // javax.inject.Provider
    public SystemEventChipAnimationController get() {
        return newInstance(this.contextProvider.get(), this.statusBarViewFactoryProvider.get(), this.statusBarWindowControllerProvider.get(), this.locationPublisherProvider.get());
    }

    public static SystemEventChipAnimationController_Factory create(Provider<Context> provider, Provider<SuperStatusBarViewFactory> provider2, Provider<StatusBarWindowController> provider3, Provider<StatusBarLocationPublisher> provider4) {
        return new SystemEventChipAnimationController_Factory(provider, provider2, provider3, provider4);
    }

    public static SystemEventChipAnimationController newInstance(Context context, SuperStatusBarViewFactory superStatusBarViewFactory, StatusBarWindowController statusBarWindowController, StatusBarLocationPublisher statusBarLocationPublisher) {
        return new SystemEventChipAnimationController(context, superStatusBarViewFactory, statusBarWindowController, statusBarLocationPublisher);
    }
}
