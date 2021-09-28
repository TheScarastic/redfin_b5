package com.google.android.systemui.assist.uihints;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyboardMonitor_Factory implements Factory<KeyboardMonitor> {
    private final Provider<Optional<CommandQueue>> commandQueueOptionalProvider;
    private final Provider<Context> contextProvider;

    public KeyboardMonitor_Factory(Provider<Context> provider, Provider<Optional<CommandQueue>> provider2) {
        this.contextProvider = provider;
        this.commandQueueOptionalProvider = provider2;
    }

    @Override // javax.inject.Provider
    public KeyboardMonitor get() {
        return newInstance(this.contextProvider.get(), this.commandQueueOptionalProvider.get());
    }

    public static KeyboardMonitor_Factory create(Provider<Context> provider, Provider<Optional<CommandQueue>> provider2) {
        return new KeyboardMonitor_Factory(provider, provider2);
    }

    public static KeyboardMonitor newInstance(Context context, Optional<CommandQueue> optional) {
        return new KeyboardMonitor(context, optional);
    }
}
