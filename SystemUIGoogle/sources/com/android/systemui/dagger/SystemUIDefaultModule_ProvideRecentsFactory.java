package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideRecentsFactory implements Factory<Recents> {
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<Context> contextProvider;
    private final Provider<RecentsImplementation> recentsImplementationProvider;

    public SystemUIDefaultModule_ProvideRecentsFactory(Provider<Context> provider, Provider<RecentsImplementation> provider2, Provider<CommandQueue> provider3) {
        this.contextProvider = provider;
        this.recentsImplementationProvider = provider2;
        this.commandQueueProvider = provider3;
    }

    @Override // javax.inject.Provider
    public Recents get() {
        return provideRecents(this.contextProvider.get(), this.recentsImplementationProvider.get(), this.commandQueueProvider.get());
    }

    public static SystemUIDefaultModule_ProvideRecentsFactory create(Provider<Context> provider, Provider<RecentsImplementation> provider2, Provider<CommandQueue> provider3) {
        return new SystemUIDefaultModule_ProvideRecentsFactory(provider, provider2, provider3);
    }

    public static Recents provideRecents(Context context, RecentsImplementation recentsImplementation, CommandQueue commandQueue) {
        return (Recents) Preconditions.checkNotNullFromProvides(SystemUIDefaultModule.provideRecents(context, recentsImplementation, commandQueue));
    }
}
