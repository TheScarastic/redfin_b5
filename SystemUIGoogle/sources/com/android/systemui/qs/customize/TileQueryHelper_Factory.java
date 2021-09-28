package com.android.systemui.qs.customize;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TileQueryHelper_Factory implements Factory<TileQueryHelper> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public TileQueryHelper_Factory(Provider<Context> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<Executor> provider4) {
        this.contextProvider = provider;
        this.userTrackerProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.bgExecutorProvider = provider4;
    }

    @Override // javax.inject.Provider
    public TileQueryHelper get() {
        return newInstance(this.contextProvider.get(), this.userTrackerProvider.get(), this.mainExecutorProvider.get(), this.bgExecutorProvider.get());
    }

    public static TileQueryHelper_Factory create(Provider<Context> provider, Provider<UserTracker> provider2, Provider<Executor> provider3, Provider<Executor> provider4) {
        return new TileQueryHelper_Factory(provider, provider2, provider3, provider4);
    }

    public static TileQueryHelper newInstance(Context context, UserTracker userTracker, Executor executor, Executor executor2) {
        return new TileQueryHelper(context, userTracker, executor, executor2);
    }
}
