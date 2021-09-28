package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GroupCoalescer_Factory implements Factory<GroupCoalescer> {
    private final Provider<SystemClock> clockProvider;
    private final Provider<GroupCoalescerLogger> loggerProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;

    public GroupCoalescer_Factory(Provider<DelayableExecutor> provider, Provider<SystemClock> provider2, Provider<GroupCoalescerLogger> provider3) {
        this.mainExecutorProvider = provider;
        this.clockProvider = provider2;
        this.loggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public GroupCoalescer get() {
        return newInstance(this.mainExecutorProvider.get(), this.clockProvider.get(), this.loggerProvider.get());
    }

    public static GroupCoalescer_Factory create(Provider<DelayableExecutor> provider, Provider<SystemClock> provider2, Provider<GroupCoalescerLogger> provider3) {
        return new GroupCoalescer_Factory(provider, provider2, provider3);
    }

    public static GroupCoalescer newInstance(DelayableExecutor delayableExecutor, SystemClock systemClock, GroupCoalescerLogger groupCoalescerLogger) {
        return new GroupCoalescer(delayableExecutor, systemClock, groupCoalescerLogger);
    }
}
