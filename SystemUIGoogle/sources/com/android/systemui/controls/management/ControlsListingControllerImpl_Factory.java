package com.android.systemui.controls.management;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsListingControllerImpl_Factory implements Factory<ControlsListingControllerImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public ControlsListingControllerImpl_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<UserTracker> provider3) {
        this.contextProvider = provider;
        this.executorProvider = provider2;
        this.userTrackerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ControlsListingControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.executorProvider.get(), this.userTrackerProvider.get());
    }

    public static ControlsListingControllerImpl_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<UserTracker> provider3) {
        return new ControlsListingControllerImpl_Factory(provider, provider2, provider3);
    }

    public static ControlsListingControllerImpl newInstance(Context context, Executor executor, UserTracker userTracker) {
        return new ControlsListingControllerImpl(context, executor, userTracker);
    }
}
