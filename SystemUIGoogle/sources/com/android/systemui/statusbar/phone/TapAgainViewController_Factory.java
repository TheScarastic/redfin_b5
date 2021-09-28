package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TapAgainViewController_Factory implements Factory<TapAgainViewController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<DelayableExecutor> delayableExecutorProvider;
    private final Provider<Long> doubleTapTimeMsProvider;
    private final Provider<TapAgainView> viewProvider;

    public TapAgainViewController_Factory(Provider<TapAgainView> provider, Provider<DelayableExecutor> provider2, Provider<ConfigurationController> provider3, Provider<Long> provider4) {
        this.viewProvider = provider;
        this.delayableExecutorProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.doubleTapTimeMsProvider = provider4;
    }

    @Override // javax.inject.Provider
    public TapAgainViewController get() {
        return newInstance(this.viewProvider.get(), this.delayableExecutorProvider.get(), this.configurationControllerProvider.get(), this.doubleTapTimeMsProvider.get().longValue());
    }

    public static TapAgainViewController_Factory create(Provider<TapAgainView> provider, Provider<DelayableExecutor> provider2, Provider<ConfigurationController> provider3, Provider<Long> provider4) {
        return new TapAgainViewController_Factory(provider, provider2, provider3, provider4);
    }

    public static TapAgainViewController newInstance(TapAgainView tapAgainView, DelayableExecutor delayableExecutor, ConfigurationController configurationController, long j) {
        return new TapAgainViewController(tapAgainView, delayableExecutor, configurationController, j);
    }
}
