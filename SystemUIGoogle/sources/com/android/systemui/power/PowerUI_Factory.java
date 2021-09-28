package com.android.systemui.power;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PowerUI_Factory implements Factory<PowerUI> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<StatusBar> statusBarLazyProvider;

    public PowerUI_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<CommandQueue> provider3, Provider<StatusBar> provider4) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.commandQueueProvider = provider3;
        this.statusBarLazyProvider = provider4;
    }

    @Override // javax.inject.Provider
    public PowerUI get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.commandQueueProvider.get(), DoubleCheck.lazy(this.statusBarLazyProvider));
    }

    public static PowerUI_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<CommandQueue> provider3, Provider<StatusBar> provider4) {
        return new PowerUI_Factory(provider, provider2, provider3, provider4);
    }

    public static PowerUI newInstance(Context context, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Lazy<StatusBar> lazy) {
        return new PowerUI(context, broadcastDispatcher, commandQueue, lazy);
    }
}
