package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemKeyPress_Factory implements Factory<SystemKeyPress> {
    private final Provider<Set<Integer>> blockingKeysProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Long> gateDurationProvider;
    private final Provider<Handler> handlerProvider;

    public SystemKeyPress_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<CommandQueue> provider3, Provider<Long> provider4, Provider<Set<Integer>> provider5) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.commandQueueProvider = provider3;
        this.gateDurationProvider = provider4;
        this.blockingKeysProvider = provider5;
    }

    @Override // javax.inject.Provider
    public SystemKeyPress get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.commandQueueProvider.get(), this.gateDurationProvider.get().longValue(), this.blockingKeysProvider.get());
    }

    public static SystemKeyPress_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<CommandQueue> provider3, Provider<Long> provider4, Provider<Set<Integer>> provider5) {
        return new SystemKeyPress_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static SystemKeyPress newInstance(Context context, Handler handler, CommandQueue commandQueue, long j, Set<Integer> set) {
        return new SystemKeyPress(context, handler, commandQueue, j, set);
    }
}
