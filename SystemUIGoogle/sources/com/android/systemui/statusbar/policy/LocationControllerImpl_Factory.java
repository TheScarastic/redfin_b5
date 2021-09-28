package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LocationControllerImpl_Factory implements Factory<LocationControllerImpl> {
    private final Provider<AppOpsController> appOpsControllerProvider;
    private final Provider<Handler> backgroundHandlerProvider;
    private final Provider<BootCompleteCache> bootCompleteCacheProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Looper> mainLooperProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public LocationControllerImpl_Factory(Provider<Context> provider, Provider<AppOpsController> provider2, Provider<Looper> provider3, Provider<Handler> provider4, Provider<BroadcastDispatcher> provider5, Provider<BootCompleteCache> provider6, Provider<UserTracker> provider7) {
        this.contextProvider = provider;
        this.appOpsControllerProvider = provider2;
        this.mainLooperProvider = provider3;
        this.backgroundHandlerProvider = provider4;
        this.broadcastDispatcherProvider = provider5;
        this.bootCompleteCacheProvider = provider6;
        this.userTrackerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public LocationControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.appOpsControllerProvider.get(), this.mainLooperProvider.get(), this.backgroundHandlerProvider.get(), this.broadcastDispatcherProvider.get(), this.bootCompleteCacheProvider.get(), this.userTrackerProvider.get());
    }

    public static LocationControllerImpl_Factory create(Provider<Context> provider, Provider<AppOpsController> provider2, Provider<Looper> provider3, Provider<Handler> provider4, Provider<BroadcastDispatcher> provider5, Provider<BootCompleteCache> provider6, Provider<UserTracker> provider7) {
        return new LocationControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static LocationControllerImpl newInstance(Context context, AppOpsController appOpsController, Looper looper, Handler handler, BroadcastDispatcher broadcastDispatcher, BootCompleteCache bootCompleteCache, UserTracker userTracker) {
        return new LocationControllerImpl(context, appOpsController, looper, handler, broadcastDispatcher, bootCompleteCache, userTracker);
    }
}
