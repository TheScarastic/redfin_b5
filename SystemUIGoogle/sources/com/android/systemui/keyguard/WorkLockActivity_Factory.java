package com.android.systemui.keyguard;

import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WorkLockActivity_Factory implements Factory<WorkLockActivity> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;

    public WorkLockActivity_Factory(Provider<BroadcastDispatcher> provider) {
        this.broadcastDispatcherProvider = provider;
    }

    @Override // javax.inject.Provider
    public WorkLockActivity get() {
        return newInstance(this.broadcastDispatcherProvider.get());
    }

    public static WorkLockActivity_Factory create(Provider<BroadcastDispatcher> provider) {
        return new WorkLockActivity_Factory(provider);
    }

    public static WorkLockActivity newInstance(BroadcastDispatcher broadcastDispatcher) {
        return new WorkLockActivity(broadcastDispatcher);
    }
}
