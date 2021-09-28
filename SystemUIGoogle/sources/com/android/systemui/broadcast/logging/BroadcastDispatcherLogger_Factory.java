package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BroadcastDispatcherLogger_Factory implements Factory<BroadcastDispatcherLogger> {
    private final Provider<LogBuffer> bufferProvider;

    public BroadcastDispatcherLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    public BroadcastDispatcherLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static BroadcastDispatcherLogger_Factory create(Provider<LogBuffer> provider) {
        return new BroadcastDispatcherLogger_Factory(provider);
    }

    public static BroadcastDispatcherLogger newInstance(LogBuffer logBuffer) {
        return new BroadcastDispatcherLogger(logBuffer);
    }
}
