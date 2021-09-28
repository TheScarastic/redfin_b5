package com.android.systemui.log;

import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogBufferFactory_Factory implements Factory<LogBufferFactory> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<LogcatEchoTracker> logcatEchoTrackerProvider;

    public LogBufferFactory_Factory(Provider<DumpManager> provider, Provider<LogcatEchoTracker> provider2) {
        this.dumpManagerProvider = provider;
        this.logcatEchoTrackerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public LogBufferFactory get() {
        return newInstance(this.dumpManagerProvider.get(), this.logcatEchoTrackerProvider.get());
    }

    public static LogBufferFactory_Factory create(Provider<DumpManager> provider, Provider<LogcatEchoTracker> provider2) {
        return new LogBufferFactory_Factory(provider, provider2);
    }

    public static LogBufferFactory newInstance(DumpManager dumpManager, LogcatEchoTracker logcatEchoTracker) {
        return new LogBufferFactory(dumpManager, logcatEchoTracker);
    }
}
