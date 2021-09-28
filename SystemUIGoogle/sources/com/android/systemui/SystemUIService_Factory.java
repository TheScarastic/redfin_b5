package com.android.systemui;

import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.LogBufferFreezer;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIService_Factory implements Factory<SystemUIService> {
    private final Provider<BatteryStateNotifier> batteryStateNotifierProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<DumpHandler> dumpHandlerProvider;
    private final Provider<LogBufferFreezer> logBufferFreezerProvider;
    private final Provider<Handler> mainHandlerProvider;

    public SystemUIService_Factory(Provider<Handler> provider, Provider<DumpHandler> provider2, Provider<BroadcastDispatcher> provider3, Provider<LogBufferFreezer> provider4, Provider<BatteryStateNotifier> provider5) {
        this.mainHandlerProvider = provider;
        this.dumpHandlerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.logBufferFreezerProvider = provider4;
        this.batteryStateNotifierProvider = provider5;
    }

    @Override // javax.inject.Provider
    public SystemUIService get() {
        return newInstance(this.mainHandlerProvider.get(), this.dumpHandlerProvider.get(), this.broadcastDispatcherProvider.get(), this.logBufferFreezerProvider.get(), this.batteryStateNotifierProvider.get());
    }

    public static SystemUIService_Factory create(Provider<Handler> provider, Provider<DumpHandler> provider2, Provider<BroadcastDispatcher> provider3, Provider<LogBufferFreezer> provider4, Provider<BatteryStateNotifier> provider5) {
        return new SystemUIService_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static SystemUIService newInstance(Handler handler, DumpHandler dumpHandler, BroadcastDispatcher broadcastDispatcher, LogBufferFreezer logBufferFreezer, BatteryStateNotifier batteryStateNotifier) {
        return new SystemUIService(handler, dumpHandler, broadcastDispatcher, logBufferFreezer, batteryStateNotifier);
    }
}
