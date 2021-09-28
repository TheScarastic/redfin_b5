package com.android.systemui;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SliceBroadcastRelayHandler_Factory implements Factory<SliceBroadcastRelayHandler> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;

    public SliceBroadcastRelayHandler_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    @Override // javax.inject.Provider
    public SliceBroadcastRelayHandler get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static SliceBroadcastRelayHandler_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2) {
        return new SliceBroadcastRelayHandler_Factory(provider, provider2);
    }

    public static SliceBroadcastRelayHandler newInstance(Context context, BroadcastDispatcher broadcastDispatcher) {
        return new SliceBroadcastRelayHandler(context, broadcastDispatcher);
    }
}
