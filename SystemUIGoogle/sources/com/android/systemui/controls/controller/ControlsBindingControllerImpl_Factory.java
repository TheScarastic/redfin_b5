package com.android.systemui.controls.controller;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsBindingControllerImpl_Factory implements Factory<ControlsBindingControllerImpl> {
    private final Provider<DelayableExecutor> backgroundExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<ControlsController> controllerProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public ControlsBindingControllerImpl_Factory(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<ControlsController> provider3, Provider<UserTracker> provider4) {
        this.contextProvider = provider;
        this.backgroundExecutorProvider = provider2;
        this.controllerProvider = provider3;
        this.userTrackerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ControlsBindingControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.backgroundExecutorProvider.get(), DoubleCheck.lazy(this.controllerProvider), this.userTrackerProvider.get());
    }

    public static ControlsBindingControllerImpl_Factory create(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<ControlsController> provider3, Provider<UserTracker> provider4) {
        return new ControlsBindingControllerImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static ControlsBindingControllerImpl newInstance(Context context, DelayableExecutor delayableExecutor, Lazy<ControlsController> lazy, UserTracker userTracker) {
        return new ControlsBindingControllerImpl(context, delayableExecutor, lazy, userTracker);
    }
}
