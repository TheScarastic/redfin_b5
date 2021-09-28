package com.android.systemui;

import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BootCompleteCacheImpl_Factory implements Factory<BootCompleteCacheImpl> {
    private final Provider<DumpManager> dumpManagerProvider;

    public BootCompleteCacheImpl_Factory(Provider<DumpManager> provider) {
        this.dumpManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public BootCompleteCacheImpl get() {
        return newInstance(this.dumpManagerProvider.get());
    }

    public static BootCompleteCacheImpl_Factory create(Provider<DumpManager> provider) {
        return new BootCompleteCacheImpl_Factory(provider);
    }

    public static BootCompleteCacheImpl newInstance(DumpManager dumpManager) {
        return new BootCompleteCacheImpl(dumpManager);
    }
}
