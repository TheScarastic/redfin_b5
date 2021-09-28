package com.android.systemui.statusbar.events;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PrivacyDotViewController_Factory implements Factory<PrivacyDotViewController> {
    private final Provider<SystemStatusAnimationScheduler> animationSchedulerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<StatusBarContentInsetsProvider> contentInsetsProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<StatusBarStateController> stateControllerProvider;

    public PrivacyDotViewController_Factory(Provider<Executor> provider, Provider<StatusBarStateController> provider2, Provider<ConfigurationController> provider3, Provider<StatusBarContentInsetsProvider> provider4, Provider<SystemStatusAnimationScheduler> provider5) {
        this.mainExecutorProvider = provider;
        this.stateControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.contentInsetsProvider = provider4;
        this.animationSchedulerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public PrivacyDotViewController get() {
        return newInstance(this.mainExecutorProvider.get(), this.stateControllerProvider.get(), this.configurationControllerProvider.get(), this.contentInsetsProvider.get(), this.animationSchedulerProvider.get());
    }

    public static PrivacyDotViewController_Factory create(Provider<Executor> provider, Provider<StatusBarStateController> provider2, Provider<ConfigurationController> provider3, Provider<StatusBarContentInsetsProvider> provider4, Provider<SystemStatusAnimationScheduler> provider5) {
        return new PrivacyDotViewController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static PrivacyDotViewController newInstance(Executor executor, StatusBarStateController statusBarStateController, ConfigurationController configurationController, StatusBarContentInsetsProvider statusBarContentInsetsProvider, SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        return new PrivacyDotViewController(executor, statusBarStateController, configurationController, statusBarContentInsetsProvider, systemStatusAnimationScheduler);
    }
}
