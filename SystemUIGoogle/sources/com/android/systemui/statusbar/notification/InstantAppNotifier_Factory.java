package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class InstantAppNotifier_Factory implements Factory<InstantAppNotifier> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Optional<LegacySplitScreen>> splitScreenOptionalProvider;
    private final Provider<Executor> uiBgExecutorProvider;

    public InstantAppNotifier_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<Executor> provider3, Provider<Optional<LegacySplitScreen>> provider4) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.uiBgExecutorProvider = provider3;
        this.splitScreenOptionalProvider = provider4;
    }

    @Override // javax.inject.Provider
    public InstantAppNotifier get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), this.uiBgExecutorProvider.get(), this.splitScreenOptionalProvider.get());
    }

    public static InstantAppNotifier_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<Executor> provider3, Provider<Optional<LegacySplitScreen>> provider4) {
        return new InstantAppNotifier_Factory(provider, provider2, provider3, provider4);
    }

    public static InstantAppNotifier newInstance(Context context, CommandQueue commandQueue, Executor executor, Optional<LegacySplitScreen> optional) {
        return new InstantAppNotifier(context, commandQueue, executor, optional);
    }
}
